package com.surevine.alfresco.webscript.gsa.getallitems;

import java.util.List;

public class SearchResultsImpl implements SearchResults {

	private boolean _more;
	
	private List<SearchResult> _results;
	
	@Override
	public boolean isMoreAvailable() {
		return _more;
	}

	@Override
	public List<SearchResult> getResults() {
		return _results;
	}
	
	public void moreAvailable(boolean isMoreAvailable)
	{
		_more=isMoreAvailable;
	}
	
	public void setResults(List<SearchResult> results)
	{
		_results=results;
	}

}