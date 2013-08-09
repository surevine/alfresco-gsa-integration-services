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
