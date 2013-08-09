package com.surevine.alfresco.webscript.gsa.getallitems;

import java.util.Date;

import org.alfresco.service.cmr.repository.NodeRef;

import com.surevine.alfresco.webscript.gsa.getallitems.SecurityLabel;


/**
 * Mock to deal with null or missing properties - for security label, returns a non-null security label object full 
 * of null values to simulate what happens when an item in the repo doesn't have any security label
 * @author simonw
 *
 */
public class GSANodePropertyServiceNullPropertiesMock extends
		GSANodePropertyServiceMock {
	
	@Override
	public Date getModifiedDate(NodeRef nodeRef) {
		return null;
	}

	@Override
	public String getURL(NodeRef nodeRef) {
		return null;
	}

	@Override
	public long getContentSize(NodeRef nodeRef) {
		return (Long)null;
	}

	@Override
	public String getTitle(NodeRef nodeRef) {
		return null;
	}

	@Override
	public String getContent(NodeRef nodeRef) {
		return null;
	}

	@Override
	public SecurityLabel getSecurityLabel(NodeRef nodeRef) {
		SecurityLabel label = new SecurityLabel();
		label.addNationalityCaveat(null);
		label.setFreeformCaveats(null);
		label.setNOD(null);
		label.setProtectiveMarking(null);
		return label;
	}

}
