package com.surevine.alfresco.webscript.gsa.getallitems;

import org.springframework.extensions.surf.util.Content;
import org.springframework.extensions.webscripts.Match;
import org.springframework.extensions.webscripts.Runtime;
import org.springframework.extensions.webscripts.WebScriptRequest;
import org.springframework.extensions.webscripts.Description.FormatStyle;

public class WebScriptRequestMock implements WebScriptRequest {

	private String _startAt;
	private String _since;
	private String _limit;
	
	public WebScriptRequestMock(String startAt, String since, String limit)
	{
		_startAt=startAt;
		_since=since;
		_limit=limit;
	}
	
	@Override
	public String getParameter(String arg0) {
		if (arg0.equals("startAt"))
		{
			return _startAt;
		}
		if (arg0.equals("since"))
		{
			return _since;
		}
		if (arg0.equals("limit"))
		{
			return _limit;
		}
		return null;
	}
	
	//Everything below here is just stubbed
	
	
	@Override
	public boolean forceSuccessStatus() {
		// Auto-generated method stub intentionally unimplemented
		return false;
	}

	@Override
	public String getAgent() {
		// Auto-generated method stub intentionally unimplemented
		return null;
	}

	@Override
	public Content getContent() {
		// Auto-generated method stub intentionally unimplemented
		return null;
	}

	@Override
	public String getContentType() {
		// Auto-generated method stub intentionally unimplemented
		return null;
	}

	@Override
	public String getContextPath() {
		// Auto-generated method stub intentionally unimplemented
		return null;
	}

	@Override
	public String getExtensionPath() {
		// Auto-generated method stub intentionally unimplemented
		return null;
	}

	@Override
	public String getFormat() {
		// Auto-generated method stub intentionally unimplemented
		return null;
	}

	@Override
	public FormatStyle getFormatStyle() {
		// Auto-generated method stub intentionally unimplemented
		return null;
	}

	@Override
	public String getHeader(String arg0) {
		// Auto-generated method stub intentionally unimplemented
		return null;
	}

	@Override
	public String[] getHeaderNames() {
		// Auto-generated method stub intentionally unimplemented
		return null;
	}

	@Override
	public String[] getHeaderValues(String arg0) {
		// Auto-generated method stub intentionally unimplemented
		return null;
	}

	@Override
	public String getJSONCallback() {
		// Auto-generated method stub intentionally unimplemented
		return null;
	}

	@Override
	public String[] getParameterNames() {
		// Auto-generated method stub intentionally unimplemented
		return null;
	}

	@Override
	public String[] getParameterValues(String arg0) {
		// Auto-generated method stub intentionally unimplemented
		return null;
	}

	@Override
	public String getPathInfo() {
		// Auto-generated method stub intentionally unimplemented
		return null;
	}

	@Override
	public String getQueryString() {
		// Auto-generated method stub intentionally unimplemented
		return null;
	}

	@Override
	public Runtime getRuntime() {
		// Auto-generated method stub intentionally unimplemented
		return null;
	}

	@Override
	public String getServerPath() {
		// Auto-generated method stub intentionally unimplemented
		return null;
	}

	@Override
	public String getServiceContextPath() {
		// Auto-generated method stub intentionally unimplemented
		return null;
	}

	@Override
	public Match getServiceMatch() {
		// Auto-generated method stub intentionally unimplemented
		return null;
	}

	@Override
	public String getServicePath() {
		// Auto-generated method stub intentionally unimplemented
		return null;
	}

	@Override
	public String getURL() {
		// Auto-generated method stub intentionally unimplemented
		return null;
	}

	@Override
	public boolean isGuest() {
		// Auto-generated method stub intentionally unimplemented
		return false;
	}

	@Override
	public Object parseContent() {
		// Auto-generated method stub intentionally unimplemented
		return null;
	}

}
