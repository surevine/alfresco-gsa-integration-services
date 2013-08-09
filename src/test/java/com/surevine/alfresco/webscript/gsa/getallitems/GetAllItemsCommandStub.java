package com.surevine.alfresco.webscript.gsa.getallitems;

import java.util.Arrays;
import java.util.Date;

import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.search.SearchService;

public class GetAllItemsCommandStub implements GetAllItemsCommand {

	@Override
	public SearchResults getAllItems(Date since) {
		
		return getSearchResults();
	}

	@Override
	public SearchResults getAllItems(Date since, NodeRef startAt) {
		return getAllItems(since);
	}

	@Override
	public SearchResults getAllItems(Date since, int maxItems) {
		return getAllItems(since);
	}

	@Override
	public SearchResults getAllItems(Date since, NodeRef startAt, int maxItems) {
		return getAllItems(since);
	}
	
	private SearchResults getSearchResults()
	{
		SearchResults results = new SearchResultsImpl();
		SearchResult[] sra = {  new SearchResultStub(),  new SearchResultStub(),  new SearchResultStub(),  new SearchResultStub(),  new SearchResultStub(), 
				 new SearchResultStub(),  new SearchResultStub(),  new SearchResultStub(),  new SearchResultStub(),  new SearchResultStub()  };
		results.setResults(Arrays.asList(sra));
		results.moreAvailable(true);
		return results;
	}

	@Override
	public void setSearchService(SearchService searchService) {
		// Auto-generated method stub intentionally unimplemented
		
	}

	@Override
	public void setGSANodePropertyService(
			GSANodePropertyService gsaNodePropertyService) {
		// Auto-generated method stub intentionally unimplemented
		
	}

	@Override
	public SearchService getSearchService() {
		// Auto-generated method stub intentionally unimplemented
		return null;
	}

	@Override
	public GSANodePropertyService getGSANodePropertyService() {
		// Auto-generated method stub intentionally unimplemented
		return null;
	}
	
	

}