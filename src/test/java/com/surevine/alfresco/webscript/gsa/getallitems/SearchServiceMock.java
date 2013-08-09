package com.surevine.alfresco.webscript.gsa.getallitems;

import java.io.Serializable;
import java.util.List;

import org.alfresco.service.cmr.repository.InvalidNodeRefException;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.StoreRef;
import org.alfresco.service.cmr.repository.XPathException;
import org.alfresco.service.cmr.search.QueryParameter;
import org.alfresco.service.cmr.search.QueryParameterDefinition;
import org.alfresco.service.cmr.search.ResultSet;
import org.alfresco.service.cmr.search.SearchParameters;
import org.alfresco.service.cmr.search.SearchParameters.Operator;
import org.alfresco.service.cmr.search.SearchService;
import org.alfresco.service.namespace.NamespacePrefixResolver;
import org.alfresco.service.namespace.QName;

public class SearchServiceMock implements SearchService {
	
	protected int _results=1000;
	
	public SearchServiceMock(int results)
	{
		_results=results;
	}
	
	public SearchServiceMock()
	{

	}
	
	@Override
	public ResultSet query(StoreRef store, String language, String query) {
		
		return new MockResultSet(_results);
	}

	@Override
	public boolean contains(NodeRef arg0, QName arg1, String arg2)
			throws InvalidNodeRefException {
		// Auto-generated method stub intentionally unimplemented
		return false;
	}

	@Override
	public boolean contains(NodeRef arg0, QName arg1, String arg2, Operator arg3)
			throws InvalidNodeRefException {
		// Auto-generated method stub intentionally unimplemented
		return false;
	}

	@Override
	public boolean like(NodeRef arg0, QName arg1, String arg2, boolean arg3)
			throws InvalidNodeRefException {
		// Auto-generated method stub intentionally unimplemented
		return false;
	}

	@Override
	public ResultSet query(SearchParameters arg0) {
		return new MockResultSet(_results);
	}


	@Override
	public ResultSet query(StoreRef arg0, QName arg1, QueryParameter[] arg2) {
		return new MockResultSet(_results);
	}

	@Override
	public ResultSet query(StoreRef arg0, String arg1, String arg2,
			QueryParameterDefinition[] arg3) {
		return new MockResultSet(_results);
	}

	@Override
	public List<NodeRef> selectNodes(NodeRef arg0, String arg1,
			QueryParameterDefinition[] arg2, NamespacePrefixResolver arg3,
			boolean arg4) throws InvalidNodeRefException, XPathException {
		// Auto-generated method stub intentionally unimplemented
		return null;
	}

	@Override
	public List<NodeRef> selectNodes(NodeRef arg0, String arg1,
			QueryParameterDefinition[] arg2, NamespacePrefixResolver arg3,
			boolean arg4, String arg5) throws InvalidNodeRefException,
			XPathException {
		// Auto-generated method stub intentionally unimplemented
		return null;
	}

	@Override
	public List<Serializable> selectProperties(NodeRef arg0, String arg1,
			QueryParameterDefinition[] arg2, NamespacePrefixResolver arg3,
			boolean arg4) throws InvalidNodeRefException, XPathException {
		// Auto-generated method stub intentionally unimplemented
		return null;
	}

	@Override
	public List<Serializable> selectProperties(NodeRef arg0, String arg1,
			QueryParameterDefinition[] arg2, NamespacePrefixResolver arg3,
			boolean arg4, String arg5) throws InvalidNodeRefException,
			XPathException {
		// Auto-generated method stub intentionally unimplemented
		return null;
	}

}
