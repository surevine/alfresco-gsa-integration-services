package com.surevine.alfresco.webscript.gsa.getallitems;

import java.util.List;

public interface SearchResults {

	public boolean isMoreAvailable();
	
	public List<SearchResult> getResults();
	
	public void setResults(List<SearchResult> result);
	
	public void moreAvailable(boolean isMoreAvailable);
	
	
}
