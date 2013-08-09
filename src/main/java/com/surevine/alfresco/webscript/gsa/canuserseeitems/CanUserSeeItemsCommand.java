package com.surevine.alfresco.webscript.gsa.canuserseeitems;

import java.util.Collection;

import org.alfresco.module.org_alfresco_module_dod5015.caveat.RMCaveatConfigService;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.cmr.security.AuthorityService;
import org.alfresco.service.cmr.security.PermissionService;

public interface CanUserSeeItemsCommand
{
	public Collection<NodeRef> canUserSeeItems(Collection<NodeRef> itemsToQuery);
	public CanUserSeeItemsResponse canUserSeeItems(Collection<NodeRef> itemsToQuery, String runUser);
	public void setNodeService(NodeService nodeService);
	public void setCaveatConfigService( RMCaveatConfigService caveatConfigService);
	public void setAuthorityService( AuthorityService authorityService);
	public void setPermissionService(PermissionService permissionService);
}
