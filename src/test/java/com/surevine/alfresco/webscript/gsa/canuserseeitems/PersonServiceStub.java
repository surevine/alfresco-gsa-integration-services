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

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.security.PersonService;
import org.alfresco.service.namespace.QName;

/**
 * Very minimal stub for the GSA work.  Says every user exists, except that a single user can be specified who does not exist
 * @author simonw
 *
 */
public class PersonServiceStub implements PersonService {

	private String _doesNotExist=null;
	
	public void setPersonWhoDoesNotExist(String name)
	{
		_doesNotExist=name;
	}
	
	public String getPersonWhoDoesNotExist()
	{
		return _doesNotExist;
	}
	
	@Override
	public boolean personExists(String name) {
		if (_doesNotExist == null)
		{
			return true;
		}
		return (!name.equalsIgnoreCase(_doesNotExist));
	}
	
	@Override
	public boolean createMissingPeople() {
		//Intentionally unimplemented stub method
		return false;
	}

	@Override
	public NodeRef createPerson(Map<QName, Serializable> arg0) {
		//Intentionally unimplemented stub method
		return null;
	}

	@Override
	public NodeRef createPerson(Map<QName, Serializable> arg0, Set<String> arg1) {
		//Intentionally unimplemented stub method
		return null;
	}

	@Override
	public void deletePerson(String arg0) {
		//Intentionally unimplemented stub method

	}

	@Override
	public void deletePerson(NodeRef arg0) {
		//Intentionally unimplemented stub method

	}

	@Override
	public Set<NodeRef> getAllPeople() {
		//Intentionally unimplemented stub method
		return null;
	}

	@Override
	public Set<QName> getMutableProperties() {
		//Intentionally unimplemented stub method
		return null;
	}

	@Override
	public NodeRef getPeopleContainer() {
		//Intentionally unimplemented stub method
		return null;
	}

	@Override
	public Set<NodeRef> getPeopleFilteredByProperty(QName arg0,
			Serializable arg1) {
		//Intentionally unimplemented stub method
		return null;
	}

	@Override
	public NodeRef getPerson(String arg0) {
		//Intentionally unimplemented stub method
		return null;
	}

	@Override
	public NodeRef getPerson(String arg0, boolean arg1) {
		//Intentionally unimplemented stub method
		return null;
	}

	@Override
	public String getUserIdentifier(String arg0) {
		//Intentionally unimplemented stub method
		return null;
	}

	@Override
	public boolean getUserNamesAreCaseSensitive() {
		//Intentionally unimplemented stub method
		return false;
	}

	@Override
	public boolean isMutable() {
		//Intentionally unimplemented stub method
		return false;
	}

	@Override
	public void setCreateMissingPeople(boolean arg0) {
		//Intentionally unimplemented stub method

	}

	@Override
	public void setPersonProperties(String arg0, Map<QName, Serializable> arg1) {
		//Intentionally unimplemented stub method

	}

	@Override
	public void setPersonProperties(String arg0, Map<QName, Serializable> arg1,
			boolean arg2) {
		//Intentionally unimplemented stub method

	}

}
