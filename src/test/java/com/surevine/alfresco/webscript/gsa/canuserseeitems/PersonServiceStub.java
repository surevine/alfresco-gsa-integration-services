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
