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
