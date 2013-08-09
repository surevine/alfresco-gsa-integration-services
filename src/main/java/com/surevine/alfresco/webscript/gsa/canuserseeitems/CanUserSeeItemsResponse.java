package com.surevine.alfresco.webscript.gsa.canuserseeitems;

import java.util.Collection;

import org.alfresco.service.cmr.repository.NodeRef;

public class CanUserSeeItemsResponse {

	private Collection<NodeRef> responseNodes;
	private String runAsUser;

	public Collection<NodeRef> getResponseNodes() {
		return responseNodes;
	}
	public void setResponseNodes(Collection<NodeRef> responseNodes) {
		this.responseNodes = responseNodes;
	}
	public String getRunAsUser() {
		return runAsUser;
	}
	public void setRunAsUser(String runAsUser) {
		this.runAsUser = runAsUser;
	}
	
	
	
}
