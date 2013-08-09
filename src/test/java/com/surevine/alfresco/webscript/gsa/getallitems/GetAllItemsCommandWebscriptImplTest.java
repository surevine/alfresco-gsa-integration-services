package com.surevine.alfresco.webscript.gsa.getallitems;

import static junit.framework.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.alfresco.service.cmr.repository.NodeRef;
import org.junit.Test;
import org.xml.sax.SAXException;

import com.surevine.alfresco.webscript.gsa.exception.GSAInvalidParameterException;

public class GetAllItemsCommandWebscriptImplTest {
	
	public static SearchResults getTestSearchResults(int size, boolean moreAvailable)
	{
		SearchResults res = new SearchResultsImpl();
		
		List<SearchResult> results = new ArrayList<SearchResult>(size);
		for (int i=0; i<size; i++)
		{
			results.add(getTestSearchResult("Result number "+i, new Integer(i).toString(), new Date(), "Title "+i, "http://test.result."+i, getTestSecurityLabel()));
		}
		res.setResults(results);
		res.moreAvailable(moreAvailable);
		return res;
	}
	
	public static SecurityLabel getTestSecurityLabel()
	{
		SecurityLabel label = new SecurityLabel();
		label.setNOD("SV");
		label.setProtectiveMarking("NOT PROTECTIVELY MARKED");
		label.setFreeformCaveats("TESTTESTTEST");
		
		SecurityMarking og1 = new SecurityMarking();
		og1.setName("OG1");
		og1.setType(SecurityMarkingType.OPEN);
		
		SecurityMarking cg1 = new SecurityMarking();
		cg1.setName("CG1");
		cg1.setType(SecurityMarkingType.CLOSED);
		
		SecurityMarking org1 = new SecurityMarking();
		org1.setName("ORG1");
		org1.setType(SecurityMarkingType.ORGANISATION);
		
		label.addMarking(og1);
		label.addMarking(cg1);
		label.addMarking(org1);
		
		label.addNationalityCaveat("UK");
		label.addNationalityCaveat("SV");
		return label;
	}
	
	public static SearchResult getTestSearchResult(String content, String nrID, Date modified, String title, String url, SecurityLabel label)
	{
		SearchResult result = new SearchResultImpl();
		result.setContent(content);
		result.setNodeRef(new NodeRef("workspace://SpacesStore://123-456-"+nrID));
		result.setLastModifiedDate(modified);
		result.setTitle(title);
		result.setURL(url);
		result.setSecurityLabel(label);
		return result;
	}
	
	@Test
	public void securityMarkingRendition()
	{
		SecurityLabel label = getTestSecurityLabel();
		String result = new GetAllItemsCommandWebscriptImpl().createXMLForSecurityLabel(label).toString();
		assertTrue(result.equalsIgnoreCase("<securityLabel><nationalOwnershipDesignator>SV</nationalOwnershipDesignator><protectiveMarking>NOT PROTECTIVELY MARKED</protectiveMarking><groups><group type='open'>OG1</group><group type='closed'>CG1</group><group type='organisation'>ORG1</group></groups><freeformMarking>TESTTESTTEST</freeformMarking><nationalityCaveats><UK/><SV/></nationalityCaveats></securityLabel>"));
	}
	
	@Test
	public void sinceParsed() throws GSAInvalidParameterException
	{
		WebScriptRequestMock req = new WebScriptRequestMock(null, "123456789", null);
		GetAllItemsParameters params = new GetAllItemsCommandWebscriptImpl().getParametersFromRequest(req);
		assertTrue(params.getSince().getTime()==123456789l);
	}
	
	@Test
	public void startAtParsed() throws GSAInvalidParameterException
	{
		NodeRef startAt = new NodeRef("workspace://SpacesStore://123-456-7890");
		WebScriptRequestMock req = new WebScriptRequestMock("workspace://SpacesStore://123-456-7890", "123456789", null);
		GetAllItemsParameters params = new GetAllItemsCommandWebscriptImpl().getParametersFromRequest(req);
		assertTrue(params.getStartAt().equals(startAt));
	}
	
	@Test
	public void limitParsed() throws GSAInvalidParameterException
	{
		WebScriptRequestMock req = new WebScriptRequestMock(null, "123456789", "42");
		GetAllItemsParameters params = new GetAllItemsCommandWebscriptImpl().getParametersFromRequest(req);
		assertTrue(params.getMaxItems()==42);
	}
	
