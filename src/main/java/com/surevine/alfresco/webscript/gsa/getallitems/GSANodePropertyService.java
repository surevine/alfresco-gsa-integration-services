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

import java.util.Collection;
import java.util.Date;

import org.alfresco.service.cmr.repository.ContentService;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.cmr.security.PersonService;

import com.surevine.alfresco.webscript.gsa.getallitems.NodeRefTypeResolver.NodeRefType;

/**
 * Interface for a service whose job it is to retrieve information about nodes from the Alfresco
 * repository
 * @author simonw
 *
 */
public interface GSANodePropertyService {
	
	Date getModifiedDate(NodeRef nodeRef);
	
	/**
	 * Retrieve a URL to the given nodeRef.  The URL must be a share URL.
	 * @param nodeRef The item to retrieve the URL for, which should both exist and refer to a cm:content 
	 * node managed by Share
	 * @return String representing a url <i>fragment</i>, starting from the context root (ie. /share) and 
	 * finishing at the end of the URL - a properley authenticated user visiting this URL will be able to
	 * see the target nodeRef in context
	 */
	String getURL(NodeRef nodeRef);
	
	long getContentSize(NodeRef nodeRef);
	
	String getTitle(NodeRef nodeRef);
	
	String getContent(NodeRef nodeRef);
	
	SecurityLabel getSecurityLabel(NodeRef nodeRef);
	
	void setNodeService(NodeService nodeService);
	
	void setContentService(ContentService contentService);
	
	String getMimeType(NodeRef nodeRef);
	
	void setProfileNod(String nod);

	void setProfilePM(String pm);
	
	void setProfileNatn(Collection<String> natn);

	/**
	 * Return the modifing user.
	 * 
	 * <p>Uses the format username (Firstname Lastname). Values of Firstname
	 * which are equal, ignoring case, to "Officer" and values of either the
	 * Firstname or Lastname which equal the username are discarded.</p>
	 * 
	 * @param nodeRef The nodeRef to retrieve the modifier for.
	 * @return The name of the modifying user.
	 */
	String getModifiedBy(NodeRef nodeRef);

	void setPersonService(PersonService personService);

	/**
	 * Returns the type of a document.
	 * 
	 * @param nr The {@link NodeRef} to query the type for.
	 * @return The type of document.
	 */
	NodeRefType getDocumentType(NodeRef nodeRef);
}
