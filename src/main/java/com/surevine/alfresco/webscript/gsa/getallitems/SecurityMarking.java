package com.surevine.alfresco.webscript.gsa.getallitems;

public class SecurityMarking {
	
	private String _name;
	private SecurityMarkingType _type=SecurityMarkingType.CLOSED;
	
	public String getName()
	{
		return _name;
	}
	
	public void setName(String name)
	{
		_name=name;
	}
	
	public void setType(SecurityMarkingType type)
	{
		_type=type;
	}
	
	public SecurityMarkingType getType()
	{
		return _type;
	}

}
