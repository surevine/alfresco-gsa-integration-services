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
package com.surevine.alfresco.webscript.gsa.getallitems;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.alfresco.model.ContentModel;
import org.alfresco.repo.security.person.PersonServiceImpl;
import org.alfresco.service.cmr.repository.InvalidNodeRefException;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.cmr.search.ResultSet;
import org.alfresco.service.cmr.search.SearchParameters;
import org.alfresco.service.cmr.security.PersonService;
import org.alfresco.service.namespace.QName;
import org.junit.Before;
import org.junit.Test;

import com.surevine.alfresco.repo.profile.UserProfileModel;
import com.surevine.alfresco.webscript.gsa.exception.GSAInvalidParameterException;
import com.surevine.alfresco.webscript.gsa.getallitems.GSANodePropertyService;
import com.surevine.alfresco.webscript.gsa.getallitems.GSANodePropertyServiceImpl;
import com.surevine.alfresco.webscript.gsa.getallitems.GetAllItemsCommand;
import com.surevine.alfresco.webscript.gsa.getallitems.GetAllItemsCommandImpl;
import com.surevine.alfresco.webscript.gsa.getallitems.SearchResult;
import com.surevine.alfresco.webscript.gsa.getallitems.SearchResults;

public class GetAllItemsCommandImplTest {

	private GetAllItemsCommand _impl = new GetAllItemsCommandImpl();
	
	@Before 
	public void setup()
	{
		_impl.setSearchService(new SearchServiceMock());
		_impl.setGSANodePropertyService(new GSANodePropertyServiceMock());
	}
	
	@Test (expected = GSAInvalidParameterException.class)
	public void nullSince() throws GSAInvalidParameterException
	{
		_impl.getAllItems(null);
	}
	
	@Test (expected = GSAInvalidParameterException.class)
	public void futureSince() throws GSAInvalidParameterException
	{
		_impl.getAllItems(new Date(new Date().getTime()+1000*60*60));
	}
	
	@Test (expected = GSAInvalidParameterException.class)
	public void nullStartAt() throws GSAInvalidParameterException
	{
		_impl.getAllItems(new Date(new Date().getTime()-1000*60*60),(NodeRef)null);
	}
	
	@Test (expected = GSAInvalidParameterException.class)
	public void negativeMaxItems() throws GSAInvalidParameterException
	{
		_impl.getAllItems(new Date(new Date().getTime()-1000*60*60),-1);
	}
	
	@Test (expected = GSAInvalidParameterException.class)
	public void zeroMaxItems() throws GSAInvalidParameterException
	{
		_impl.getAllItems(new Date(new Date().getTime()-1000*60*60),0);
	}
	
	
	/**
	 * Assumes that mock search service returns > 10 results
	 */
	@Test
	public void limitNotExceeded() throws GSAInvalidParameterException
	{
		Collection<SearchResult> result = _impl.getAllItems(new Date(0l), 10).getResults();
		int size = result.size();
		assertTrue(size<11);
		assertTrue(size>0);
	}
	
