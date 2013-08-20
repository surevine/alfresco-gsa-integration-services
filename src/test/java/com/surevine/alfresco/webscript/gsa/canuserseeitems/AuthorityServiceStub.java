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

import java.util.Collection;
import java.util.Set;

import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.security.AuthorityService;
import org.alfresco.service.cmr.security.AuthorityType;

public class AuthorityServiceStub implements AuthorityService {

	private Boolean sessionUserIsAdmin = false;
	
	public void setSessionUserIsAdmin(Boolean sessionUserIsAdmin) {
		this.sessionUserIsAdmin = sessionUserIsAdmin;
	}

	@Override
	public boolean hasAdminAuthority() {
		return this.sessionUserIsAdmin;
	}
	
	@Override
	public void addAuthority(String arg0, String arg1) {
		// Auto-generated method stub intentionally unimplemented

	}

	@Override
	public void addAuthority(Collection<String> arg0, String arg1) {
		// Auto-generated method stub intentionally unimplemented

	}

	@Override
	public void addAuthorityToZones(String arg0, Set<String> arg1) {
		// Auto-generated method stub intentionally unimplemented

	}

	@Override
	public boolean authorityExists(String arg0) {
		// Auto-generated method stub intentionally unimplemented
		return false;
	}

	@Override
	public String createAuthority(AuthorityType arg0, String arg1) {
		// Auto-generated method stub intentionally unimplemented
		return null;
	}

	@Override
	public String createAuthority(AuthorityType arg0, String arg1, String arg2,
			Set<String> arg3) {
		// Auto-generated method stub intentionally unimplemented
		return null;
	}

	@Override
	public void deleteAuthority(String arg0) {
		// Auto-generated method stub intentionally unimplemented

	}

	@Override
	public void deleteAuthority(String arg0, boolean arg1) {
		// Auto-generated method stub intentionally unimplemented

	}

	@Override
	public Set<String> findAuthorities(AuthorityType arg0, String arg1,
			boolean arg2, String arg3, String arg4) {
		// Auto-generated method stub intentionally unimplemented
		return null;
	}

	@Override
	public Set<String> getAllAuthorities(AuthorityType arg0) {
		// Auto-generated method stub intentionally unimplemented
		return null;
	}

	@Override
	public Set<String> getAllAuthoritiesInZone(String arg0, AuthorityType arg1) {
		// Auto-generated method stub intentionally unimplemented
		return null;
	}

	@Override
	public Set<String> getAllRootAuthorities(AuthorityType arg0) {
		// Auto-generated method stub intentionally unimplemented
		return null;
	}

	@Override
	public Set<String> getAllRootAuthoritiesInZone(String arg0,
			AuthorityType arg1) {
		// Auto-generated method stub intentionally unimplemented
		return null;
	}

	@Override
	public Set<String> getAuthorities() {
		// Auto-generated method stub intentionally unimplemented
		return null;
	}

	@Override
	public Set<String> getAuthoritiesForUser(String arg0) {
		// Auto-generated method stub intentionally unimplemented
		return null;
	}

	@Override
	public String getAuthorityDisplayName(String arg0) {
		// Auto-generated method stub intentionally unimplemented
		return null;
	}

	@Override
	public Set<String> getAuthorityZones(String arg0) {
		// Auto-generated method stub intentionally unimplemented
		return null;
	}

	@Override
	public Set<String> getContainedAuthorities(AuthorityType arg0, String arg1,
			boolean arg2) {
		// Auto-generated method stub intentionally unimplemented
		return null;
	}

	@Override
	public Set<String> getContainingAuthorities(AuthorityType arg0,
			String arg1, boolean arg2) {
		// Auto-generated method stub intentionally unimplemented
		return null;
	}

	@Override
	public Set<String> getDefaultZones() {
		// Auto-generated method stub intentionally unimplemented
		return null;
	}

	@Override
	public String getName(AuthorityType arg0, String arg1) {
		// Auto-generated method stub intentionally unimplemented
		return null;
	}

	@Override
	public NodeRef getOrCreateZone(String arg0) {
		// Auto-generated method stub intentionally unimplemented
		return null;
	}

	@Override
	public String getShortName(String arg0) {
		// Auto-generated method stub intentionally unimplemented
		return null;
	}

	@Override
	public NodeRef getZone(String arg0) {
		// Auto-generated method stub intentionally unimplemented
		return null;
	}

	@Override
	public boolean hasGuestAuthority() {
		// Auto-generated method stub intentionally unimplemented
		return false;
	}

	@Override
	public boolean isAdminAuthority(String arg0) {
		// Auto-generated method stub intentionally unimplemented
		return false;
	}

	@Override
	public boolean isGuestAuthority(String arg0) {
		// Auto-generated method stub intentionally unimplemented
		return false;
	}

	@Override
	public void removeAuthority(String arg0, String arg1) {
		// Auto-generated method stub intentionally unimplemented

	}

	@Override
	public void removeAuthorityFromZones(String arg0, Set<String> arg1) {
		// Auto-generated method stub intentionally unimplemented

	}

	@Override
	public void setAuthorityDisplayName(String arg0, String arg1) {
		// Auto-generated method stub intentionally unimplemented

	}

	@Override
	public Set<String> getContainingAuthoritiesInZone(AuthorityType type,
			String name, String zoneName, AuthorityFilter filter, int size) {
		// TODO Auto-generated method stub
		return null;
	}

}
