package com.surevine.alfresco.webscript.gsa.getallitems;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.SimpleTimeZone;

import org.alfresco.service.cmr.repository.InvalidNodeRefException;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.StoreRef;
import org.alfresco.service.cmr.search.SearchParameters;
import org.alfresco.service.cmr.search.SearchService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.surevine.alfresco.webscript.gsa.exception.GSAInvalidParameterException;
import com.surevine.alfresco.webscript.gsa.exception.GSAProcessingException;


/**
 * Business logic implementation of the GetAllItems service as defined by the GSA ICD
 * @author simonw
 *
 */
public class GetAllItemsCommandImpl implements GetAllItemsCommand {
 
	
	private static final Log _logger = LogFactory.getLog(GetAllItemsCommandImpl.class);
	
	private int errorCount=0;
	
	private static final SimpleTimeZone GMT = new SimpleTimeZone(0, "GMT");
	
	//Dependencies
	private SearchService _searchService;
	private GSANodePropertyService _gsaNodePropertyService;

	//Adjustable via Spring, this is an array of mimetypes to limit return values to.  Lucene wildcards are permitted
	private String[] _mimeTypes={"text/*"};
	
	//If this many individual items fail to parse in a row, we call a systemic error and throw in the towel
	private int _systemicParseExceptionThreshold=100;

	//Default values we expect to be overriden by spring
	private int _hardMaxResultsLimit=1000; 
	private int _softMaxResultsSizeLimitInBytes=1024*1024*10; //10 Meg
	
	private static final StoreRef SPACES_STORE= new StoreRef(StoreRef.PROTOCOL_WORKSPACE, "SpacesStore");
	
	private static final String LUCENE_DATE_FORMAT="yyyy-MM-dd'T'HH:mm:ss:SSS'Z'";

	public SearchService getSearchService()
	{
		return _searchService;
	}
	
	public GSANodePropertyService getGSANodePropertyService()
	{
		return _gsaNodePropertyService;
	}
	
	public void setMimeTypes(String[] mimeTypes)
	{
		_mimeTypes=mimeTypes;
	}
	
	public void setSystemicParseExceptionThreshold(int threshold)
	{
		_systemicParseExceptionThreshold=threshold;
	}
	
	/**
	 * Regardless of what the caller asks for, never retrieve more than this number of results 
	 * @throws GSAInvalidParameterException 
	 */
	public void setHardMaxResultsLimit(int limit) throws GSAInvalidParameterException
	{
		if (limit<1)
		{
			throw new GSAInvalidParameterException("Results limit must be >1 and was specified as "+limit, null, 10);
		}
		if (_logger.isInfoEnabled())
		{
			_logger.info("hardMaxResultsLimit: "+limit);
		}
		_hardMaxResultsLimit=limit;
	}
	
	/**
	 * Try not to retrieve results whose total content is much more than this limit.  This limit will be 
	 * exceeded during normal operation, but only so that the system can finish rendition of the current item
	 * @param limit
	 * @throws GSAInvalidParameterException 
	 */
	public void setSoftMaxResultsSizeLimitInBytes(int limit) throws GSAInvalidParameterException
	{
		if (limit<1)
		{
			throw new GSAInvalidParameterException("Size limit must be >1 and was specified as "+limit, null, 11);
		}
		if (_logger.isInfoEnabled())
		{
			_logger.info("softMaxResultsSizeLimitInBytes: "+limit);
		}
		_softMaxResultsSizeLimitInBytes=limit;
	}
	
	//See the interface for details of these methods
	@Override
	public SearchResults getAllItems(Date since) throws GSAInvalidParameterException {
		checkSince(since);
		return getAllItemsImpl(since, null, Integer.MAX_VALUE);
	}

	@Override
	public SearchResults getAllItems(Date since, NodeRef startAt) throws GSAInvalidParameterException {
		checkSince(since);
		checkStartAt(startAt);
		return getAllItemsImpl(since, startAt,  Integer.MAX_VALUE);
	}

	@Override
	public SearchResults getAllItems(Date since, int maxItems) throws GSAInvalidParameterException {
		checkSince(since);
		checkMaxItems(maxItems);
		return getAllItemsImpl(since, null,maxItems);
	}

	@Override
	public SearchResults getAllItems(Date since, NodeRef startAt, int maxItems) throws GSAInvalidParameterException {
		checkSince(since);
		checkStartAt(startAt);
		checkMaxItems(maxItems);
		return getAllItemsImpl(since, startAt, maxItems);
	}
	
