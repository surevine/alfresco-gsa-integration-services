package com.surevine.alfresco.webscript.gsa.getallitems;

import java.util.Date;

import org.alfresco.service.cmr.repository.NodeRef;

import com.surevine.alfresco.webscript.gsa.exception.GSAProcessingException;
import com.surevine.alfresco.webscript.gsa.getallitems.NodeRefTypeResolver.NodeRefType;

public class SearchResultImpl implements SearchResult {

	private String _url;
	private Date _lastModified;
	private NodeRef _nodeRef; 
	private String _title;
	private String _content;
	private SecurityLabel _label;
	private String _mimeType;
	private String _modifiedBy;
	private NodeRefType _documentType;
	
	@Override
	public String getURL() {
		return _url;
	}

	@Override
	public Date getLastModifiedDate() {
		return _lastModified;
	}

	@Override
	public NodeRef getNodeRef() {
		return _nodeRef;
	}
	
	@Override
	public void setURL(String url)
	{
		_url=url;
	}
	
	@Override
	public void setLastModifiedDate(Date modified)
	{
		_lastModified=modified;
	}
	
	@Override
	public void setNodeRef(NodeRef nr)
	{
		_nodeRef=nr;
	}

	@Override
	public String getTitle() {
		return _title;
	}

	@Override
	public void setTitle(String title) {
		_title=title;		
	}

	@Override
	public String getContent() {
		return _content;
	}

	@Override
	public void setContent(String content) {
		_content=content;		
	}

	@Override
	public SecurityLabel getSecurityLabel() {
		return _label;
	}

	@Override
	public void setSecurityLabel(SecurityLabel secLab) {
		_label=secLab;
	}
	
	public String getMimeType()
	{
		return _mimeType;
	}
	
	public void setMimeType(String mimeType)
	{
		_mimeType=mimeType;
	}

	@Override
	public void setModifiedBy(String modifiedBy) {
		_modifiedBy = modifiedBy;
	}

	@Override
	public String getModifiedBy() {
		return _modifiedBy;
	}
	
	@Override
	public void setDocumentType(final NodeRefType documentType) {
		_documentType = documentType;
	}
	
	@Override
	public NodeRefType getDocumentType() {
		return _documentType;
	}

	@Override
	public int compareTo(SearchResult result) {
		if (_lastModified==null || result.getLastModifiedDate()==null)
		{
			throw new GSAProcessingException("Could not compare "+this+" with "+result, 7653452);
		}
		return _lastModified.compareTo(result.getLastModifiedDate());
	}
	
	public String toString()
	{
		return "SearchResult["+_nodeRef+"]";
	}
}
