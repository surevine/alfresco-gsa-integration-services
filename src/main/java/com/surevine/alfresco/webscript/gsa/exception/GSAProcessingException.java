package com.surevine.alfresco.webscript.gsa.exception;

import java.util.Date;

public class GSAProcessingException extends RuntimeException {

	public GSAProcessingException(String message, Throwable cause, int code)
	{
		super(code+"|"+message+"|"+code, cause);
	}
	
	public GSAProcessingException(String message, int code)
	{
		super(code+"|"+message);
	}
}
