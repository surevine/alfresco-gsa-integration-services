package com.surevine.alfresco.webscript.gsa.getallitems;

import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.SimpleTimeZone;

import org.alfresco.error.AlfrescoRuntimeException;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.search.SearchService;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.extensions.webscripts.AbstractWebScript;
import org.springframework.extensions.webscripts.WebScriptRequest;
import org.springframework.extensions.webscripts.WebScriptResponse;

import com.surevine.alfresco.webscript.gsa.exception.GSAInvalidParameterException;
import com.surevine.alfresco.webscript.gsa.exception.GSAProcessingException;

/**
 * Class to render business operations performed by GetAllItemsCommandImpl into an XML REST service via the webscript framework
 * @author simonw
 *
 */
public class GetAllItemsCommandWebscriptImpl extends AbstractWebScript implements GetAllItemsCommand {
	
	/**
	 * Delegate to this instance to perform the actual work
	 */
	private GetAllItemsCommand _implementation;
	
	private static final Log _logger = LogFactory.getLog(GetAllItemsCommandWebscriptImpl.class);
	
	private static final SimpleTimeZone GMT = new SimpleTimeZone(0, "GMT");
	
	private static final String ISO8601LocalFormat = "yyyy-MM-dd'T'HH:mm:ss:SSS";

	public SearchService getSearchService()
	{
		return _implementation.getSearchService();
	}
	
	public GSANodePropertyService getGSANodePropertyService()
	{
		return _implementation.getGSANodePropertyService();
	}
	
	@Override
	/**
	 * Webscript execute method, and the entry-point for this class under normal operation
	 */
	public void execute(WebScriptRequest request, WebScriptResponse response) throws IOException 
    {
		PrintStream ps=null;
		
		try 
		{
			ps = new PrintStream(response.getOutputStream());
			GetAllItemsParameters params = getParametersFromRequest(request);
			if (_logger.isInfoEnabled())
			{
				_logger.info("WebScript called with "+params);
			}
			SearchResults modelResults = executeCommand(params);
			String responseString = createXMLResponse(modelResults);
			
			if (_logger.isDebugEnabled())
			{
				_logger.debug("Response Length: "+responseString.length()); //We used to log the whole string but this was OOMing 
			}
			ps.println(responseString);
			
		}
		//If we catch an exception, return an appropriate HTTP response code and a summary of the exception, then dump the full
		//details of the exception to the logs.  Non-GSA Exceptions are wrapped, non-Exception Throwables are left to percolate upward
		catch (GSAInvalidParameterException e)
		{
			response.setStatus(400); //Something wrong with the parameters so we use Bad Request
			ps.println(e);
			_logger.error(e.getMessage(),e);
		}
		catch (GSAProcessingException exx)
		{
			response.setStatus(500); //Internal Server Error
			ps.println(exx);
			_logger.error(exx.getMessage(),exx);
		}
		catch (Exception ex)
		{
			response.setStatus(500);
			ps.println(new GSAProcessingException("Exception occured processing the commanmd: "+ex, ex, 1000));
			_logger.error(ex.getMessage(),ex);
		}
		finally
		{
			if (ps!=null)
			{
				ps.close();
			}
		}
	}
	
	/**
	 * Execute method that interrogates the parameters and delegates execution to the appropriate method of the
	 * injected implementation class.  Essentially, this transforms the parameters to avoid passing null values to
	 * the delegate
	 * @param params Parameters passed to the service, decoded from the webscript request
	 * @return SearchResults encapsulating the results of the service call in Objects
	 * @throws GSAInvalidParameterException
	 */
	private SearchResults executeCommand(GetAllItemsParameters params) throws GSAInvalidParameterException
	{
		if (params.getStartAt()!=null && params.getMaxItems()>0 && params.getMaxItems()<Integer.MAX_VALUE)
		{
			return getAllItems(params.getSince(), params.getStartAt(), params.getMaxItems());
		}
		if (params.getStartAt()!=null)
		{
			return getAllItems(params.getSince(), params.getStartAt());
		}
		if (params.getMaxItems()>0 && params.getMaxItems()<Integer.MAX_VALUE)
		{
			return getAllItems(params.getSince(), params.getMaxItems());
		}
		return getAllItems(params.getSince());
	}
	
