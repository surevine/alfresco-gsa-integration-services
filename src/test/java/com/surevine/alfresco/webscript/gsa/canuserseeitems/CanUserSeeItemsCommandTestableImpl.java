package com.surevine.alfresco.webscript.gsa.canuserseeitems;

import java.util.Collection;

import org.alfresco.service.cmr.repository.NodeRef;

import com.surevine.alfresco.webscript.gsa.canuserseeitems.CanUserSeeItemsCommandImpl;
import com.surevine.alfresco.webscript.gsa.canuserseeitems.DeferredUserCanUserSeeItems;


/**
 * this class is a bit horrible! it was created so that we can override the 'runAs' method and 
 *	remove the dependency on the AuthenticationUtil class for the purposes of unit tests.
 */
public class CanUserSeeItemsCommandTestableImpl extends
		CanUserSeeItemsCommandImpl {

	private Collection<NodeRef> nodeRefItems = null;
	
	//test setup method - call this BEFORE runAs is called to set the nodeRefItems to return.
	//...it may all seem complicated, but enables us to exercise the class under test
	//without having to significantly change our production code to be testable. 
	public void setExpectedTestReturnedNodeList( Collection<NodeRef> nodeRefItems) {
		this.nodeRefItems = nodeRefItems;
	}


	@Override
	protected Collection<NodeRef> runAs( DeferredUserCanUserSeeItems deferredUserCanUserSeeItems, String runUser) {
		return this.nodeRefItems;
	}

	@Override
	protected String getFullyAuthenticatedUser() {
		return "authenticatedUser";
	}
	
}
