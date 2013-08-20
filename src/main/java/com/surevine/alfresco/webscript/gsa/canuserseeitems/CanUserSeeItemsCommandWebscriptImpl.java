/*
 * Copyright (C) 2008-2010 Surevine Limited.
 *   
 * Although intended for deployment and use alongside Alfresco this module should
 * be considered 'Not a Contribution' as defined in Alfresco'sstandard contribution agreement, see
 * http://www.alfresco.org/resource/AlfrescoContributionAgreementv2.pdf
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
*/
package com.surevine.alfresco.webscript.gsa.canuserseeitems;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.StringWriter;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.security.PersonService;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.extensions.webscripts.AbstractWebScript;
import org.springframework.extensions.webscripts.WebScriptRequest;
import org.springframework.extensions.webscripts.WebScriptResponse;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.surevine.alfresco.webscript.gsa.exception.GSAInvalidParameterException;
import com.surevine.alfresco.webscript.gsa.exception.GSAProcessingException;

public class CanUserSeeItemsCommandWebscriptImpl extends AbstractWebScript implements CanUserSeeItemsResponseGenerator
{

	private CanUserSeeItemsCommand canUserSeeItemsCommand = null;
	private PersonService personService = null;
	
	//make these an enumeration:
	public static Integer RUN_AS_USER_KEY = 1;
	public static Integer NODE_REFS_KEY = 2;

	private static final Log _logger = LogFactory.getLog(CanUserSeeItemsCommandWebscriptImpl.class);
	
	public void setPersonService(PersonService personService)
	{
		this.personService=personService;
	}
	
	@Override
	public void execute(WebScriptRequest request, WebScriptResponse response)
			throws IOException
	{
		if (_logger.isDebugEnabled())
		{
			_logger.debug("Beginning canUserSeeItems Webscript");
		}
		PrintStream ps=null;
		try {

			ps = new PrintStream( response.getOutputStream());
			
			InputStream payloadInputStream = request.getContent().getInputStream();
			StringWriter writer = new StringWriter();
			IOUtils.copy(payloadInputStream, writer, "UTF-8");
			String requestXMLString = writer.toString();
			requestXMLString = URLDecoder.decode( requestXMLString, "UTF-8");
			if (_logger.isDebugEnabled())
			{
				_logger.debug("Request: "+requestXMLString);
			}
			
			//String requestXMLString = request.getParameter("");
			if( null == requestXMLString) {
				throw new GSAInvalidParameterException("Request payload XML is empty", null, 500177);
			}

			Map<Integer,Object> parsedRequestDataMap = this.requestToNodeRefCollection( requestXMLString);

			Collection<NodeRef> nodeRefs = (Collection<NodeRef>) parsedRequestDataMap.get( NODE_REFS_KEY);
			String runAsUser = (String) parsedRequestDataMap.get( RUN_AS_USER_KEY);

			String XMLResponseString = this.getXMLResponse( nodeRefs, runAsUser);
			if (_logger.isDebugEnabled())
			{
				_logger.debug("Response: "+XMLResponseString);
			}
			ps.println( XMLResponseString); //Write the response to the target OutputStream

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

	public Map<Integer,Object> requestToNodeRefCollection( String requestXMLString)  throws SAXException, ParserConfigurationException, IOException, GSAInvalidParameterException
	{
		Map<Integer,Object> responseItems = new HashMap<Integer,Object>();
		
		Collection<NodeRef> nodeRefs = new ArrayList<NodeRef>();		

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setValidating( false);
		DocumentBuilder parser = factory.newDocumentBuilder();
		Document document = parser.parse( new ByteArrayInputStream( requestXMLString.getBytes("UTF-8") ) );

		NodeList allNodeRefs = document.getElementsByTagName("nodeRef");
		Element documentElement = document.getDocumentElement();
		String runAsUser = documentElement.getAttribute("runAsUser");
		
		//If the runAsUser is specified but doesn't exist, throw an error
		if (runAsUser!=null && (!runAsUser.equals("")) && !personService.personExists(runAsUser))
		{
			throw new GSAInvalidParameterException("The specified runAsUser("+runAsUser+") does not exist", null, 140611);
		}
		responseItems.put( RUN_AS_USER_KEY, runAsUser);
		
		int nodeRefCount = allNodeRefs.getLength();
		for(int i = 0; i < nodeRefCount; i++) {
			Node node = allNodeRefs.item( i);
			String nodeReference = node.getTextContent();

			NodeRef newNode = null;
			try {
				newNode = new NodeRef( nodeReference);
				if (_logger.isDebugEnabled())
				{
					_logger.debug("Created nodeRef "+newNode+" from request");
				}
			} 
			catch (Exception e) 
			{
				//some problem creating the NodeRef - probably a syntactically invalid node ref.
				//ignore and continue:
				GSAProcessingException wrapped = new GSAProcessingException("Could not serialise "+nodeReference+" to a NodeRef Collection", e, 34831);
				_logger.warn("NodeRef could not be instantiated for supplied node reference [" + nodeReference + "].", wrapped);
				continue;
			}

			nodeRefs.add( newNode);
		}
		
		responseItems.put( NODE_REFS_KEY, nodeRefs);
		
		return responseItems;
	}
	
	
	public CanUserSeeItemsResponse canUserSeeItems(Collection<NodeRef> itemsToQuery, String runAsUser)
	{
		return canUserSeeItemsCommand.canUserSeeItems( itemsToQuery, runAsUser);
	}
	
	@Override
	public String getXMLResponse(Collection<NodeRef> itemsToQuery)
	{
		return this.getXMLResponse( itemsToQuery, null);
	}
	
	@Override
	public String getXMLResponse(Collection<NodeRef> itemsToQuery, String runAsUser)
	{
		StringBuffer responseBuffer = new StringBuffer();
		
		CanUserSeeItemsResponse response = this.canUserSeeItems( itemsToQuery, runAsUser);
		Collection<NodeRef> responseItems = response.getResponseNodes();

		responseBuffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		responseBuffer.append("<sv:response xmlns:sv=\"http://surevine.com/alfresco/gsa/1.0\" runAsUser=\"" + response.getRunAsUser() + "\">");

		for(Iterator<NodeRef> iterator = responseItems.iterator(); iterator.hasNext();) 
		{
			NodeRef nodeRef = iterator.next();
			responseBuffer.append("<nodeRef>" + nodeRef.toString() + "</nodeRef>");
		}
		responseBuffer.append("</sv:response>");
		
		return responseBuffer.toString();
	}

	
	public void setCanUserSeeItemsCommand( CanUserSeeItemsCommand canUserSeeItemsCommand)
	{
		this.canUserSeeItemsCommand = canUserSeeItemsCommand;
	}

}