	/**
	 * Render an object representation of the results of this service call into XML.  String manipulation is used
	 * ahead of a DOM-based approach for speed
	 * @param results Object rendition of the results of this service call
	 * @return String representing XML.  This XML is not validated by this service, although see the unit tests
	 */
	public String createXMLResponse(SearchResults results)
	{
		StringBuilder sb = new StringBuilder(5000);
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		sb.append("<sv:response xmlns:sv='http://surevine.com/alfresco/gsa/1.0' ");
		sb.append("moreAvailable='").append(results.isMoreAvailable()).append("'>");
		sb.append("<results>");
		
		Iterator<SearchResult> resultItr= results.getResults().iterator();
		while (resultItr.hasNext())
		{
			try 
			{
				sb.append(createXMLForSearchResult(resultItr.next()));
			}
			catch (GSAProcessingException e)
			{
				_logger.warn("Exception processing resultlist into XML", e);
			}
		}
		sb.append("</results></sv:response>");
		String rV = sb.toString();
		
		return rV;
	}
	
	public StringBuilder createXMLForSearchResult(SearchResult result)
	{
		//Format the date for XML output
		SimpleDateFormat sdf = new SimpleDateFormat(ISO8601LocalFormat);
		sdf.setCalendar(Calendar.getInstance(GMT));
		String formattedDate = sdf.format(result.getLastModifiedDate());
		
		//Write the results as XML
		StringBuilder sb = new StringBuilder(1000);
		sb.append("<result>");
		sb.append("<nodeRef>").append(StringEscapeUtils.escapeXml(result.getNodeRef().toString())).append("</nodeRef>");
		sb.append("<modifiedDateTime>").append(StringEscapeUtils.escapeXml(formattedDate)).append("</modifiedDateTime>");
		
		if (result.getDocumentType() != null) {
			sb.append("<documentType>").append(StringEscapeUtils.escapeXml(result.getDocumentType().toString())).append("</documentType>");
		}
		
		if (result.getModifiedBy() != null) {
			sb.append("<modifiedBy>").append(StringEscapeUtils.escapeXml(result.getModifiedBy())).append("</modifiedBy>");
		}
		
		sb.append("<title>").append(StringEscapeUtils.escapeXml(result.getTitle())).append("</title>");
		sb.append("<url>").append(StringEscapeUtils.escapeXml(result.getURL())).append("</url>");
		sb.append("<mimetype>").append(StringEscapeUtils.escapeXml(result.getMimeType())).append("</mimetype>");
		sb.append("<content>").append(result.getContent()).append("</content>"); //Base64 encoded so don't need to escape
		sb.append(createXMLForSecurityLabel(result.getSecurityLabel()));
		sb.append("</result>");
		return sb;
	}
	
