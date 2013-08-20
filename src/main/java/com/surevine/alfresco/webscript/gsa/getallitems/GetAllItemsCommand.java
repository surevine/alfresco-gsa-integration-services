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

import java.util.Collection;
import java.util.Date;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.search.SearchService;

import com.surevine.alfresco.webscript.gsa.exception.GSAInvalidParameterException;

/**
 * Interface describing the GetAllItems commands
 * @author simonw
 *
 */
public interface GetAllItemsCommand {
 
	/**
	 * Retrieve all the items in the repository.  Responses may be paged.
	 * @param since Only retrieve items modified since this date
	 * @return SearchResults objects encapsulating the results of this search
	 * @throws GSAInvalidParameterException If since if null or in the past
	 */
	public SearchResults getAllItems(Date since) throws GSAInvalidParameterException;
	
	/**
	 * Retrieve all the items in the repository.  Responses may be paged.
	 * @param since Only retrieve items modified since this date
	 * @param startAt If multiple items are tied for the "earliest" item within the search and one of them is this item,
	 * discard all the items that occur before this item according to some deterministic ordering
	 * @return SearchResults objects encapsulating the results of this search
	 * @throws GSAInvalidParameterException If since if null or in the past or if startAt is null
	 */
	public SearchResults getAllItems(Date since, NodeRef startAt) throws GSAInvalidParameterException;
	
	/**
	 * Retrieve all the items in the repository.  Responses may be paged.
	 * @param since Only retrieve items modified since this date
	 * @param maxItems A hint as to the number of items the caller wishes to retrieve.  The system must not return more
	 * than this number of items, but may return less.  If it does, this should be indicated in the return value
	 * @return SearchResults objects encapsulating the results of this search
	 * @throws GSAInvalidParameterException If since if null or in the past or if maxItems is less than one or is null
	 */
	public SearchResults getAllItems(Date since, int maxItems) throws GSAInvalidParameterException;
	
	/**
	 * Retrieve all the items in the repository.  Responses may be paged.
	 * @param since Only retrieve items modified since this date
	 * @param maxItems A hint as to the number of items the caller wishes to retrieve.  The system must not return more
	 * than this number of items, but may return less.  If it does, this should be indicated in the return value
	 * @param startAt If multiple items are tied for the "earliest" item within the search and one of them is this item,
	 * discard all the items that occur before this item according to some deterministic ordering
	 * @return SearchResults objects encapsulating the results of this search
	 * @throws GSAInvalidParameterException If since if null or in the past, if startAt is null or if maxItems is less than one or is null
	 */
	public SearchResults getAllItems(Date since, NodeRef startAt, int maxItems) throws GSAInvalidParameterException;
	
	/**
	 * Implementors of this interface are dependant upon a SearchService to retrieve items from the repo
	 * @param searchService
	 */
	public void setSearchService(SearchService searchService);
	
	/**
	 * Implementors of this interface are dependant upon a GSANodePropertyService to retrieve details of node in query results
	 * @param gsaNodePropertyService
	 */
	public void setGSANodePropertyService(GSANodePropertyService gsaNodePropertyService);
	
	public SearchService getSearchService();
	
	public GSANodePropertyService getGSANodePropertyService();
	
}
