package com.surevine.alfresco.webscript.gsa.getallitems;

import java.util.Date;

import org.alfresco.service.cmr.repository.NodeRef;

import com.surevine.alfresco.webscript.gsa.getallitems.NodeRefTypeResolver.NodeRefType;

public interface SearchResult extends Comparable<SearchResult> {
	
	String getURL();
	
	Date getLastModifiedDate();
	
	NodeRef getNodeRef();
	
	void setURL(String url);
	
	void setLastModifiedDate(Date modified);
	
	void setNodeRef(NodeRef nr);
	
	String getTitle();
	
	void setTitle(String title);
	
	String getContent();
	
	void setContent(String content);
	
	SecurityLabel getSecurityLabel();
	
	void setSecurityLabel(SecurityLabel secLab);
	
	String getMimeType();
	
	void setMimeType(String mimeType);
	
	void setModifiedBy(String modifiedBy);
	
	String getModifiedBy();
	
	void setDocumentType(NodeRefType documentType);

	NodeRefType getDocumentType();
}