	protected String getMimeTypeQueryPart()
	{
		StringBuilder query = new StringBuilder(100);
		query.append("+(");
		for (int i=0; i < _mimeTypes.length; i++)
		{
			query.append("@\\{http\\://www.alfresco.org/model/content/1.0\\}content.mimetype:");
			query.append(_mimeTypes[i]);
			if (i < _mimeTypes.length-1)
			{
				query.append(" or ");
			}
		}
		query.append(") ");
		return query.toString();
	}
	
	private final SearchResult createSearchResult(NodeRef nr)
	{
		SearchResult result = new SearchResultImpl();
		result.setNodeRef(nr);
		result.setLastModifiedDate(_gsaNodePropertyService.getModifiedDate(nr));
		result.setSecurityLabel(_gsaNodePropertyService.getSecurityLabel(nr));
		result.setTitle(_gsaNodePropertyService.getTitle(nr));
		result.setURL(_gsaNodePropertyService.getURL(nr));
		result.setContent(_gsaNodePropertyService.getContent(nr));
		result.setMimeType(_gsaNodePropertyService.getMimeType(nr));
		result.setModifiedBy(_gsaNodePropertyService.getModifiedBy(nr));
		result.setDocumentType(_gsaNodePropertyService.getDocumentType(nr));
		return result;
	}
	