	/**
	 * Assumes that the mocksearchservice will return exactly 1000 results (as it is configured to do)
	 * @throws GSAInvalidParameterException
	 */
	@Test
	public void isMoreSetCorrectlyWhenPaging() throws GSAInvalidParameterException
	{
		Date inputDate = new Date(0l);
		
		int resultsSoFar=0;
		//This should give us 1000 results, so we should count up all the results we get, get to exactly 1000 then 
		//see moreavailable hitting "stop"
		
		SearchResults results=null;
		while (resultsSoFar<1000)
		{
		   results = _impl.getAllItems(inputDate, 50);
		   int size = results.getResults().size();
		   assertTrue(size<51); //Quick double-check of the limit but not really the purpose of this test
		   assertTrue(size>0);
		   resultsSoFar+=size;
		   if (resultsSoFar<1000)
		   {
			   assertTrue(results.isMoreAvailable()); //We know there were 1000 results, so if we have 
			                                          //consumed less than 1000 then there must be more available
			                                          //and this should be indicated to us in the results object
		   }
		}
		
		//We've exited the while loop without error so we must have consumed 1000 results.  This means that isMoreAvailable
		//should be false, as there were 1000 results in the unpaged result set
		//assertFalse(results.isMoreAvailable());
	}
	
	
	/*
	 * Check that under a few different scenarios, the startAt logic works correctly
	 */
	@Test
	public void startAtToTieDateComparisons() throws GSAInvalidParameterException
	{
		//Use a special mock GSANPS that will cause every item in our result set to have the same date
		_impl.setGSANodePropertyService(new GSANodePropertyServiceSameDateMock());
		
		//Basic case - check that we can trim one value off the front of our results list
		NodeRef startAt = new NodeRef("test", "test", new Integer(0).toString());
		NodeRef nextNode = new NodeRef("test", "test", new Integer(1).toString());
		NodeRef firstResult = _impl.getAllItems(new Date(0l), startAt , 100).getResults().iterator().next().getNodeRef();
		assertEquals(nextNode, firstResult);
		
		//Corner case - the startAt node is already the first result in our list
		startAt = new NodeRef("test", "test", new Integer(1).toString());
		nextNode = new NodeRef("test", "test", new Integer(2).toString());
		firstResult = _impl.getAllItems(new Date(0l), startAt , 100).getResults().iterator().next().getNodeRef();
		assertEquals(nextNode, firstResult);
		
		//Double check of the standard case - can we trim more than one node from the front of our results list?
		startAt = new NodeRef("test", "test", new Integer(10).toString());
		nextNode = new NodeRef("test", "test", new Integer(11).toString());
		firstResult = _impl.getAllItems(new Date(0l), startAt , 100).getResults().iterator().next().getNodeRef();
		assertEquals(nextNode, firstResult);
	}
	
	@Test
	public void startAtOnlyItemInResults() throws GSAInvalidParameterException
	{
		_impl.setSearchService(new SearchServiceMock(1)
		{
			@Override
			public ResultSet query(SearchParameters p) {
				if (p.getQuery().contains("people"))
				{
					return new MockResultSet(0);
				}
				else
				{
					return new MockResultSet(_results);
				}
			}
		});
		_impl.setGSANodePropertyService(new GSANodePropertyServiceSameDateMock());

		NodeRef startAt = new NodeRef("test", "test", new Integer(0).toString());
		SearchResults results = _impl.getAllItems(new Date(0l), startAt , 100);
		assertTrue(results.getResults().size()==0);
	}
	
	@Test
	public void startAtSpecifiedButDatesNotEqual() throws GSAInvalidParameterException
	{
		//We're using the regular GSANPS mock here, so every item in our result set will have a different date
		
		//Set startAt to be the third node in our result list, so our startAt value should do nothing
		NodeRef startAt = new NodeRef("test", "test", new Integer(2).toString());
		SearchResult firstResult = _impl.getAllItems(new Date(0l), startAt , 100).getResults().iterator().next();
		assertFalse(startAt.equals(firstResult));
	}
	
	@Test
	public void checkAllFieldsInResultObjectSet() throws GSAInvalidParameterException
	{
		//Get a result set with 10 items in it and get the first item
		SearchResult result = _impl.getAllItems(new Date(0l), 100).getResults().iterator().next();
		
		//Get values our of the result object
		Date date = result.getLastModifiedDate();
		String title = result.getTitle();
		String url = result.getURL();
		String content = result.getContent();
		NodeRef nodeRef = result.getNodeRef();
		
		//Nothing should be null, and the strings should have some non-whitespace in
		assertNotNull(date);
		assertNotNull(title);
		assertNotNull(url);
		assertNotNull(content);
		assertNotNull(nodeRef);
		
		assertTrue(title.trim().length()>0);
		assertTrue(url.trim().length()>0);
		assertTrue(content.trim().length()>0);
	}
	
