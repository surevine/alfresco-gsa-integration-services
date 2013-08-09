package com.surevine.alfresco.webscript.gsa.getallitems;

import java.util.Date;

import org.alfresco.service.cmr.repository.NodeRef;

public class SearchResultStub extends SearchResultImpl  {

	private String url="http://testurl.url";
	
	private Date lastModified= new Date();
	
	private NodeRef nodeRef = new NodeRef("http", "abc", "id");
	
	public SearchResultStub()
	{
		
	}
	
	public SearchResultStub(String theUrl, Date theLastModified, NodeRef theNodeRef)
	{
		url=theUrl;
		lastModified=theLastModified;
		nodeRef=theNodeRef;
	}
	
	@Override
	public String getURL() {
		return url;
	}

	@Override
	public Date getLastModifiedDate() {
		return lastModified;
	}

	@Override
	public NodeRef getNodeRef() {
		return nodeRef;
	}

	@Override
	public String getTitle() {
		// Auto-generated method stub intentionally unimplemented
		return null;
	}


	@Override
	public String getContent() {
		// Auto-generated method stub intentionally unimplemented
		return null;
	}


	@Override
	public SecurityLabel getSecurityLabel() {
		// Auto-generated method stub intentionally unimplemented
		return null;
	}


	@Override
	public String getMimeType() {
		// Auto-generated method stub intentionally unimplemented
		return null;
	}

}
