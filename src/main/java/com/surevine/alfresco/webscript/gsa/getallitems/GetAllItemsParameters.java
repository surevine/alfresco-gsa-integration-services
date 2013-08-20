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

public class GetAllItemsParameters {

	private Date _since;
	private NodeRef _startAt=null;
	private  int _maxItems=Integer.MAX_VALUE;
	
	public Date getSince() 
	{
		return _since;
	}
	
	public NodeRef getStartAt()
	{
		return _startAt;
	}
	
	public int getMaxItems()
	{
		return _maxItems;
	}
	
	public void setSince(Date since)
	{
		_since=since;
	}
	
	public void setStartAt(NodeRef startAt)
	{
		_startAt=startAt;
	}
	
	public void setMaxItems(int maxItems)
	{
		_maxItems=maxItems;
	}
	
	public String toString()
	{
		StringBuffer sb = new StringBuffer(100);
		sb.append("GetAllItemsParameters:[");
		sb.append("since=").append(_since).append("|");
		sb.append("startAt=").append(_startAt).append("|");
		sb.append("maxItems=").append(_maxItems);
		sb.append("]");
		return sb.toString();
	}
	
}
