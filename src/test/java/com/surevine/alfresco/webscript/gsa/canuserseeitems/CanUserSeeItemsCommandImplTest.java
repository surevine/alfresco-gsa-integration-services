package com.surevine.alfresco.webscript.gsa.canuserseeitems;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import org.alfresco.module.org_alfresco_module_dod5015.caveat.RMCaveatConfigService;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.cmr.security.AccessStatus;
import org.alfresco.service.cmr.security.PermissionService;
import org.junit.Before;
import org.junit.Test;

import com.surevine.alfresco.webscript.gsa.canuserseeitems.CanUserSeeItemsResponse;

public class CanUserSeeItemsCommandImplTest {

	private NodeService nodeService = new NodeServiceStub();
	private RMCaveatConfigService caveatConfigService = new RMCaveatConfigServiceStub();
	private CanUserSeeItemsCommandTestableImpl canUserSeeItemsComamnd = null;
	private AuthorityServiceStub authorityService;

	private NodeRef[] ALLOWED_FIXTURE_ARRAY = { new NodeRef("store:///1"), new NodeRef("store:///2"), new NodeRef("store:///3") };
	
	// This will be implemented by the NodeService
	private NodeRef[] UNAUTHORISED_FIXTURE_ARRAY = { new NodeRef("store:///1_not-authorized"), new NodeRef("store:///2_not-authorized"), new NodeRef("store:///3_not-authorized") };
	
	// This will be implemented by the NodeService
	private NodeRef[] NONEXISTANT_FIXTURE_ARRAY = { new NodeRef("store:///1_non-existant"), new NodeRef("store:///2_non-existant"), new NodeRef("store:///3_non-existant") };
	
	// This will be implemented by the Permission Service
	private NodeRef[] NOT_PERMITTED_FIXTURE_ARRAY = {new NodeRef("store:///1_not-permitted"), new NodeRef("store:///2_not-permitted"), new NodeRef("store:///3_not-permitted")};

	private PermissionService mockPermissionService;

	@Before
	public void setup() {
		canUserSeeItemsComamnd = new CanUserSeeItemsCommandTestableImpl();
		canUserSeeItemsComamnd.setNodeService(nodeService);
		canUserSeeItemsComamnd.setCaveatConfigService(caveatConfigService);

		authorityService = new AuthorityServiceStub();
		canUserSeeItemsComamnd.setAuthorityService(authorityService);

		mockPermissionService = mock(PermissionService.class);
		
		// Now setup the mock permission Service to return ALLOWED on those that will be governed by the NodeService.
		for (int i = 0; i < ALLOWED_FIXTURE_ARRAY.length; i++) {
			when(mockPermissionService.hasReadPermission(ALLOWED_FIXTURE_ARRAY[i])).thenReturn(AccessStatus.ALLOWED);
			when(mockPermissionService.hasReadPermission(UNAUTHORISED_FIXTURE_ARRAY[i])).thenReturn(AccessStatus.ALLOWED);
		}
		
		for (int i = 0; i < NOT_PERMITTED_FIXTURE_ARRAY.length; i++) {
			when(mockPermissionService.hasReadPermission(NOT_PERMITTED_FIXTURE_ARRAY[i])).thenReturn(AccessStatus.DENIED);
		}
		
		// Default to a permissionService which returns ALLOWED, 
		canUserSeeItemsComamnd.setPermissionService(mockPermissionService);
	}

	@Test
	public void testForIllegalArgumentException() {
		try {
			canUserSeeItemsComamnd.canUserSeeItems(null);
			fail("illegal argument not thrown!");
		} catch (IllegalArgumentException success) {
			assertNotNull(success.getMessage());
		}
	}

