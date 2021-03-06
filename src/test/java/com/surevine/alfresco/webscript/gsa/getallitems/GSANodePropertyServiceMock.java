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

import org.alfresco.service.cmr.repository.ContentService;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.cmr.security.PersonService;

import com.surevine.alfresco.webscript.gsa.getallitems.NodeRefTypeResolver.NodeRefType;

public class GSANodePropertyServiceMock extends GSANodePropertyServiceImpl implements GSANodePropertyService {

	private Date _nextDate = new Date(new Date().getTime()-(1000*60*60*24));
	
	public void setNextDate(Date nextDate)
	{
		_nextDate=nextDate;
	}
	
	@Override
	public Date getModifiedDate(NodeRef nodeRef) {
		_nextDate = new Date(_nextDate.getTime()+(1000*60*60*2));
		return _nextDate;
	}
	
	@Override
	public String getModifiedBy(NodeRef nodeRef) {
		return "jbloggs (Joe Bloggs)";
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public NodeRefType getDocumentType(final NodeRef nodeRef) {
		return NodeRefType.DOCUMENT;
	}

	@Override
	public String getURL(NodeRef nodeRef) {
		return "http://test.url";
	}

	@Override
	public long getContentSize(NodeRef nodeRef) {
		return 1024;
	}

	@Override
	public String getTitle(NodeRef nodeRef) {
		return "Title";
	}

	@Override
	public String getContent(NodeRef nodeRef) {
		return escapeContentString("<p>Hello!</p><p><br />Welcome to the sandbox. &nbsp;Feel free to do anything you like here - play with the product and find out how it works.</p><p>&nbsp;</p><p>Have fun! <img title\"Cool\" src=\"/share/modules/editors/tiny_mce/plugins/emotions/img/smiley-cool.gif\" border=\"0\" alt=\"Cool\" /></p>");
	}

	@Override
	public SecurityLabel getSecurityLabel(NodeRef nodeRef) {
		return GetAllItemsCommandWebscriptImplTest.getTestSecurityLabel();
	}

	@Override
	public void setNodeService(NodeService nodeService) {
		// Intentionally empty
	}

	@Override
	public void setContentService(ContentService contentService) {
		// Intentionally empty
	}
	
	@Override
	public void setPersonService(PersonService personService) {
		// Intentionally empty
	}
	
	@Override
	public String getMimeType(NodeRef nodeRef)
	{
		return "text/html";
	}
	
	@Override
	protected String getNationalCaveatsString(NodeRef nodeRef)
	{
		return "UK/SV/AB EYES ONLY";
	}
	
	public String[] getNationalCaveats(NodeRef nodeRef)
	{
		return super.getNationalCaveats(nodeRef);
	}

}
