package com.surevine.alfresco.webscript.gsa.getallitems;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.alfresco.model.ContentModel;
import org.alfresco.model.ForumModel;
import org.alfresco.service.cmr.repository.ContentReader;
import org.alfresco.service.cmr.repository.ContentService;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.cmr.security.PersonService;
import org.alfresco.service.namespace.QName;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.extensions.surf.util.Base64;

import com.surevine.alfresco.repo.profile.UserProfileModel;
import com.surevine.alfresco.webscript.gsa.exception.GSAProcessingException;
import com.surevine.alfresco.webscript.gsa.getallitems.NodeRefTypeResolver.NodeRefType;

/**
 * Implementation class for GSANodePropertyService.
 * 
 * This class is responsible for interfacing between the alfresco repository itself and the rest of the GSA service
 * @author simonw
 *
 */
public class GSANodePropertyServiceImpl implements GSANodePropertyService {
	
	/**
	 * Placeholder name used by some organisations.
	 */
	private static final String OFFICER = "officer";

	private NodeService _nodeService;
	private ContentService _contentService;
	private PersonService _personService;
	
	//TODO - move this into EnhancedSecurityModel.java once we've using an SDK we built from our branch
	private static final String NAMESPACE_SV="http://www.alfresco.org/model/enhancedSecurity/0.3";
	private static final QName NOD_QN = QName.createQName(NAMESPACE_SV, "nod");
	private static final QName PM_QN = QName.createQName(NAMESPACE_SV, "pm");
	private static final QName OG_QN = QName.createQName(NAMESPACE_SV, "openMarkings");
	private static final QName CG_QN = QName.createQName(NAMESPACE_SV, "closedMarkings");
	private static final QName ORG_QN = QName.createQName(NAMESPACE_SV, "organisations");
	private static final QName NATN_QN = QName.createQName(NAMESPACE_SV, "nationalityCaveats");
	private static final QName FREEFORM_QN = QName.createQName(NAMESPACE_SV, "freeFormCaveats");
	
	/**
	 * String within an item's path immediatley preceding the name of the site the item is in
	 */
	private static final String SITE_PART_OF_PATH="{http://www.alfresco.org/model/site/1.0}sites/{http://www.alfresco.org/model/content/1.0}";
	
	private static final Log _logger = LogFactory.getLog(GSANodePropertyServiceImpl.class);
	
	/**
	 * Profiles don't have security labels, so we return a constant value informed by the values of these
	 * (injected) fields
	 */
	private String _profileNod;
	private String _profilePM;
	private Collection<String> _profileNatn;
	private int _maxItemSizeInBytes=1024*1024*50;
	
	private NodeRefTypeResolver _nodeRefResolver = new NodeRefTypeResolver();
	
	public void setProfileNod(String nod)
	{
		_profileNod=nod;
	}
	
	public void setProfilePM(String pm)
	{
		_profilePM=pm;
	}
	
	public void setProfileNatn(Collection<String> natn)
	{
		_profileNatn=natn;
	}
	
	public void setMaxItemSizeInBytes(int maxSize)
	{
		_maxItemSizeInBytes=maxSize;
	}
	
