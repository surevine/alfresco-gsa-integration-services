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
