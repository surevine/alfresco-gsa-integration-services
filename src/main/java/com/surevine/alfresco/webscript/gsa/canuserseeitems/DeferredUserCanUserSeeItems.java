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
