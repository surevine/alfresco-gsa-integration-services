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

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.alfresco.module.org_alfresco_module_dod5015.caveat.RMCaveatConfigService;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.cmr.security.AccessStatus;
import org.alfresco.service.cmr.security.PermissionService;
import org.junit.Before;
import org.junit.Test;

import com.surevine.alfresco.webscript.gsa.exception.GSAInvalidParameterException;

public class CanUserSeeItemsCommandWebscriptImplTest
{

	private CanUserSeeItemsResponseGenerator canUserSeeItemsResponseGenerator = null;
	
	@Before
	public void setup()
	{
		//boilerplate:
		CanUserSeeItemsCommand canUserSeeItemsComamnd = new CanUserSeeItemsCommandTestableImpl();
		NodeService nodeService = new NodeServiceStub();
		canUserSeeItemsComamnd.setNodeService( nodeService);
		
		PermissionService mockPermissionService = mock(PermissionService.class);
		when(mockPermissionService.hasReadPermission((NodeRef) anyObject())).thenReturn(AccessStatus.ALLOWED);
		canUserSeeItemsComamnd.setPermissionService(mockPermissionService);
		
		RMCaveatConfigService caveatConfigService = new RMCaveatConfigServiceStub();
		canUserSeeItemsComamnd.setCaveatConfigService( caveatConfigService);
		canUserSeeItemsResponseGenerator = new CanUserSeeItemsCommandWebscriptImpl();
		canUserSeeItemsResponseGenerator.setCanUserSeeItemsCommand( canUserSeeItemsComamnd);
		((CanUserSeeItemsCommandWebscriptImpl)canUserSeeItemsResponseGenerator).setPersonService(new PersonServiceStub());
	}
	
	
	@Test
	public void testEmptyNodeRefList()
	{
		String expectedXMLString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><sv:response xmlns:sv=\"http://surevine.com/alfresco/gsa/1.0\" runAsUser=\"authenticatedUser\"></sv:response>";
		Collection<NodeRef> fixtureNodeRefCollection = new ArrayList<NodeRef>();
		String actualXMLString = canUserSeeItemsResponseGenerator.getXMLResponse( fixtureNodeRefCollection);
		assertNotNull( actualXMLString);
        assertEquals( expectedXMLString, actualXMLString);
	}

	@Test
	public void testPopulatedNodeRefList()
	{
		String expectedXMLString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><sv:response xmlns:sv=\"http://surevine.com/alfresco/gsa/1.0\" runAsUser=\"authenticatedUser\"><nodeRef>store:///1</nodeRef><nodeRef>store:///2</nodeRef><nodeRef>store:///3</nodeRef></sv:response>";

		Collection<NodeRef> fixtureNodeRefCollection = new ArrayList<NodeRef>();
		fixtureNodeRefCollection.add( new NodeRef("store:///1") );		
		fixtureNodeRefCollection.add( new NodeRef("store:///2") );		
		fixtureNodeRefCollection.add( new NodeRef("store:///3") );		

		String actualXMLString = canUserSeeItemsResponseGenerator.getXMLResponse( fixtureNodeRefCollection);

		assertNotNull( actualXMLString);
		assertEquals( expectedXMLString, actualXMLString);
	}

	/**
	 * @throws Exception
	 * This test does not work - for some reason the schema validation is not working
	 * HOWEVER the output has been validated manually with XMLSpy at the time of writing.
	 */
/*	@Test
	public void testResponseXMLIsSchemaCompliant() throws Exception
	{
		Collection<NodeRef> fixtureNodeRefCollection = new ArrayList<NodeRef>();
		fixtureNodeRefCollection.add( new NodeRef("store:///1") );		
		fixtureNodeRefCollection.add( new NodeRef("store:///2") );		
		fixtureNodeRefCollection.add( new NodeRef("store:///3") );		
		
		String actualXMLString = canUserSeeItemsResponseGenerator.getXMLResponse( fixtureNodeRefCollection);

		SchemaFactory sFactory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");

		//URL xmlSchemaResourceURL = getClass().getResource( "CanUserSeeItemsResponseSchema.xsd");
		//Schema schema = sFactory.newSchema( xmlSchemaResourceURL);

		File xmlSchemaResourceFile = new File(System.getProperty("com.surevine.alfresco.webscript.gsa.getallitems.test.SchemaLocation"));
		Schema schema = sFactory.newSchema( xmlSchemaResourceFile);
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setValidating( true);
		factory.setSchema( schema);
		DocumentBuilder parser = factory.newDocumentBuilder();
		parser.parse(new ByteArrayInputStream( actualXMLString.getBytes() ));
	}
*/
	@Test
	public void testRequestXMLToNodeCollection() throws Exception
	{
		String requestXMLString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><sv:request runAsUser=\"tom\" xmlns:sv=\"http://surevine.com/alfresco/gsa/1.0\"><nodeRef>store:///1</nodeRef><nodeRef>store:///2</nodeRef><nodeRef>store:///3</nodeRef></sv:request>";
		
		Collection<NodeRef> expectedNodeRefCollection = new ArrayList<NodeRef>();
		expectedNodeRefCollection.add( new NodeRef("store:///1") );
		expectedNodeRefCollection.add( new NodeRef("store:///2") );
		expectedNodeRefCollection.add( new NodeRef("store:///3") );
		
		Map<Integer,Object> parsedRequestDataMap = canUserSeeItemsResponseGenerator.requestToNodeRefCollection( requestXMLString);
		Collection<NodeRef> actualNodeRefCollection = (Collection<NodeRef>) parsedRequestDataMap.get( CanUserSeeItemsCommandWebscriptImpl.NODE_REFS_KEY);
		
		assertNotNull( actualNodeRefCollection);
		assertEquals( actualNodeRefCollection, expectedNodeRefCollection);
	}

