package com.surevine.alfresco.webscript.gsa.getallitems;

import java.util.Collection;
import java.util.Date;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.search.SearchService;

import com.surevine.alfresco.webscript.gsa.exception.GSAInvalidParameterException;

/**
 * Interface describing the GetAllItems commands
 * @author simonw
 *
 */
public interface GetAllItemsCommand {
 
	/**
	 * Retrieve all the items in the repository.  Responses may be paged.
	 * @param since Only retrieve items modified since this date
	 * @return SearchResults objects encapsulating the results of this search
	 * @throws GSAInvalidParameterException If since if null or in the past
	 */
	public SearchResults getAllItems(Date since) throws GSAInvalidParameterException;
	
	/**
	 * Retrieve all the items in the repository.  Responses may be paged.
	 * @param since Only retrieve items modified since this date
	 * @param startAt If multiple items are tied for the "earliest" item within the search and one of them is this item,
	 * discard all the items that occur before this item according to some deterministic ordering
	 * @return SearchResults objects encapsulating the results of this search
	 * @throws GSAInvalidParameterException If since if null or in the past or if startAt is null
	 */
	public SearchResults getAllItems(Date since, NodeRef startAt) throws GSAInvalidParameterException;
	
	/**
	 * Retrieve all the items in the repository.  Responses may be paged.
	 * @param since Only retrieve items modified since this date
	 * @param maxItems A hint as to the number of items the caller wishes to retrieve.  The system must not return more
	 * than this number of items, but may return less.  If it does, this should be indicated in the return value
	 * @return SearchResults objects encapsulating the results of this search
	 * @throws GSAInvalidParameterException If since if null or in the past or if maxItems is less than one or is null
	 */
	public SearchResults getAllItems(Date since, int maxItems) throws GSAInvalidParameterException;
	
	/**
	 * Retrieve all the items in the repository.  Responses may be paged.
	 * @param since Only retrieve items modified since this date
	 * @param maxItems A hint as to the number of items the caller wishes to retrieve.  The system must not return more
	 * than this number of items, but may return less.  If it does, this should be indicated in the return value
	 * @param startAt If multiple items are tied for the "earliest" item within the search and one of them is this item,
	 * discard all the items that occur before this item according to some deterministic ordering
	 * @return SearchResults objects encapsulating the results of this search
	 * @throws GSAInvalidParameterException If since if null or in the past, if startAt is null or if maxItems is less than one or is null
	 */
	public SearchResults getAllItems(Date since, NodeRef startAt, int maxItems) throws GSAInvalidParameterException;
	
	/**
	 * Implementors of this interface are dependant upon a SearchService to retrieve items from the repo
	 * @param searchService
	 */
	public void setSearchService(SearchService searchService);
	
	/**
	 * Implementors of this interface are dependant upon a GSANodePropertyService to retrieve details of node in query results
	 * @param gsaNodePropertyService
	 */
	public void setGSANodePropertyService(GSANodePropertyService gsaNodePropertyService);
	
	public SearchService getSearchService();
	
	public GSANodePropertyService getGSANodePropertyService();
	
}
