package com.surevine.alfresco.webscript.gsa.getallitems;

import java.util.Date;

import org.alfresco.service.cmr.repository.NodeRef;

public class GSANodePropertyServiceSameDateMock extends
		GSANodePropertyServiceMock {
	
	private static Date _date = new Date();
	
	@Override
	public Date getModifiedDate(NodeRef nodeRef) {
		return _date;
	}
	
}
