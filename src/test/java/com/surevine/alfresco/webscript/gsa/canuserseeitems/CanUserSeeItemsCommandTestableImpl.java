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