	@Test
	public void nullValuesForPropertiesFromRepo() throws GSAInvalidParameterException
	{
		//Get a result set with 10 items in it and get the first item
		SearchResult result = _impl.getAllItems(new Date(0l), 100).getResults().iterator().next();
		
		//Get values our of the result object
		result.getLastModifiedDate();
		result.getTitle();
		result.getURL();
		result.getContent();
		result.getNodeRef();
		
		//As long as we don't hit an NPE, the test has passed
	}
	
	@Test
	public void xmlContentEscaped() throws GSAInvalidParameterException
	{
		//Get a result set with 10 items in it and get the first item
		SearchResult result = _impl.getAllItems(new Date(0l), 10).getResults().iterator().next();
		
		String content = result.getContent();
		assertTrue(content.indexOf("Welcome to the sandbox")==-1);
	}
	
	@Test
	public void mimetypeSet() throws GSAInvalidParameterException
	{
		SearchResult result = _impl.getAllItems(new Date(0l), 10).getResults().iterator().next();
		assertTrue(result.getMimeType()!=null && result.getMimeType().trim().length()>1);
	}
	
	@Test
	public void nationalityCaveatsSplitCorrectly() throws GSAInvalidParameterException
	{
		//get a direct reference to the mock service, which we need in order to expose a protected method in the real service as public
		GSANodePropertyServiceMock propService = (GSANodePropertyServiceMock)(_impl.getGSANodePropertyService());
		
		//Mock results have natn caveat of "UK/SV/AB EYES ONLY"
		SearchResult result = _impl.getAllItems(new Date(0l), 10).getResults().iterator().next();
		NodeRef nr = result.getNodeRef();
		//Get the array of values from our mock
		String [] natns = propService.getNationalCaveats(nr);
				
		StringBuffer allNatns = new StringBuffer(10);
		
		//There should be three nationality caveats
		assertTrue(natns.length==3);
		
		//The nationality caveats should contain the right elements
		for (int i=0; i < natns.length; i++)
		{
			allNatns.append(natns[i]);
		}
		String all = allNatns.toString();
		assertTrue(all.indexOf("UK")!=-1);
		assertTrue(all.indexOf("SV")!=-1);
		assertTrue(all.indexOf("AB")!=-1);
	}
	
	@Test
	public void emptyNationalityCaveats() throws GSAInvalidParameterException
	{
		_impl.setGSANodePropertyService(new GSANodePropertyServiceMock(){
			protected String getNationalCaveatsString(NodeRef nodeRef)
			{
				return "  ";
			}
		});
		GSANodePropertyServiceMock propService = (GSANodePropertyServiceMock)(_impl.getGSANodePropertyService());
		SearchResult result = _impl.getAllItems(new Date(0l), 10).getResults().iterator().next();
		NodeRef nr = result.getNodeRef();
		//Get the array of values from our mock
		String [] natns = propService.getNationalCaveats(nr);
		
		//There shouldn't be any nationality caveats
		assertTrue(natns==null || natns.length==0);
	}
	
	@Test
	public void nullNationalityCaveats() throws GSAInvalidParameterException
	{
		_impl.setGSANodePropertyService(new GSANodePropertyServiceMock(){
			protected String getNationalCaveatsString(NodeRef nodeRef)
			{
				return null;
			}
		});
		GSANodePropertyServiceMock propService = (GSANodePropertyServiceMock)(_impl.getGSANodePropertyService());
		SearchResult result = _impl.getAllItems(new Date(0l), 10).getResults().iterator().next();
		NodeRef nr = result.getNodeRef();
		//Get the array of values from our mock
		String [] natns = propService.getNationalCaveats(nr);
		
		//There shouldn't be any nationality caveats
		assertTrue(natns==null || natns.length==0);
	}
	
	/**
	 * Test that when a profile is encountered, all of the expected values are set in the SearchResult object
	 * @throws GSAInvalidParameterException
	 */
	@Test
	public void profileBasicFlow() throws GSAInvalidParameterException
	{
		performProfileTest(new NodeServiceUserProfileMock());
	}
	
