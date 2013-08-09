package com.surevine.alfresco.webscript.gsa.getallitems;

public enum SecurityMarkingType {
	OPEN("open"), CLOSED("closed"), ORGANISATION("organisation");
	
	private final String _name;
	
	SecurityMarkingType(String name)
	{
		_name=name;
	}
	
	public String getName()
	{
		return _name;
	}
	
	public String toString()
	{
		return _name;
	}
}
