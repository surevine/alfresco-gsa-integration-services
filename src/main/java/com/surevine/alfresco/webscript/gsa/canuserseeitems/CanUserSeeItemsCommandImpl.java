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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.alfresco.model.ContentModel;
import org.alfresco.module.org_alfresco_module_dod5015.caveat.RMCaveatConfigService;
import org.alfresco.repo.domain.permissions.Permission;
import org.alfresco.repo.security.authentication.AuthenticationUtil;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.cmr.security.AccessStatus;
import org.alfresco.service.cmr.security.AuthorityService;
import org.alfresco.service.cmr.security.PermissionService;
import org.alfresco.service.cmr.security.PersonService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.surevine.alfresco.webscript.gsa.exception.GSAProcessingException;

public class CanUserSeeItemsCommandImpl implements CanUserSeeItemsCommand
{

	private PermissionService permissionService = null;
	private NodeService nodeService = null;
	private RMCaveatConfigService caveatConfigService = null;
	private AuthorityService authorityService = null;
	private static final Log _logger = LogFactory.getLog(CanUserSeeItemsCommandImpl.class);	
	
	public CanUserSeeItemsCommandImpl()
	{	}
	
	/*
	 * (tc) Two possible approaches - remove invalid items from the given list or
	 * create a new empty list and add permitted items. the second seems more robust/secure 
	 */
	public Collection<NodeRef> canUserSeeItems(Collection<NodeRef> itemsToQuery)
	{
		if( null == itemsToQuery) 
		{
			throw new IllegalArgumentException("itemsToQuery can not be null");
		}

		if( 0 == itemsToQuery.size() ) 
		{
			_logger.debug("No items were provided, so returning empty list");
			return Collections.emptyList();
		}

		Collection<NodeRef> result = new ArrayList<NodeRef>();
		
		int numberOfInputItems=0;
		if (_logger.isInfoEnabled())
		{
			numberOfInputItems=itemsToQuery.size();
		}
		
		for(NodeRef nodeRef : itemsToQuery) 
		{
			try 
			{
				if( nodeService.exists(nodeRef) && caveatConfigService.hasAccess( nodeRef) ) 
				{
					if (permissionService.hasReadPermission(nodeRef).equals(AccessStatus.ALLOWED))
					{
						result.add( nodeRef);
					}
					else
					{
						_logger.debug("No permissions on "+nodeRef);
					}
				}
			}
			catch (Exception e)
			{
				_logger.warn("Exception occured when processing nodeRef: "+nodeRef, new GSAProcessingException(e.getMessage(), e, 34830));
			}
		}
		
		if (_logger.isInfoEnabled())
		{
			_logger.info(numberOfInputItems+" items were provided, "+result.size()+" are being returned");
		}
		
		return result;
	}

	public void setNodeService(NodeService nodeService)
	{
		this.nodeService = nodeService;
	}
	
	public void setPermissionService (PermissionService permissionService)
	{
		this.permissionService=permissionService;
	}

	public void setCaveatConfigService( RMCaveatConfigService caveatConfigService)
	{
		this.caveatConfigService = caveatConfigService;
	}

	/**
	 * Executes the key functionality of this class.  It is up to the caller to determine that runAsUser exists and is a real user.
	 * Behaviour is undefined if a user called runAsUser does not exist in Alfresco
	 */
	@Override
	public CanUserSeeItemsResponse canUserSeeItems( Collection<NodeRef> itemsToQuery, String runAsUser) 
	{

		CanUserSeeItemsResponse response = new CanUserSeeItemsResponse();
		response.setRunAsUser(runAsUser);
		
		//need to get the session user, so we can return the user the search was actually run as.
		if( null == runAsUser || "".equals( runAsUser)) {
			response.setResponseNodes( this.canUserSeeItems(itemsToQuery) );
			response.setRunAsUser(getFullyAuthenticatedUser());
			return response;
		}

		Collection<NodeRef> responseNodes = null;

		//authority service - test if runUser is an admin user, or a special given user
		if( getFullyAuthenticatedUser().equals("admin-gsa") || this.authorityService.hasAdminAuthority() )
		{
			//current user has adminAuthority, so let's agree and perform as 'run as':
			response.setRunAsUser(runAsUser);
			DeferredUserCanUserSeeItems deferredUserCanUserSeeItems = new DeferredUserCanUserSeeItems( this, itemsToQuery);
			responseNodes = this.runAs( deferredUserCanUserSeeItems, runAsUser);
		}
		else
		{
			_logger.warn("The user "+getFullyAuthenticatedUser()+" does not have rights to run this query as the specified user ("+runAsUser+"), so executing this query in their own right");
			response.setRunAsUser(getFullyAuthenticatedUser());
			responseNodes = this.canUserSeeItems( itemsToQuery);
		}

		response.setResponseNodes( responseNodes);
		return response;
	}

	@Override
	public void setAuthorityService(AuthorityService authorityService)
	{
		this.authorityService = authorityService;
	}

	
	/**
	 * this method has been added as a class member so that a 'test' version could be written to overwrite this 
	 * class to enable the testing/stubbing of the 'AuthenticationUtil'.
	 */
	protected Collection<NodeRef> runAs( DeferredUserCanUserSeeItems deferredUserCanUserSeeItems, String runUser)
	{
		return AuthenticationUtil.runAs( deferredUserCanUserSeeItems, runUser);
	}
	
	/**
	 * this method has been added as a class member so that a 'test' version could be written to overwrite this 
	 * class to enable the testing/stubbing of the 'AuthenticationUtil'.
	 */
	protected String getFullyAuthenticatedUser() {
		return AuthenticationUtil.getFullyAuthenticatedUser();
	}
	
}
