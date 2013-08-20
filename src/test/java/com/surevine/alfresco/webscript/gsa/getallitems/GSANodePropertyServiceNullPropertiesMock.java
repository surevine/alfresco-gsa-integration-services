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