	/**
	 * this test exercises the capability to run the canUserSeeItems as a
	 * different user. In this case, we are simulating that the CAS
	 * authenticated user has 'admin' rights and can indeed run the method as a
	 * different user.
	 * 
	 * Please note, this is unit test and thus does not have access to the
	 * Alfresco runtime environment, therefore we do not aim to test the ability
	 * for Alfresco to do 'stuff' as another user, but simply that the mechanics
	 * of the code behave as expected.
	 */
	@Test
	public void testRunAsUserApproved() {
		// this will allow the service to run as the supplied 'runUser'.
		authorityService.setSessionUserIsAdmin(true);

		String runUser = "testUserA";

		Collection<NodeRef> fixtureNodeRefCollection = Arrays.asList(ALLOWED_FIXTURE_ARRAY);

		canUserSeeItemsComamnd.setExpectedTestReturnedNodeList(fixtureNodeRefCollection);

		CanUserSeeItemsResponse response = canUserSeeItemsComamnd.canUserSeeItems(fixtureNodeRefCollection, runUser);
		Collection<NodeRef> actualResultCollection = response.getResponseNodes();
		assertEquals(response.getRunAsUser(), runUser);
		assertNotNull(actualResultCollection);
		assertEquals(fixtureNodeRefCollection, actualResultCollection);
	}

	@Test
	public void testEmptyListArgumentException() {
		Collection<NodeRef> fixtureNodeRefCollection = new ArrayList<NodeRef>();
		Collection<NodeRef> actualResultCollection = canUserSeeItemsComamnd.canUserSeeItems(fixtureNodeRefCollection);
		assertNotNull(actualResultCollection);
		assertEquals(Collections.emptyList(), actualResultCollection);
	}

	@Test
	public void testCanSeeAllItems() {

		Collection<NodeRef> actualResultCollection = canUserSeeItemsComamnd.canUserSeeItems(Arrays.asList(ALLOWED_FIXTURE_ARRAY));

		// no NodeRef items should have been changed, removed or added.
		assertNotNull(actualResultCollection);
		assertEquals(Arrays.asList(ALLOWED_FIXTURE_ARRAY), actualResultCollection);
	}

	@Test
	public void testNotAuthorizedItemsRemoved() {
		Collection<NodeRef> actualResultCollection = canUserSeeItemsComamnd.canUserSeeItems(Arrays.asList(UNAUTHORISED_FIXTURE_ARRAY));

		// all un-authorized NodeRef items should have been removed, thus
		// resulting in an empty list
		assertEquals(Collections.emptyList(), actualResultCollection);
	}

	@Test
	public void testNonExistingItemsRemoved() {

		Collection<NodeRef> actualResultCollection = canUserSeeItemsComamnd.canUserSeeItems(Arrays.asList(NONEXISTANT_FIXTURE_ARRAY));

		// all non-existant NodeRef items should have been removed, thus
		// resulting in an empty list:
		assertNotNull(actualResultCollection);
		assertEquals(Collections.emptyList(), actualResultCollection);
	}

	@Test
	public void testMixOfItems() {
		NodeRef[] fixtureArray = { ALLOWED_FIXTURE_ARRAY[0], NONEXISTANT_FIXTURE_ARRAY[1], ALLOWED_FIXTURE_ARRAY[2], 
			UNAUTHORISED_FIXTURE_ARRAY[2]};

		NodeRef[] expectedArray = { ALLOWED_FIXTURE_ARRAY[0],  ALLOWED_FIXTURE_ARRAY[2]};

		Collection<NodeRef> actualResultCollection = canUserSeeItemsComamnd.canUserSeeItems(Arrays.asList(fixtureArray));

		assertNotNull(actualResultCollection);
		assertEquals(Arrays.asList(expectedArray), actualResultCollection);
	}
	
	/**
	 * In this test the permission service will be used to govern access is correctly allowed/denied.
	 */
	@Test
	public void testPermissionServiceDeniesAccessCorrectly() {
		NodeRef[] fixtureArray = {ALLOWED_FIXTURE_ARRAY[0], NOT_PERMITTED_FIXTURE_ARRAY[0]};
		NodeRef[] expectedArray = {ALLOWED_FIXTURE_ARRAY[0]};
		
		Collection<NodeRef> actualResult = canUserSeeItemsComamnd.canUserSeeItems(Arrays.asList(fixtureArray));
		
		assertEquals(Arrays.asList(expectedArray), actualResult);
 	}

}
