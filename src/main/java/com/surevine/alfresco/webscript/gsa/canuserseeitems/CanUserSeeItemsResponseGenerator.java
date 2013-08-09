package com.surevine.alfresco.webscript.gsa.canuserseeitems;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.alfresco.service.cmr.repository.NodeRef;
import org.xml.sax.SAXException;

import com.surevine.alfresco.webscript.gsa.exception.GSAInvalidParameterException;

public interface CanUserSeeItemsResponseGenerator
{
	public String getXMLResponse(Collection<NodeRef> itemsToQuery);
	public String getXMLResponse(Collection<NodeRef> itemsToQuery, String runAsUser);
	public Map<Integer,Object> requestToNodeRefCollection( String requestXMLString)  throws SAXException, ParserConfigurationException, IOException, GSAInvalidParameterException;
	public void setCanUserSeeItemsCommand( CanUserSeeItemsCommand canUserSeeItemsComamnd);
}