	public StringBuilder createXMLForSecurityLabel(SecurityLabel label)
	{
		StringBuilder sb = new StringBuilder(200);
		sb.append("<securityLabel>");
		sb.append("<nationalOwnershipDesignator>").append(StringEscapeUtils.escapeXml(label.getNOD())).append("</nationalOwnershipDesignator>");
		sb.append("<protectiveMarking>").append(StringEscapeUtils.escapeXml(label.getProtectiveMarking())).append("</protectiveMarking>");  
		sb.append("<groups>");
		Iterator<SecurityMarking> markings = label.getMarkings();
		while (markings.hasNext())
		{
			SecurityMarking marking = markings.next();
			sb.append("<group type='");
			sb.append(marking.getType().getName());
			sb.append("'>").append(StringEscapeUtils.escapeXml(marking.getName())).append("</group>");
		}
		sb.append("</groups>");
		sb.append("<freeformMarking>").append(StringEscapeUtils.escapeXml(label.getFreeformCaveats())).append("</freeformMarking>");
		sb.append("<nationalityCaveats>");
		Iterator<String> ncs = label.getNationalityCaveats();
		while (ncs.hasNext())
		{
			String caveat=ncs.next().trim();
			if (caveat.matches("[A-Z]+")) 	//Some legacy items may have malformed national caveats as a result of a pre-existing XSS vuln that
											//was fixed some time ago - this conditional works around these legacy values by stripping-and-logging
											//dodgy-looking results
			{
				sb.append("<").append(caveat).append("/>");
			}
			else
			{
				_logger.warn("National Caveat ["+caveat+"] is invalid.  Stripping from results document and continuing");
			}
		}
		sb.append("</nationalityCaveats>").append("</securityLabel>");
		return sb;
	}
	
	/**
	 * Decodes parameters from the request into an object representation, performing validation as per the ICD
	 * @param request
	 * @return
	 * @throws GSAInvalidParameterException
	 */
	public GetAllItemsParameters getParametersFromRequest(WebScriptRequest request) throws GSAInvalidParameterException
	{
		
		//Since - note this is the only mandatory param
		GetAllItemsParameters params = new GetAllItemsParameters();
		try 
		{
			String sinceStr = request.getParameter("since");
			if (sinceStr!=null)
			{
				params.setSince(new Date(Long.parseLong(sinceStr.trim())));
			}
			else {
				throw new GSAInvalidParameterException("The since parameter is mandatory and was not set", null, 103);
			}
		}
		catch (NumberFormatException e)
		{
			throw new GSAInvalidParameterException("The since parameter should be a number.  Was: "+request.getParameter("since"), e, 100);
		}
		
		//maxItems aka limit
		try
		{
			String limitStr = request.getParameter("limit");
			if (limitStr!=null)
			{
				params.setMaxItems(Integer.parseInt(limitStr.trim()));
			}
		}
		catch (NumberFormatException e)
		{
			throw new GSAInvalidParameterException("The limit parameter should be a number.  Was: "+request.getParameter("limit"), e, 101);
		}
		if (params.getMaxItems()<1)
		{
			throw new GSAInvalidParameterException("The limit parameter must be at least 1.  Was: "+params.getMaxItems(), null, 102);
		}
		
		//StartAt		
		String startAtStr = request.getParameter("startAt");
		if (startAtStr!=null)
		{
			try 
			{
				params.setStartAt(new NodeRef (startAtStr.trim()));
			}
			catch (AlfrescoRuntimeException e)
			{
				throw new GSAInvalidParameterException("Could not create the noderef for: "+startAtStr.trim(), e, 104);
			}
		}
		return params;
	}

	@Override
	public SearchResults getAllItems(Date since) throws GSAInvalidParameterException {
		return _implementation.getAllItems(since);
	}

	@Override
	public SearchResults getAllItems(Date since, NodeRef startAt) throws GSAInvalidParameterException {
		return _implementation.getAllItems(since, startAt);
	}

	@Override
	public SearchResults getAllItems(Date since, int maxItems) throws GSAInvalidParameterException {
		return _implementation.getAllItems(since, maxItems);
	}

	@Override
	public SearchResults getAllItems(Date since, NodeRef startAt, int maxItems) throws GSAInvalidParameterException {
		return _implementation.getAllItems(since, startAt, maxItems);
	}
	
	public void setCommand(GetAllItemsCommand impl) {
		_implementation=impl;
	}

	@Override
	public void setSearchService(SearchService searchService) {
		_implementation.setSearchService(searchService);
	}

	@Override
	public void setGSANodePropertyService(GSANodePropertyService gsaNodePropertyService) {
		_implementation.setGSANodePropertyService(gsaNodePropertyService);
	}

}