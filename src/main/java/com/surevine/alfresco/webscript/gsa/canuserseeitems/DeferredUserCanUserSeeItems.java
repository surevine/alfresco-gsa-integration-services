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

import org.alfresco.repo.security.authentication.AuthenticationUtil.RunAsWork;
import org.alfresco.service.cmr.repository.NodeRef;

public class DeferredUserCanUserSeeItems implements RunAsWork<Collection<NodeRef>> {

	private Collection<NodeRef> itemsToQuery = null;
	private CanUserSeeItemsCommand canUserSeeItemsCommand = null;
	
	public DeferredUserCanUserSeeItems( CanUserSeeItemsCommand canUserSeeItemsCommand, Collection<NodeRef> itemsToQuery) {
		this.canUserSeeItemsCommand = canUserSeeItemsCommand;
		this.itemsToQuery = itemsToQuery;
	}
	
	@Override
	public Collection<NodeRef> doWork() throws Exception {
		return canUserSeeItemsCommand.canUserSeeItems( this.itemsToQuery);
	}

}