	/**
	 * All the interface methods delegate down to this implementation method to retrieve a "get all items" page.
	 * This method performs the logic for a getAllitems call on content items, then delegates to another method
	 * to retrieve profile results, before merging the two result sets together.
	 * @param since Only retrieve items modified since this date
	 * @param maxItems A hint as to the number of items the caller wishes to retrieve.  The system must not return more
	 * than this number of items, but may return less.  If it does, this is indicated in the return value
	 * @param startAt If multiple items are tied for the "earliest" item within the search and one of them is this item,
	 * discard all the items that occur before this item according to some deterministic ordering
	 * @return SearchResults objects encapsulating the results of this search
	 * @throws GSAProcessingException in certain circumstances.  The code tries to distinguish between errors with parsing
	 * a subset of data points and systemic errors.  The heuristic used is that if 100 items fail to parse in a row (this
	 * count persists accross service calls) AND no items within the current batch have parsed then we call a systemic 
     * error and throw an exception.  We have to use behaviour like this because the ICD doesn't allow for a partial 
     * success return value
	 */
	private SearchResults getAllItemsImpl(Date since, NodeRef startAt, int maxItems)
	{
		//Respect the hard results limit
		if (maxItems > _hardMaxResultsLimit)
		{
			if (_logger.isInfoEnabled())
			{
				_logger.info("Results size specified was "+maxItems+" but this is greater than the hard limit of "+_hardMaxResultsLimit+" so using the hard limit");
			}
			maxItems=_hardMaxResultsLimit;
		}
		
		//Generate the lucene query and log
		StringBuilder sb = new StringBuilder(300);

		sb.append("+((PATH:\"/app:company_home/st:sites//*\" AND ("+getMimeTypeQueryPart() +"))) "+
				"+ (@cm\\:modified:["+DateToLuceneDateString(since)+" TO MAX ] )");
		sb.append("-TYPE:\"fm:topic\" -TYPE:\"cm:folder\" -TYPE:\"cm:thumbnail\"");	
		String queryString = sb.toString();
		
		SearchParameters sp = new SearchParameters();
		sp.addStore(SPACES_STORE);
		sp.setLanguage(SearchService.LANGUAGE_LUCENE);
		sp.setQuery(queryString);
		sp.addSort("@cm:modified", true);

		if (_logger.isDebugEnabled())
		{
			_logger.debug("Running search: "+queryString);
		}
				
		//Actually run the search and get the results as an Iterator of NodeRefs 
		Iterator<NodeRef> results = _searchService.query(sp).getNodeRefs().iterator();
		List<SearchResult> workingResults = new ArrayList<SearchResult>(maxItems);
		
		//Respecting the specified limits, turn the iterator of NodeRefs into a list of Objects of type SearchResult
		int resultsSoFar=0;
		int bytesSoFar=0;
		while(results.hasNext() && bytesSoFar < _softMaxResultsSizeLimitInBytes && resultsSoFar < maxItems)
		{
			NodeRef nr = results.next();
			try
			{	
				//Alfresco only indexes by date not time (!)  So we're going to get back some documents which are too early and we need to throw away
				Date lastModifiedDate = _gsaNodePropertyService.getModifiedDate(nr);
				
				if (lastModifiedDate!=null && !lastModifiedDate.before(since))
				{			
					if (_logger.isDebugEnabled())
					{
						_logger.debug("Adding "+nr +" to results list with date "+lastModifiedDate);
					}

					SearchResult result = createSearchResult(nr);
					bytesSoFar+=_gsaNodePropertyService.getContentSize(nr);
					workingResults.add(result);
					resultsSoFar++;
					errorCount=0; // Reset the error count as by this point we have succesfully retrieved a result
				}
				else
				{
					if (_logger.isDebugEnabled())
					{
						_logger.debug("Ignoring "+nr+" as it's last modified date of "+lastModifiedDate+" is before the since value");
					}
				}
			}
			catch (GSAProcessingException e)
			{
				errorCount++;
				_logger.warn("Error creating a record in the results set for "+queryString, e);
			}
			catch (InvalidNodeRefException ex)
			{
				errorCount++;
				_logger.warn("Found an invalid node with reference: "+ ex.getNodeRef().toString());
			}
			catch (NullPointerException npe) {
				errorCount++;
				_logger.warn("NullPointerException during parsing of result set.  This is usually benign, but please contact support if these occur alongside undesirable behaviour", npe);
			}
			catch (Exception exx) {
				errorCount++;
				_logger.warn("An unexpected exeption occurred during parsing of result set.  This may be benign, and indexing has not been stopped, but please report this issue to support so that the cause can be analysed", exx);
			}
			if (errorCount > (_systemicParseExceptionThreshold-1) && resultsSoFar==0)
			{
				throw new GSAProcessingException ("Systemic error detected.  Batch returned "+errorCount+" errors and no successes", 29108265);
			}
		}
		
		if (_logger.isInfoEnabled())
		{
			_logger.info(resultsSoFar+" results returned");
		}
		
		List<SearchResult> profileResults=null;
		if (workingResults.size()>0)
		{
			//Get profile results - we know we don't need to take anything with a date later than the latest thing in the content results list
			profileResults = getProfileResults(since, workingResults.get(workingResults.size()-1).getLastModifiedDate());
		}
		else
		{
			//Just use profile results with no maximum date (well, use "now") as there are no normal results
			profileResults = getProfileResults(since, new Date());
		}
			if (profileResults.size()>0) //merge profile and normal results if required
			{
				workingResults.addAll(profileResults);
				Collections.sort(workingResults);
				while (workingResults.size() > maxItems)
				{
					if (_logger.isDebugEnabled())
					{
						_logger.debug("MergeProfile: Removing "+workingResults.get(workingResults.size()-1).getTitle()+" from results list - last modified date = "+workingResults.get(workingResults.size()-1).getLastModifiedDate());
					}
					workingResults.remove(workingResults.size()-1);
				}
				if (_logger.isDebugEnabled())
				{
					_logger.debug("MergeProfile: Search results runs until "+workingResults.get(workingResults.size()-1).getLastModifiedDate());
				}
			}
		
		
		//If startAt is specified, perform the relevant filtering logic - usually, this will leave workingResults unchanged
		if (startAt!=null && workingResults.size()>0)
		{
			workingResults = resolveDateCollisionsWithStartAt(workingResults, startAt);
		}
		else {
			if (_logger.isDebugEnabled())
			{
				_logger.debug("startAt not specified so returning all results within merged results list");
			}
		}
		
		//Special case - if resolving date collisions has left us with no results at all, then return the next result in the underlying list, even if it would normally fall outside of the input params.  
		//This keeps a parse going where it might otherwise stall
		if (workingResults.size()==0 && results.hasNext())
		{
			if (_logger.isDebugEnabled())
			{
				_logger.debug("Anti-stall - returning next item in results list");
			}
			workingResults = new ArrayList<SearchResult>(1);
			workingResults.add(createSearchResult(results.next()));
		}
		
		//Wrap the collection of SearchResult objects into a SearchResults object and calculate whether more results are available
		SearchResults finalResults = new SearchResultsImpl();
		finalResults.setResults(workingResults);
		finalResults.moreAvailable(results.hasNext());
		
		return finalResults;
		
	}
	
	protected List<SearchResult> getProfileResults(Date from, Date to)
	{
		//Generate the lucene query and log
		StringBuilder sb = new StringBuilder(300);

		sb.append("+PATH:\"/sys:system/sys:people/*\" +@up\\:lastModified:["+DateToLuceneDateString(from)+" TO "+DateToLuceneDateString(to)+"]");
		String queryString = sb.toString();
		
		SearchParameters sp = new SearchParameters();
		sp.addStore(SPACES_STORE);
		sp.setLanguage(SearchService.LANGUAGE_LUCENE);
		sp.setQuery(queryString);
		sp.addSort("@up:lastModified", true);
		if (_logger.isDebugEnabled())
		{
			_logger.debug("GetProfile: Running people search: "+queryString);
		}
				
		//Actually run the search and get the results as an Iterator of NodeRefs 
		Iterator<NodeRef> results = _searchService.query(sp).getNodeRefs().iterator();
		List<SearchResult> workingResults = new ArrayList<SearchResult>();
		while (results.hasNext())
		{
			NodeRef nodeRef = results.next();
			if (!_gsaNodePropertyService.getModifiedDate(nodeRef).before(from))
			{
				SearchResult result = createSearchResult(nodeRef);
				workingResults.add(result);
				if (_logger.isDebugEnabled())
				{
					_logger.debug("GetProfile: Adding profile for "+result.getTitle()+" to profile results list");
				}
			}
		}
		return workingResults;
	}
	