	/**
	 * Test success scenario.
	 * @throws GSAInvalidParameterException
	 */
	@Test
	public void modifiedByFirstNameAndLastName() throws Exception
	{
		final NodeRef personNodeRef = new NodeRef("workspace://SpacesStore/xxxx");
		
		final PersonService personService = mock(PersonService.class);
		when(personService.getPerson("jbloggs")).thenReturn(personNodeRef);
		
		final SearchResult result = performProfileTest(new NodeServiceUserProfileMock() {
			public Serializable getProperty(final NodeRef nodeRef, final QName qName) {
				if (nodeRef.equals(personNodeRef)) {
					if (qName.equals(ContentModel.PROP_FIRSTNAME)) {
						return "Joe";
					}
					
					if (qName.equals(ContentModel.PROP_LASTNAME)) {
						return "Bloggs";
					}
				}
				
				if (qName.equals(ContentModel.PROP_MODIFIER)){
					return "jbloggs";
				}
				
				return super.getProperty(nodeRef, qName);
			}
		}, personService, new GSANodePropertyServiceImpl());
		
		assertEquals("jbloggs (Joe Bloggs)", result.getModifiedBy());
	}
	
	/**
	 * Test "officer" as firstname scenario.
	 * @throws GSAInvalidParameterException
	 */
	@Test
	public void modifiedByOfficerAndLastName() throws Exception
	{
		final NodeRef personNodeRef = new NodeRef("workspace://SpacesStore/xxxx");
		
		final PersonService personService = mock(PersonService.class);
		when(personService.getPerson("jbloggs")).thenReturn(personNodeRef);
		
		final SearchResult result = performProfileTest(new NodeServiceUserProfileMock() {
			public Serializable getProperty(final NodeRef nodeRef, final QName qName) {
				if (nodeRef.equals(personNodeRef)) {
					if (qName.equals(ContentModel.PROP_FIRSTNAME)) {
						return "Officer";
					}
					
					if (qName.equals(ContentModel.PROP_LASTNAME)) {
						return "Bloggs";
					}
				}
				
				if (qName.equals(ContentModel.PROP_MODIFIER)){
					return "jbloggs";
				}
				
				return super.getProperty(nodeRef, qName);
			}
		}, personService, new GSANodePropertyServiceImpl());
		
		assertEquals("jbloggs (Bloggs)", result.getModifiedBy());
	}
	
	/**
	 * Test only usernames provided just returns a username.
	 * @throws GSAInvalidParameterException
	 */
	@Test
	public void modifiedByUsernameOnly() throws Exception
	{
		final NodeRef personNodeRef = new NodeRef("workspace://SpacesStore/xxxx");
		
		final PersonService personService = mock(PersonService.class);
		when(personService.getPerson("jbloggs")).thenReturn(personNodeRef);
		
		final SearchResult result = performProfileTest(new NodeServiceUserProfileMock() {
			public Serializable getProperty(final NodeRef nodeRef, final QName qName) {
				if (nodeRef.equals(personNodeRef)) {
					if (qName.equals(ContentModel.PROP_FIRSTNAME)) {
						return "jbloggs";
					}
					
					if (qName.equals(ContentModel.PROP_LASTNAME)) {
						return "jbloggs";
					}
				}
				
				if (qName.equals(ContentModel.PROP_MODIFIER)){
					return "jbloggs";
				}
				
				return super.getProperty(nodeRef, qName);
			}
		}, personService, new GSANodePropertyServiceImpl());
		
		assertEquals("jbloggs", result.getModifiedBy());
	}
	
