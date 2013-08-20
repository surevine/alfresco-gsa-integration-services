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