	/**
	 * If multiple items are tied for the title of "earliest item" and one of them is the specified startAt node, remove nodes
	 * whose ordering within the result set is earlier than this node from the result list.  This method usually does nothing, and is mildly optimised
	 * to reduce the amount of work done on that basis
	 * @param workingResults Ordered list of results to process
	 * @param startAt Node to use as the start node if multiple nodes have the same earliest date.  Must not be null
	 * @return Modified copy of workingResults with all nodes tied with startNode for the earliest date which occur before startrNode in the 
	 * natural ordering of search results removed.  Note this will usually simply be == the input workingResults.
	 */
	protected List<SearchResult> resolveDateCollisionsWithStartAt(List<SearchResult> workingResults, NodeRef startAt)
	{
		
		Iterator<SearchResult> resultsItr = workingResults.iterator();
		
		//We assume results are ordered date ascending so minimum date = date of first item
		Date minDate = _gsaNodePropertyService.getModifiedDate(resultsItr.next().getNodeRef());
		
		//Reset the iterator
		resultsItr = workingResults.iterator();
		
		//Go through the results until we either find the startAt or we reach the end of nodes tied for the earliest date and set
		//the following boolean depending on whether we found startAt or not
		boolean isSpecifiedStartAtPresentAndWithMinDate=false;
		while (resultsItr.hasNext())
		{
			SearchResult result = resultsItr.next();

			if (_gsaNodePropertyService.getModifiedDate(result.getNodeRef()).after(minDate))
			{
				break;
			}
			if (result.getNodeRef().equals(startAt))
			{
				isSpecifiedStartAtPresentAndWithMinDate=true;
				break;
			}
		}
		
		
		//If startNode was present amongst the nodes tied from the minumum date...
		if (isSpecifiedStartAtPresentAndWithMinDate)
		{
			
			if (_logger.isDebugEnabled())
			{
				_logger.debug("The startAt node "+startAt+" was found tied for the minimum date");
			}
			
			//...then run through the results in order, removing nodes until we get to startNode
			resultsItr = workingResults.iterator();
			while (resultsItr.hasNext())
			{
				SearchResult sr = resultsItr.next();
				if (sr.getNodeRef().equals(startAt))
				{
					if (_logger.isDebugEnabled())
					{
						_logger.debug("Examining: "+sr.getNodeRef());
					}
					if (!resultsItr.hasNext())
					{
						return (List<SearchResult>)Collections.EMPTY_LIST;
					}
					else {
						resultsItr.remove();
						break;
					}
				}
				else {
					resultsItr.remove(); //This will remove the element from the underlying collection, not just the iterator
				}
			}
		}
		return workingResults;
	}
	
	/**
	 * Given a date, return the date as a String
	 */
	protected String DateToLuceneDateString(Date date)
	{
		SimpleDateFormat sdf = new SimpleDateFormat(LUCENE_DATE_FORMAT);
		sdf.setCalendar(Calendar.getInstance(GMT));
		return sdf.format(date);
	}
	
	//START SIMPLE VALIDATION METHODS
	
	private void checkSince(Date since) throws GSAInvalidParameterException
	{
		if (since==null)
		{
			throw new GSAInvalidParameterException("A start date was not specified", null, 1);
		}
		
		if (since.after(new Date()))
		{
			throw new GSAInvalidParameterException("The specified start date of "+since+" is in the future", null, 2);
		}
	}
	
	private void checkStartAt(NodeRef startAt) throws GSAInvalidParameterException
	{
		if (startAt==null)
		{
			throw new GSAInvalidParameterException("A start noderef was not specified", null, 3);
		}
	}
	
	private void checkMaxItems(int maxItems) throws GSAInvalidParameterException
	{
	
		if ((Integer)maxItems==null)
		{
			throw new GSAInvalidParameterException("A value for maxItems was not specified", null, 4);
		}
		
		if (maxItems<1)
		{
			throw new GSAInvalidParameterException("MaxItems must be at least 1 value was not specified", null, 5);
		}
	}
	
	//END SIMPLE VALIDATION METHODS

	@Override
	public void setSearchService(SearchService searchService) {
		_searchService=searchService;
	}

	@Override
	public void setGSANodePropertyService(GSANodePropertyService gsaNodePropertyService) {
		_gsaNodePropertyService=gsaNodePropertyService;
	}
}