	/**
	 * Test "officer" first name with empty last name scenario.
	 * @throws GSAInvalidParameterException
	 */
	@Test
	public void modifiedByOfficerAndEmpty() throws Exception
	{
		final NodeRef personNodeRef = new NodeRef("workspace://SpacesStore/xxxx");
		
		final PersonService personService = mock(PersonService.class);
		when(personService.getPerson("jbloggs")).thenReturn(personNodeRef);
		
		final SearchResult result = performProfileTest(new NodeServiceUserProfileMock() {
			public Serializable getProperty(final NodeRef nodeRef, final QName qName) {
				if (nodeRef.equals(personNodeRef)) {
					if (qName.equals(ContentModel.PROP_FIRSTNAME)) {
						return "Officer";
					}
					
					if (qName.equals(ContentModel.PROP_LASTNAME)) {
						return "";
					}
				}
				
				if (qName.equals(ContentModel.PROP_MODIFIER)){
					return "jbloggs";
				}
				
				return super.getProperty(nodeRef, qName);
			}
		}, personService, new GSANodePropertyServiceImpl());
		
		assertEquals("jbloggs", result.getModifiedBy());
	}
	
	/**
	 * Test "officer" first name with empty last name scenario.
	 * @throws GSAInvalidParameterException
	 */
	@Test
	public void modifiedByFirstnameAndEmpty() throws Exception
	{
		final NodeRef personNodeRef = new NodeRef("workspace://SpacesStore/xxxx");
		
		final PersonService personService = mock(PersonService.class);
		when(personService.getPerson("jbloggs")).thenReturn(personNodeRef);
		
		final SearchResult result = performProfileTest(new NodeServiceUserProfileMock() {
			public Serializable getProperty(final NodeRef nodeRef, final QName qName) {
				if (nodeRef.equals(personNodeRef)) {
					if (qName.equals(ContentModel.PROP_FIRSTNAME)) {
						return "Joe";
					}
					
					if (qName.equals(ContentModel.PROP_LASTNAME)) {
						return "";
					}
				}
				
				if (qName.equals(ContentModel.PROP_MODIFIER)){
					return "jbloggs";
				}
				
				return super.getProperty(nodeRef, qName);
			}
		}, personService, new GSANodePropertyServiceImpl());
		
		assertEquals("jbloggs (Joe)", result.getModifiedBy());
	}
	
	/**
	 * When we have a modifier by that person returns null.
	 * @throws GSAInvalidParameterException
	 */
	@Test
	public void modifiedByNullPerson() throws Exception
	{
		final NodeRef personNodeRef = new NodeRef("workspace://SpacesStore/xxxx");
		
		final PersonService personService = mock(PersonService.class);
		when(personService.getPerson("jbloggs")).thenReturn(personNodeRef);
		
		final SearchResult result = performProfileTest(new NodeServiceUserProfileMock() {
			public Serializable getProperty(final NodeRef nodeRef, final QName qName) {
				if (qName.equals(ContentModel.PROP_MODIFIER)){
					return null;
				}
				
				return super.getProperty(nodeRef, qName);
			}
		}, personService, new GSANodePropertyServiceImpl());
		
		assertEquals(null, result.getModifiedBy());
	}
	
	/**
	 * Test that the system can cope with a string value, in this case the profile, being both null
	 * and the empty String
	 * @throws GSAInvalidParameterException
	 */
	@Test
	public void profileNoBioSet() throws GSAInvalidParameterException
	{
		NodeService nullProfile = new NodeServiceUserProfileMock()
			{
				public Serializable getProperty(NodeRef nodeRef, QName qName) throws InvalidNodeRefException
				{
					if (qName.equals(UserProfileModel.PROP_BIOGRAPHY))
					{
						return null;
					}
					return super.getProperty(nodeRef, qName);
				}
			};
		
		NodeService emptyProfile = new NodeServiceUserProfileMock()
			{
				public Serializable getProperty(NodeRef nodeRef, QName qName) throws InvalidNodeRefException
				{
					if (qName.equals(UserProfileModel.PROP_BIOGRAPHY))
					{
						return "";
					}
					return super.getProperty(nodeRef, qName);
				}
			};
			
		performProfileTest(nullProfile);
		performProfileTest(emptyProfile);
		
	}
	
