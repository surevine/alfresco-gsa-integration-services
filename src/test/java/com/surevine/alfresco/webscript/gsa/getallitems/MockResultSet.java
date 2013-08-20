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

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

import org.alfresco.service.cmr.repository.ChildAssociationRef;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.search.ResultSet;
import org.alfresco.service.cmr.search.ResultSetMetaData;
import org.alfresco.service.cmr.search.ResultSetRow;

import com.sun.star.uno.RuntimeException;

public class MockResultSet implements ResultSet {
	
	private int _results=1000;
	
	public MockResultSet(int results)
	{
		_results=results;
	}
	
	public MockResultSet()
	{
		throw new RuntimeException();
	}
	
	@Override
	public NodeRef getNodeRef(int arg0) {
		return new NodeRef("test", "test", new Integer(arg0).toString());
	}
	
	@Override
	public List<NodeRef> getNodeRefs() {
		List<NodeRef> nrs = new ArrayList<NodeRef>(_results);
		for (int i=0; i < _results; i++)
		{
			nrs.add(getNodeRef(i));
		}
		return nrs;
	}
	
	//Everything below here is stubbed

	@Override
	public void close() {
		// Auto-generated method stub intentionally unimplemented

	}

	@Override
	public boolean getBulkFetch() {
		// Auto-generated method stub intentionally unimplemented
		return false;
	}

	@Override
	public int getBulkFetchSize() {
		// Auto-generated method stub intentionally unimplemented
		return 0;
	}

	@Override
	public ChildAssociationRef getChildAssocRef(int arg0) {
		// Auto-generated method stub intentionally unimplemented
		return null;
	}

	@Override
	public List<ChildAssociationRef> getChildAssocRefs() {
		// Auto-generated method stub intentionally unimplemented
		return null;
	}

	@Override
	public ResultSetMetaData getResultSetMetaData() {
		// Auto-generated method stub intentionally unimplemented
		return null;
	}

	@Override
	public ResultSetRow getRow(int arg0) {
		// Auto-generated method stub intentionally unimplemented
		return null;
	}

	@Override
	public float getScore(int arg0) {
		// Auto-generated method stub intentionally unimplemented
		return 0;
	}

	@Override
	public int getStart() {
		// Auto-generated method stub intentionally unimplemented
		return 0;
	}

	@Override
	public boolean hasMore() {
		// Auto-generated method stub intentionally unimplemented
		return false;
	}

	@Override
	public int length() {
		// Auto-generated method stub intentionally unimplemented
		return 0;
	}

	@Override
	public boolean setBulkFetch(boolean arg0) {
		// Auto-generated method stub intentionally unimplemented
		return false;
	}

	@Override
	public int setBulkFetchSize(int arg0) {
		// Auto-generated method stub intentionally unimplemented
		return 0;
	}

	@Override
	public Iterator<ResultSetRow> iterator() {
		// Auto-generated method stub intentionally unimplemented
		return null;
	}

}