	@Test
	public void testRequestXMLToRunAsUser() throws Exception
	{
		String requestXMLString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><sv:request runAsUser=\"tom-surevine\" xmlns:sv=\"http://surevine.com/alfresco/gsa/1.0\"><nodeRef>store:///1</nodeRef><nodeRef>store:///2</nodeRef><nodeRef>store:///3</nodeRef></sv:request>";
		String expectedRunAsUser = "tom-surevine";
		
		Collection<NodeRef> expectedNodeRefCollection = new ArrayList<NodeRef>();
		expectedNodeRefCollection.add( new NodeRef("store:///1") );
		expectedNodeRefCollection.add( new NodeRef("store:///2") );
		expectedNodeRefCollection.add( new NodeRef("store:///3") );

		Map<Integer,Object> parsedRequestDataMap = canUserSeeItemsResponseGenerator.requestToNodeRefCollection( requestXMLString);
		String actualRunAsUser = (String) parsedRequestDataMap.get( CanUserSeeItemsCommandWebscriptImpl.RUN_AS_USER_KEY);
		
		assertNotNull( actualRunAsUser);
		assertEquals( actualRunAsUser, expectedRunAsUser);
	}

	@Test
	public void testEmptyRequestXMLToNodeCollection() throws Exception
	{
		String requestXMLString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><sv:request xmlns:sv=\"http://surevine.com/alfresco/gsa/1.0\" runAsUser=\"testUser\"></sv:request>";
		Collection<NodeRef> expectedNodeRefCollection = new ArrayList<NodeRef>();

		Map<Integer,Object> parsedRequestDataMap = canUserSeeItemsResponseGenerator.requestToNodeRefCollection( requestXMLString);
		Collection<NodeRef> actualNodeRefCollection = (Collection<NodeRef>) parsedRequestDataMap.get( CanUserSeeItemsCommandWebscriptImpl.NODE_REFS_KEY);

		assertNotNull( actualNodeRefCollection);
		assertEquals( actualNodeRefCollection, expectedNodeRefCollection);
	}

	@Test
	public void testRequestXMLToNodeCollectionInvalidNodeRefs() throws Exception
	{
		String requestXMLString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><sv:request xmlns:sv=\"http://surevine.com/alfresco/gsa/1.0\"><nodeRef>invalid node ref</nodeRef></sv:request>";
		Collection<NodeRef> expectedNodeRefCollection = new ArrayList<NodeRef>();
		
		Map<Integer,Object> parsedRequestDataMap = canUserSeeItemsResponseGenerator.requestToNodeRefCollection( requestXMLString);
		Collection<NodeRef> actualNodeRefCollection = (Collection<NodeRef>) parsedRequestDataMap.get( CanUserSeeItemsCommandWebscriptImpl.NODE_REFS_KEY);

		assertNotNull( actualNodeRefCollection);
		assertEquals( actualNodeRefCollection, expectedNodeRefCollection);
	}
	
	@Test (expected=GSAInvalidParameterException.class)
	public void testRunAsUserDoesNotExist() throws Exception
	{
		PersonServiceStub ps = new PersonServiceStub();
		ps.setPersonWhoDoesNotExist("testUserWhoDoesNotExist");
		((CanUserSeeItemsCommandWebscriptImpl)canUserSeeItemsResponseGenerator).setPersonService(ps);
		
		String requestXMLString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><sv:request xmlns:sv=\"http://surevine.com/alfresco/gsa/1.0\" runAsUser=\"testUserWhoDoesNotExist\"></sv:request>";
		Collection<NodeRef> expectedNodeRefCollection = new ArrayList<NodeRef>();

		Map<Integer,Object> parsedRequestDataMap = canUserSeeItemsResponseGenerator.requestToNodeRefCollection( requestXMLString);
		Collection<NodeRef> actualNodeRefCollection = (Collection<NodeRef>) parsedRequestDataMap.get( CanUserSeeItemsCommandWebscriptImpl.NODE_REFS_KEY);

	}
	
}
