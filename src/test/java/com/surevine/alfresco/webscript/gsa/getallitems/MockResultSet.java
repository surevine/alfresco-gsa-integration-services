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
