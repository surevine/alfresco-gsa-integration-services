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
