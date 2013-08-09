/**
 * 
 */
package com.surevine.alfresco.webscript.gsa.getallitems;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.alfresco.model.ContentModel;
import org.alfresco.model.ForumModel;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.junit.Before;
import org.junit.Test;

import com.surevine.alfresco.webscript.gsa.getallitems.NodeRefTypeResolver.NodeRefType;

/**
 * Unit tests for {@link NodeRefTypeResolver}.
 * 
 * @author richardm
 */
public class NodeRefTypeResolverTest {
	
	private NodeRefTypeResolver _nrtr;
	private NodeService _nodeService;
	private NodeRef _nodeRef;
	
	@Before
	public void setUp() throws Exception {
		_nrtr = mock(NodeRefTypeResolver.class);
		when(_nrtr.getType(any(NodeService.class), any(NodeRef.class))).thenCallRealMethod();
		
		_nodeService = mock(NodeService.class);
		
		_nodeRef = new NodeRef("workspace://SpacesStore/xxxx");
	}

	@Test
	public void testDocument() throws Exception {
		when(_nodeService.getType(_nodeRef)).thenReturn(ContentModel.TYPE_CONTENT);
		when(_nrtr.getPath(any(NodeService.class), any(NodeRef.class))).thenReturn(
				"/{http://www.alfresco.org/model/application/1.0}company_home/{http://www.alfresco.org/model/site/1.0}sites/{http://www.alfresco.org/model/content/1.0}sandboxdeletedItems/{http://www.alfresco.org/model/content/1.0}documentLibrary/{http://www.alfresco.org/model/content/1.0}T44S2.txt");
		
		assertEquals(NodeRefType.DOCUMENT, _nrtr.getType(_nodeService, _nodeRef));
	}

	@Test
	public void testDocumentComment() throws Exception {
		when(_nodeService.getType(_nodeRef)).thenReturn(ForumModel.TYPE_POST);
		when(_nrtr.getPath(any(NodeService.class), any(NodeRef.class))).thenReturn(
				"/{http://www.alfresco.org/model/application/1.0}company_home/{http://www.alfresco.org/model/site/1.0}sites/{http://www.alfresco.org/model/content/1.0}sandboxdeletedItems/{http://www.alfresco.org/model/content/1.0}documentLibrary/{http://www.alfresco.org/model/content/1.0}T44S2.txt");
		
		assertEquals(NodeRefType.DOCUMENT_COMMENT, _nrtr.getType(_nodeService, _nodeRef));
	}

	@Test
	public void testWiki() throws Exception {
		when(_nodeService.getType(_nodeRef)).thenReturn(ContentModel.TYPE_CONTENT);
		when(_nrtr.getPath(any(NodeService.class), any(NodeRef.class))).thenReturn(
				"/{http://www.alfresco.org/model/application/1.0}company_home/{http://www.alfresco.org/model/site/1.0}sites/{http://www.alfresco.org/model/content/1.0}sandboxdeletedItems/{http://www.alfresco.org/model/content/1.0}wiki/{http://www.alfresco.org/model/content/1.0}test");
		
		assertEquals(NodeRefType.WIKI, _nrtr.getType(_nodeService, _nodeRef));
	}

	@Test
	public void testFolder() throws Exception {
		when(_nodeService.getType(_nodeRef)).thenReturn(ContentModel.TYPE_FOLDER);
		
		assertEquals(NodeRefType.FOLDER, _nrtr.getType(_nodeService, _nodeRef));
	}

	@Test
	public void testPerson() throws Exception {
		when(_nodeService.getType(_nodeRef)).thenReturn(ContentModel.TYPE_PERSON);
		
		assertEquals(NodeRefType.PERSON, _nrtr.getType(_nodeService, _nodeRef));
	}

	@Test
	public void testUnknown() throws Exception {
		when(_nodeService.getType(_nodeRef)).thenReturn(ContentModel.TYPE_CONTENT);
		when(_nrtr.getPath(any(NodeService.class), any(NodeRef.class))).thenReturn("");
		
		assertEquals(null, _nrtr.getType(_nodeService, _nodeRef));
	}
}
