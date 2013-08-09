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
