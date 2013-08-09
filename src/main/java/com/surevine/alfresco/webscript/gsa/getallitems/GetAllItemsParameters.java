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
