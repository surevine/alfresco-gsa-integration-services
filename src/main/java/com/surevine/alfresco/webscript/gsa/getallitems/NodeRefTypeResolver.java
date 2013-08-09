/**
 * 
 */
package com.surevine.alfresco.webscript.gsa.getallitems;

import org.alfresco.model.ContentModel;
import org.alfresco.model.ForumModel;
import org.alfresco.repo.site.SiteModel;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.cmr.repository.Path;
import org.alfresco.service.namespace.NamespaceService;
import org.alfresco.service.namespace.QName;
import org.apache.log4j.Logger;

/**
 * Class for handling the resolving of {@link NodeRef} types.
 * 
 * <p>Extracts the required information from the {@link Path} of the
 * {@link NodeRef} in order to determine the type e.g. Document, Wiki, Folder.
 * </p>
 * 
 * @author richardm
 */
public class NodeRefTypeResolver {
	
	/**
	 * Log4j {@link Logger}.
	 */
	private static final Logger LOGGER = Logger.getLogger(NodeRefTypeResolver.class);
	
	/**
	 * {@link String} values available within {@link NodeRef} {@link Path}s.
	 */
	private static final String
			COMPANY_HOME = "/{" + NamespaceService.APP_MODEL_1_0_URI + "}company_home",
			SITES = "/" +SiteModel.TYPE_SITES.toString(),
			CONTENT = "/{" + NamespaceService.CONTENT_MODEL_1_0_URI +"}";
	
	/**
	 * An enumeration of the types of document available.
	 * @author richardm
	 */
	enum NodeRefType {
		DISCUSSION,
		DOCUMENT_COMMENT,
		DOCUMENT,
		FOLDER,
		PERSON,
		WIKI;
	}

	/**
	 * Returns the {@link NodeRefType} of a given {@link NodeRef}.
	 */
	public NodeRefType getType(final NodeService nodeService, final NodeRef nodeRef) {
		final String path = getPath(nodeService, nodeRef);
		final QName type = nodeService.getType(nodeRef);
		
		NodeRefType result = null;
		
		if (type.equals(ContentModel.TYPE_PERSON)) {
			result = NodeRefType.PERSON;
		} else if (type.equals(ContentModel.TYPE_FOLDER)) {
			return NodeRefType.FOLDER;
		}
		
		if (result == null && path.startsWith(COMPANY_HOME)) {
			final String sitesPath = path.substring(COMPANY_HOME.length());
			
			if (sitesPath.startsWith(SITES)) {
				final String sitePath = sitesPath.substring(SITES.length());
				
				if (sitePath.startsWith(CONTENT)) {
					final String siteName = sitePath.substring(CONTENT.length(),
							sitePath.indexOf('/', CONTENT.length()));
					
					int offset = 2 * CONTENT.length() +siteName.length();
					final String documentType = sitePath.substring(offset,
							sitePath.indexOf('/', offset));
					
					if (documentType.toUpperCase().equals("WIKI")) {
						result = NodeRefType.WIKI;
					} else if (documentType.toUpperCase().equals("DISCUSSIONS")) {
						result = NodeRefType.DISCUSSION;
					} else if (documentType.toUpperCase().equals("DOCUMENTLIBRARY")) {
						if (type.equals(ForumModel.TYPE_POST)) {
							result = NodeRefType.DOCUMENT_COMMENT;
						} else {
							result = NodeRefType.DOCUMENT;
						}
					}
				}
			}
		}
		
		if (result == null) {
			LOGGER.warn("Failed to identify document type for nodeRef "
					+nodeRef +" with path " +path + " and type " +type);
		}
		
		return result;
	}
	
	/**
	 * Retrieve the {@link String} representation of a {@link Path}.
	 * 
	 * @param nodeService
	 * @param nodeRef
	 * @return
	 */
	String getPath(final NodeService nodeService, final NodeRef nodeRef) {
		return nodeService.getPath(nodeRef).toString();
	}
}
