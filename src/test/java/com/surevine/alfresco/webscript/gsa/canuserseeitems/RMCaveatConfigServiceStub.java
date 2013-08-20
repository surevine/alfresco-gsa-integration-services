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
package com.surevine.alfresco.webscript.gsa.canuserseeitems;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.alfresco.module.org_alfresco_module_dod5015.caveat.RMCaveatConfigService;
import org.alfresco.module.org_alfresco_module_dod5015.caveat.RMConstraintInfo;
import org.alfresco.service.cmr.repository.NodeRef;

public class RMCaveatConfigServiceStub implements RMCaveatConfigService {

	@Override
	public RMConstraintInfo addRMConstraint(String arg0, String arg1,
			String[] arg2) {
		// Auto-generated method stub intentionally unimplemented
		return null;
	}

	@Override
	public void addRMConstraintListValue(String arg0, String arg1, String arg2) {
		// Auto-generated method stub intentionally unimplemented

	}

	@Override
	public void deleteRMConstraint(String arg0) {
		// Auto-generated method stub intentionally unimplemented

	}

	@Override
	public Set<RMConstraintInfo> getAllRMConstraints() {
		// Auto-generated method stub intentionally unimplemented
		return null;
	}

	@Override
	public Map<String, List<String>> getListDetails(String arg0) {
		// Auto-generated method stub intentionally unimplemented
		return null;
	}

	@Override
	public List<String> getRMAllowedValues(String arg0) {
		// Auto-generated method stub intentionally unimplemented
		return null;
	}

	@Override
	public RMConstraintInfo getRMConstraint(String arg0) {
		// Auto-generated method stub intentionally unimplemented
		return null;
	}

	@Override
	public boolean hasAccess(NodeRef arg0) {
		//already return true for now
		return true;
	}

	@Override
	public void init() {
		// Auto-generated method stub intentionally unimplemented

	}

	@Override
	public void removeRMConstraintListAuthority(String arg0, String arg1) {
		// Auto-generated method stub intentionally unimplemented

	}

	@Override
	public void removeRMConstraintListValue(String arg0, String arg1) {
		// Auto-generated method stub intentionally unimplemented

	}

	@Override
	public NodeRef updateOrCreateCaveatConfig(File arg0) {
		// Auto-generated method stub intentionally unimplemented
		return null;
	}

	@Override
	public NodeRef updateOrCreateCaveatConfig(String arg0) {
		// Auto-generated method stub intentionally unimplemented
		return null;
	}

	@Override
	public NodeRef updateOrCreateCaveatConfig(InputStream arg0) {
		// Auto-generated method stub intentionally unimplemented
		return null;
	}

	@Override
	public RMConstraintInfo updateRMConstraintAllowedValues(String arg0,
			String[] arg1) {
		// Auto-generated method stub intentionally unimplemented
		return null;
	}

	@Override
	public void updateRMConstraintListAuthority(String arg0, String arg1,
			List<String> arg2) {
		// Auto-generated method stub intentionally unimplemented

	}

	@Override
	public void updateRMConstraintListValue(String arg0, String arg1,
			List<String> arg2) {
		// Auto-generated method stub intentionally unimplemented

	}

	@Override
	public RMConstraintInfo updateRMConstraintTitle(String arg0, String arg1) {
		// Auto-generated method stub intentionally unimplemented
		return null;
	}

}
