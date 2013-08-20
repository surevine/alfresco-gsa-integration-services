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