	@Test
	public void profileEmptyTelephoneList() throws GSAInvalidParameterException
	{
		NodeService nullProperty = new NodeServiceUserProfileMock()
		{
			public Serializable getProperty(NodeRef nodeRef, QName qName) throws InvalidNodeRefException
			{
				if (qName.equals(UserProfileModel.PROP_TELEPHONE_NUMBERS))
				{
					return null;
				}
				return super.getProperty(nodeRef, qName);
			}
		};
		
		NodeService emptyList = new NodeServiceUserProfileMock()
		{
			public Serializable getProperty(NodeRef nodeRef, QName qName) throws InvalidNodeRefException
			{
				if (qName.equals(UserProfileModel.PROP_TELEPHONE_NUMBERS))
				{
					return new ArrayList<String>(1);
				}
				return super.getProperty(nodeRef, qName);
			}
		};
		
		NodeService listWithNull = new NodeServiceUserProfileMock()
		{
			public Serializable getProperty(NodeRef nodeRef, QName qName) throws InvalidNodeRefException
			{
				if (qName.equals(UserProfileModel.PROP_TELEPHONE_NUMBERS))
				{
					ArrayList<Serializable> al = new ArrayList<Serializable>(1);
					al.add(null);
				}
				return super.getProperty(nodeRef, qName);
			}
		};
		
		NodeService listWithEmptyString = new NodeServiceUserProfileMock()
		{
			public Serializable getProperty(NodeRef nodeRef, QName qName) throws InvalidNodeRefException
			{
				if (qName.equals(UserProfileModel.PROP_TELEPHONE_NUMBERS))
				{
					ArrayList<Serializable> al = new ArrayList<Serializable>(1);
					al.add("");
				}
				return super.getProperty(nodeRef, qName);
			}
		};
		
		performProfileTest(nullProperty);
		performProfileTest(emptyList);
		performProfileTest(listWithNull);
		performProfileTest(listWithEmptyString);
	}
	
	protected SearchResult performProfileTest(NodeService nodeService) throws GSAInvalidParameterException
	{
		return performProfileTest(nodeService, new PersonServiceImpl(), 
				new GSANodePropertyServiceImpl() {
					public String getModifiedBy(NodeRef nodeRef) {
						return null;
					}
				});
	}
	
	protected SearchResult performProfileTest(final NodeService nodeService,
			final PersonService personService,
			final GSANodePropertyService nps) throws GSAInvalidParameterException {
		nps.setPersonService(personService);
		nps.setNodeService(nodeService);
		ArrayList<String> natns = new ArrayList<String>(1);
		natns.add("SV");
		nps.setProfileNatn(natns);
		nps.setProfileNod("SV");
		nps.setProfilePM("NATO SECRET");
		
		_impl.setGSANodePropertyService(nps);
		SearchResult result = _impl.getAllItems(new Date(0l), 10).getResults().iterator().next();
		
		//Check that nothing is null, everything has length and that the content String doesn't include
		//the word "null", which would be indicative of a problem somewhere else
		assertTrue(result.getTitle()!=null && result.getTitle().length()>1);
		assertTrue(result.getMimeType()!=null && result.getMimeType().length()>1);
		assertTrue(result.getSecurityLabel()!=null && result.getSecurityLabel().getProtectiveMarking().length()>1);
		assertTrue(result.getNodeRef()!=null);
		assertTrue(result.getLastModifiedDate()!=null);
		assertTrue(result.getURL()!=null && result.getURL().length()>1);
		assertTrue(result.getContent()!=null && result.getContent().length()>1);
		assertTrue(result.getContent().indexOf("null")==-1);
		
		if (result.getModifiedBy() != null) {
			assertTrue(result.getModifiedBy().indexOf("null") == -1);
		}
		
		return result;
	}
	
	public void setImplementation(GetAllItemsCommand impl)
	{
		_impl=impl;
	}
	
}
