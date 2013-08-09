package com.surevine.alfresco.webscript.gsa.getallitems;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class SecurityLabel {

	private String _nod;
	private String _pm;
	private Collection<SecurityMarking> _markings = new ArrayList<SecurityMarking>(10);
	private String _freeform="";
	private Collection<String> _natnCavs = new ArrayList<String>(5);
	
	
	public String getNOD(){
		return _nod;
	}
	
	public void setNOD(String nod)
	{
		_nod=nod;
	}
	
	public String getProtectiveMarking()
	{
		return _pm;
	}
	
	public void setProtectiveMarking(String pm)
	{
		_pm=pm;
	}
	
	public Iterator<SecurityMarking> getMarkings()
	{
		return _markings.iterator();
	}
	
	public void addMarking(SecurityMarking marking)
	{
		_markings.add(marking);
	}
	
	public String getFreeformCaveats()
	{
		return _freeform;
	}
	
	public void setFreeformCaveats(String freeform)
	{
		_freeform=freeform;
	}
	
	public Iterator<String> getNationalityCaveats()
	{
		return _natnCavs.iterator();
	}
	
	public void addNationalityCaveat(String nationality)
	{
		if (nationality!=null && nationality.trim().length()>1)
		{
			_natnCavs.add(nationality);
		}
	}
	
}