	@Test (expected = GSAInvalidParameterException.class)
	public void negativeLimit() throws GSAInvalidParameterException
	{
		WebScriptRequestMock req = new WebScriptRequestMock(null, "123456789", "-1");
		GetAllItemsParameters params = new GetAllItemsCommandWebscriptImpl().getParametersFromRequest(req);
	}
	
	@Test (expected = GSAInvalidParameterException.class)
	public void badStartAt() throws GSAInvalidParameterException
	{
		WebScriptRequestMock req = new WebScriptRequestMock("I'm Not a valid value!", "123456789", null);
		GetAllItemsParameters params = new GetAllItemsCommandWebscriptImpl().getParametersFromRequest(req);
	}
	
	@Test (expected = GSAInvalidParameterException.class)
	public void badSince() throws GSAInvalidParameterException
	{
		WebScriptRequestMock req = new WebScriptRequestMock(null, "I'm Not a valid value!", null);
		GetAllItemsParameters params = new GetAllItemsCommandWebscriptImpl().getParametersFromRequest(req);
	}
	
	@Test (expected = GSAInvalidParameterException.class)
	public void badLimit() throws GSAInvalidParameterException
	{
		WebScriptRequestMock req = new WebScriptRequestMock(null, "123456789", "I'm Not a valid value!");
		GetAllItemsParameters params = new GetAllItemsCommandWebscriptImpl().getParametersFromRequest(req);
	}
	
	@Test (expected = GSAInvalidParameterException.class)
	public void nullSince() throws GSAInvalidParameterException
	{
		WebScriptRequestMock req = new WebScriptRequestMock("workspace://SpacesStore://123-456-7890", null, "50");
		GetAllItemsParameters params = new GetAllItemsCommandWebscriptImpl().getParametersFromRequest(req);
	}
	/*
	@Test
	public void responseMatchesSchema() throws SAXException, ParserConfigurationException, IOException
	{
		
		System.out.println("Using schema at"+System.getProperty("com.surevine.alfresco.webscript.gsa.getallitems.test.SchemaLocation"));
		SearchResults results = getTestSearchResults(100, false);
		String xmlResults = new GetAllItemsCommandWebscriptImpl().createXMLResponse(results);
	
		SchemaFactory sFactory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
		File schemaFile = new File(System.getProperty("com.surevine.alfresco.webscript.gsa.getallitems.test.SchemaLocation"));
		Schema schema = sFactory.newSchema(schemaFile);
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setValidating(true);
		factory.setSchema(schema);
		DocumentBuilder parser = factory.newDocumentBuilder();
		parser.parse(new ByteArrayInputStream(xmlResults.getBytes()));
	}*/
	
	@Test
	public void responseHasCorrectNumberOfResults() throws SAXException, ParserConfigurationException, IOException
	{
		SearchResults results = getTestSearchResults(100, false);
		String xmlResults = new GetAllItemsCommandWebscriptImpl().createXMLResponse(results);
		
		int resultsCount=0;
		while (xmlResults.indexOf("<result>")!=-1)
		{
			xmlResults = xmlResults.substring(xmlResults.indexOf("<result>")+8);
			resultsCount++;
		}
		assertTrue(resultsCount==100);
	}
	
	@Test
	public void resultsHaveMoreAvailableSetCorrectly() throws SAXException, ParserConfigurationException, IOException
	{
		SearchResults results = getTestSearchResults(10, true);
		String xmlResults = new GetAllItemsCommandWebscriptImpl().createXMLResponse(results);
		assertTrue(xmlResults.matches(".*moreAvailable.?=.?.true.*"));
		results = getTestSearchResults(10, false);
		xmlResults = new GetAllItemsCommandWebscriptImpl().createXMLResponse(results);
		System.out.println(xmlResults);
		assertTrue(xmlResults.matches(".*moreAvailable.?=.?.false.*"));
	}
	
	@Test
	public void multipleNationalitiesSeperatedCorrectly()
	{
		SearchResults results = getTestSearchResults(1, false);
		SearchResult result = results.getResults().iterator().next();
		assertTrue(new GetAllItemsCommandWebscriptImpl().createXMLForSecurityLabel(result.getSecurityLabel()).indexOf("<UK/><SV/>")!=-1);
	}

}