	@Override
	public Date getModifiedDate(NodeRef nodeRef) {
		
		//People
		if (isNodeRefProfile(nodeRef))
		{
			return (Date)_nodeService.getProperty(nodeRef, UserProfileModel.PROP_MODIFIED);
		}
		
		//Things
		return (Date)_nodeService.getProperty(nodeRef, ContentModel.PROP_MODIFIED); //I was a little worried about this cast but it's in the alfresco source
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getModifiedBy(final NodeRef nodeRef) {
		final String userName = (String) _nodeService.getProperty(nodeRef, ContentModel.PROP_MODIFIER);
		
		String modifiedBy = null;
		
		if (userName != null) {
			final NodeRef person = _personService.getPerson(userName);
			
			if (person != null) {
				final String firstName = (String) _nodeService.getProperty(
						person, ContentModel.PROP_FIRSTNAME);
				final String lastName = (String) _nodeService.getProperty(
						person, ContentModel.PROP_LASTNAME);
				
				final StringBuilder fullNameBuilder = new StringBuilder();
				
				// Strip out the first name if it is "Officer" or the username.
				if (!firstName.equals(userName) && !firstName.equalsIgnoreCase(OFFICER)) {
					fullNameBuilder.append(firstName);
					fullNameBuilder.append(' ');
				}
				
				// String out the last name if it is just the username.
				if (!lastName.equals(userName)) {
					fullNameBuilder.append(lastName);
				}
				
				final String fullName = fullNameBuilder.toString().trim();
				
				// If we have a useful full name, use it, otherwise just use
				// the username alone as the modifiedBy value.
				if (fullName.length() > 0) {
					modifiedBy = String.format("%s (%s)", userName, fullName);
				} else {
					modifiedBy = userName;
				}
			}
		}
		
		return modifiedBy;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public NodeRefType getDocumentType(final NodeRef nodeRef) {
		return _nodeRefResolver.getType(_nodeService,  nodeRef);
	}

	/**
	 * Retrieve a URL to the given nodeRef.  The URL must be a share URL.  To do this,
	 * we interrogate the document path and delegate to different private methods depending on the type
	 * of the content item as inferred from it's path.
	 * @param nodeRef The item to retrieve the URL for, which should both exist and refer to a cm:content 
	 * node managed by Share
	 * @return String representing a url <i>fragment</i>, starting from the context root (ie. /share) and 
	 * finishing at the end of the URL - a properley authenticated user visiting this URL will be able to
	 * see the target nodeRef in context
	 */
	@Override
	public String getURL(NodeRef nodeRef) {

		String path = _nodeService.getPath(nodeRef).toString();
		if (_logger.isDebugEnabled())
		{
			_logger.debug("Getting url to "+nodeRef+" which has path "+path);
		}
		if (path.indexOf("documentLibrary/")!=-1)
		{
			if (path.indexOf(" discussion/")!=-1) //We can get away with this because a document called "foo discussion" would have the space escaped to "foo%20discussion"
			{
				return getDocumentCommentURL(nodeRef);
			}
			return getDocLibURL(nodeRef);
		}
		else if (path.indexOf("wiki/")!=-1)
		{
			return getWikiURL(nodeRef);
		}
		else if(path.indexOf("discussions/")!=-1)
		{
			return getDiscussionURL(nodeRef);
		}
		else if (isNodeRefProfile(nodeRef))
		{
			return getProfileURL(nodeRef);
		}
		return getUnknownItemURL(nodeRef);
	}
	
	protected boolean isNodeRefProfile(NodeRef nodeRef)
	{
		return (_nodeService.getPath(nodeRef).toString().indexOf("people/")!=-1);
	}
	
	private String getSiteName(String path)
	{
		String fromBeginning = path.substring(path.indexOf(SITE_PART_OF_PATH)+SITE_PART_OF_PATH.length());
		return fromBeginning.substring(0, fromBeginning.indexOf("/"));
	}
	
	private String getSiteName(NodeRef nodeRef)
	{
		return getSiteName(_nodeService.getPath(nodeRef).toString());
	}
	
	private String getProfileURL(NodeRef nodeRef)
	{
		return "/share/page/user/"+_nodeService.getProperty(nodeRef, ContentModel.PROP_USERNAME)+"/profile";
	}
	
	private String getDocLibURL(NodeRef nodeRef)
	{
		if (_logger.isDebugEnabled())
		{
			_logger.debug("Getting a document library URL");
		}
		return "/share/page/site/"+getSiteName(nodeRef)+"/document-details?nodeRef="+nodeRef.toString();
	}
	
	private String getWikiURL(NodeRef nodeRef)
	{
		if (_logger.isDebugEnabled())
		{
			_logger.debug("Getting a wiki URL");
		}
		String pathStr = _nodeService.getPath(nodeRef).toString();
		return "/share/page/site/"+getSiteName(pathStr)+"/wiki-page?title="+pathStr.substring(pathStr.lastIndexOf("}")+1);
	}
	
	private String getDiscussionURL(NodeRef nodeRef)
	{
		if (_logger.isDebugEnabled())
		{
			_logger.debug("Getting a discussion URL");
		}
		QName type = _nodeService.getType(nodeRef);
		
		//We can assume that we've been passed the node of a post somewhere within the hierachy of a discussion.
		//But in order to generate a URL for a visible, in-context web page, we need to base our URL on the node
		//of the discussion topic as a whole.  So we navigate up the node hierachy until we reach a node with type
		//"fm:topic", which is the root of the discussion, and use that
		while (!type.equals(ForumModel.TYPE_TOPIC))
		{
			nodeRef=_nodeService.getPrimaryParent(nodeRef).getParentRef();
			if (nodeRef==null)
			{
				throw new GSAProcessingException("Could not generate a url for "+nodeRef+" which GSA identified as a discussion", new NullPointerException(), 90210);
			}
			type = _nodeService.getType(nodeRef);
		}
		
		if (_logger.isDebugEnabled())
		{
			_logger.debug("Root node of the topic was identified as: "+nodeRef);
		}
		
		return "/share/page/site/"+getSiteName(nodeRef)+"/discussions-topicview?topicId="+_nodeService.getProperty(nodeRef, ContentModel.PROP_NAME)+"&listViewLinkBack=true";
	}
	
	private String getUnknownItemURL(NodeRef nodeRef)
	{
		//TODO: we can't do this without breaking through the firewall - share could proxy into alfresco to do this but this is a big bit of work
		//so let's leave it until we know whether or not we need it
		throw new GSAProcessingException("The type of "+nodeRef.toString()+" could not be determined", null, 90211);
	}
	
	/**
	 * For document comments, get the grandparent of the comment, which is the document itself, and return a URL to that
	 */
	private String getDocumentCommentURL(NodeRef nodeRef)
	{
		if (_logger.isDebugEnabled())
		{
			_logger.debug("Getting a document comment URL");
		}
		
		NodeRef parent = _nodeService.getPrimaryParent(nodeRef).getParentRef();
		NodeRef grandParent = _nodeService.getPrimaryParent(parent).getParentRef();
		return getDocLibURL(_nodeService.getPrimaryParent(grandParent).getParentRef());
	}

	@Override
	/**
	 * Retrieve the size, in charecters, of the content held under the given node ref
	 */
	public long getContentSize(NodeRef nodeRef) {
		if (isNodeRefProfile(nodeRef))
		{
			return getUnescapedProfileContent(nodeRef).length();
		}
		ContentReader reader =  _contentService.getReader(nodeRef, ContentModel.PROP_CONTENT);
		return reader.getSize();
	}

	/**
	 * If the item has a cm:title property, we return that.  Otherwise, we extract the "filename" part
	 * of the item's path, and use that.  TODO:  Better behavior for discussion posts would be to navigate
	 * up the node hierachy to the topic root and use the title of that, with something similar for document
	 * comments
	 */
	@Override
	public String getTitle(NodeRef nodeRef) {
		
		//People
		if (isNodeRefProfile(nodeRef))
		{
			return _nodeService.getProperty(nodeRef, ContentModel.PROP_FIRSTNAME)+" "+_nodeService.getProperty(nodeRef, ContentModel.PROP_LASTNAME);
		}
		
		//Things
		String title = (String) _nodeService.getProperty(nodeRef, ContentModel.PROP_TITLE);

		if (title==null || title.trim().length()<1) //If we don't have a title, use the last bit of the path
		{
			
			if (_logger.isDebugEnabled())
			{
				_logger.debug("No title available for "+nodeRef+" so using the path");
			}
			
			String lastBitOfPath = _nodeService.getPath(nodeRef).last().getElementString();
			int endOfNamespaceIdx = lastBitOfPath.indexOf('}');
			try {
				return lastBitOfPath.substring(endOfNamespaceIdx+1);
			}
			catch (StringIndexOutOfBoundsException e)
			{
				throw new GSAProcessingException("An invalid path was found: Namespace expected in "+lastBitOfPath, e, 3681);
			}
			catch (NullPointerException npe)
			{
				throw new GSAProcessingException("No path could be found where one was expected", npe, 3682);
			}
		}
		return title;
	}

	/**
	 * We have this in a seperate method as we need to retrieve it's length seperately
	 * @param nodeRef
	 * @return
	 */
	private String getUnescapedProfileContent(NodeRef nodeRef)
	{
		//Start with the bio
		StringBuffer sb = new StringBuffer(200);
		
		Serializable bio = _nodeService.getProperty(nodeRef, UserProfileModel.PROP_BIOGRAPHY);
		if (bio!=null)
		{
			sb.append(bio).append(" ");
		}
		
		//Add amas, if present
		Collection<Serializable> amaColl = ((Collection<Serializable>)_nodeService.getProperty(nodeRef, UserProfileModel.PROP_ASK_ME_ABOUT));
		if (amaColl!=null)
		{
			Iterator<Serializable> amas = amaColl.iterator();
			while (amas.hasNext())
			{
				sb.append(amas.next()).append(" ");
			}
		}
		
		
		//Add telephone numbers
		Collection<Serializable> telephoneColl = ((Collection<Serializable>)_nodeService.getProperty(nodeRef, UserProfileModel.PROP_TELEPHONE_NUMBERS)); 
		if (telephoneColl!=null)
		{
			Iterator<Serializable> telephones = telephoneColl.iterator();
		
			//Telephone numbers are a formatted comma seperated String - search engines might not handle the commas as we would like so let's replace with a space
			while (telephones.hasNext())
			{
				sb.append(telephones.next().toString().replaceAll(",", " ")).append(" ");
			}
		}
		
		//Construct, log and return output String
		String output = sb.toString();
		if (_logger.isDebugEnabled())
		{
			_logger.debug("NodeRef "+nodeRef+" contained the following profile data: "+output);
		}
		return output;
	}
	
	/**
	 * This method isn't optimised for very large content items, and will break entirely with anything > 4Gb, but that's
	 * more than fine given our use case
	 */
	@Override
	public String getContent(NodeRef nodeRef) {
		
		//People
		if (isNodeRefProfile(nodeRef))
		{
			return escapeContentString(getUnescapedProfileContent(nodeRef));
		}
		
		//Things
		ContentReader reader =  _contentService.getReader(nodeRef, ContentModel.PROP_CONTENT);
		
		int dataSize = (int)getContentSize(nodeRef);
		if (dataSize > _maxItemSizeInBytes)
		{
			_logger.info("Truncating "+nodeRef+" from "+ reader.getSize()+" to "+_maxItemSizeInBytes+" bytes");
			dataSize = _maxItemSizeInBytes;
		}
		
		byte[] data = new byte[dataSize]; //Seriously, you shouldn't be trying to get 4Gb of data using this method anyway
		BufferedInputStream bis=null;
		try 
		{
			bis = new BufferedInputStream(reader.getContentInputStream());
			bis.read(data, 0, dataSize);
		} 
		catch (Exception e) 
		{
			throw new GSAProcessingException("Error reading content data for "+nodeRef, e, 100000);
		}
		finally
		{
			if (bis!=null)
			{
				try 
				{
					bis.close();
				}
				catch (IOException e)
				{
					_logger.warn("Could not close byte array input stream - ignoring");
				}
			}
		}
		_logger.debug(data.length+" bytes of content data returned for "+nodeRef);
		return escapeContentString(data);
	}
	
	public String getMimeType(NodeRef nodeRef)
	{
		
		//People - always text/plain as we are assembling the profile information from String fields
		if (isNodeRefProfile(nodeRef))
		{
			return "text/plain";
		}
		
		//Things
		return _contentService.getReader(nodeRef, ContentModel.PROP_CONTENT).getMimetype();
	}


	/**
	 * Pretty self-explanatory - some fiddling about to deal with the fact that properties may be null;
	 */
	@Override
	public SecurityLabel getSecurityLabel(NodeRef nodeRef) {
		
		//People don't really have a security label, so just return a constant, injected, value
		if (isNodeRefProfile(nodeRef))
		{
			SecurityLabel label = new SecurityLabel();
			label.setNOD(_profileNod);
			label.setProtectiveMarking(_profilePM);
			Iterator<String> natns = _profileNatn.iterator();
			while (natns.hasNext())
			{
				label.addNationalityCaveat(natns.next());
			}
			return label;
		}
		
		//Things
		SecurityLabel label = new SecurityLabel();
		
		//These setters should just accept 'null' as inputs so need no checking
		label.setNOD((String)(_nodeService.getProperty(nodeRef, NOD_QN)));
		label.setProtectiveMarking((String)(_nodeService.getProperty(nodeRef, PM_QN)));
		label.setFreeformCaveats((String)(_nodeService.getProperty(nodeRef, FREEFORM_QN)));
		
		Serializable openMarkingsList = _nodeService.getProperty(nodeRef, OG_QN);
		if (openMarkingsList!=null)
		{
			Iterator<String> openMarkings = ((List<String>)openMarkingsList).iterator(); 
			while (openMarkings.hasNext())
			{
				SecurityMarking marking = new SecurityMarking();
				marking.setName(openMarkings.next());
				marking.setType(SecurityMarkingType.OPEN);
				label.addMarking(marking);
			}
		}
		
		Serializable closedMarkingsList = _nodeService.getProperty(nodeRef, CG_QN);
		if (closedMarkingsList!=null)
		{
			Iterator<String> closedMarkings = ((List<String>)(closedMarkingsList)).iterator();
			while (closedMarkings.hasNext())
			{
				SecurityMarking marking = new SecurityMarking();
				marking.setName(closedMarkings.next());
				marking.setType(SecurityMarkingType.CLOSED);
				label.addMarking(marking);
			}
		}
		
		Serializable organisationsList = _nodeService.getProperty(nodeRef, ORG_QN);
		if (organisationsList!=null)
		{
			Iterator<String> organisations = ((List<String>)(organisationsList)).iterator();
			while (organisations.hasNext())
			{
				SecurityMarking marking = new SecurityMarking();
				marking.setName(organisations.next());
				marking.setType(SecurityMarkingType.ORGANISATION);
				label.addMarking(marking);
			}
		}
		
		String[] natns = getNationalCaveats(nodeRef);
		if (natns!=null)
		{
			for (int i=0; i < natns.length; i++)
			{
				label.addNationalityCaveat(natns[i]);
			}
		}
		
		return label;
	}
	
	/**
	 * TODO: Split out from main body of getSecurityLabel method to aid testability - not what I usually like doing, maybe indicates we need to refactor
	 * the getSecurityLabel method but I'm not sure right now
	 */
	protected String[] getNationalCaveats(NodeRef nodeRef)
	{
		String nationalityCaveats = getNationalCaveatsString(nodeRef);
		if (nationalityCaveats!=null && nationalityCaveats.trim().length()>1)
		{
			return nationalityCaveats.replaceAll("EYES ONLY", "").trim().split("/");
		}
		return null;
	}
	
	protected String getNationalCaveatsString(NodeRef nodeRef)
	{
		return (String)(_nodeService.getProperty(nodeRef, NATN_QN));
	}
	
	/**
	 * This method will produce an incorrect encoding when called with a String created from a non-unicode binary file.
	 * If you're not processing character data, use the byte array version instead
	 */
	protected String escapeContentString(String content)
	{
		return escapeContentString(content.getBytes());
	}
	
	protected String escapeContentString(byte[] data)
	{
		return Base64.encodeBytes(data);
	}
	
	@Override
	public void setNodeService(NodeService nodeService) {
		_nodeService=nodeService;
	}

	@Override
	public void setContentService(ContentService contentService) {
		_contentService=contentService;
	}

	@Override
	public void setPersonService(PersonService personService) {
		_personService = personService;
	}
}
