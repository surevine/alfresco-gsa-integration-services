package com.surevine.alfresco.webscript.gsa.exception;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.surevine.alfresco.webscript.gsa.canuserseeitems.CanUserSeeItemsCommandImpl;

public class GSAInvalidParameterException extends Exception {

	private static final Log _logger = LogFactory.getLog(GSAInvalidParameterException.class);
	
	public GSAInvalidParameterException(String message, Throwable cause, int code)
	{
		super(code+"|"+message+"|"+new Date().getTime()%10000, cause);
		if (_logger.isDebugEnabled())
		{
			_logger.debug("Exception Created!", this);
		}
	}
	
}
