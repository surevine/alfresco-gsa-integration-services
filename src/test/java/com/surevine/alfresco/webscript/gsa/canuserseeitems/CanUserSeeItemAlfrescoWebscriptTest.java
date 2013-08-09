package com.surevine.alfresco.webscript.gsa.canuserseeitems;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.alfresco.model.ContentModel;
import org.alfresco.repo.security.authentication.AuthenticationComponent;
import org.alfresco.repo.web.scripts.BaseWebScriptTest;
import org.alfresco.service.cmr.repository.ChildAssociationRef;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.cmr.site.SiteInfo;
import org.alfresco.service.cmr.site.SiteService;
import org.alfresco.service.cmr.site.SiteVisibility;
import org.alfresco.service.namespace.NamespaceService;
import org.alfresco.service.namespace.QName;
import org.alfresco.util.GUID;
import org.junit.Ignore;
import org.springframework.context.ApplicationContext;
import org.springframework.extensions.webscripts.TestWebScriptServer;
import org.springframework.extensions.webscripts.TestWebScriptServer.PutRequest;
import org.springframework.extensions.webscripts.TestWebScriptServer.Request;
import org.springframework.extensions.webscripts.TestWebScriptServer.Response;

@Ignore
public class CanUserSeeItemAlfrescoWebscriptTest extends BaseWebScriptTest
{
    private static final String URL_SITES = "/api/sites";
    private static final String URL_MEMBERSHIPS = "/memberships";
    private static final String URL_CANUSERSEEITEMS = "/svTheme/retrieval/canUserSeeItems";
    private static final String USER_ONE = "UserOne";
    private static final String USER_TWO = "UserTwo";
    private static final String USER_ADMIN = "admin";

    private SiteService siteService;
    private AuthenticationComponent authenticationComponent;
    
    public CanUserSeeItemAlfrescoWebscriptTest() {
    }
    
	public void setUp()
	{
		TestWebScriptServer s = getServer();

		ApplicationContext ac = s.getApplicationContext();
		this.siteService = (SiteService) ac.getBean("SiteService");
        this.authenticationComponent = (AuthenticationComponent) getServer().getApplicationContext().getBean("authenticationComponent");

    	// Authenticate as user
    	this.authenticationComponent.setCurrentUser(USER_ADMIN);
    	
		System.out.println("exiting setup ...");
	}
	
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testCanUserSeeItems() throws Exception
	{	
        // Create a site
        String shortName = GUID.generate();
        String sitePreset = "preset1";
        String siteTitle = "TestSite";
        String siteDescription = "Test Site Description";
        SiteInfo siteInfo = siteService.createSite( sitePreset, shortName, siteTitle, siteDescription, SiteVisibility.PUBLIC);
        
        NodeService nodeService = (NodeService) getServer().getApplicationContext().getBean("NodeService");

        //NodeRef parentNodeRef = new NodeRef("store:///foo");
        NodeRef parentNodeRef = siteInfo.getNodeRef();

        String nodeName = GUID.generate();
        QName assocTypeQName = QName.createQName( ContentModel.ASSOC_CONTAINS.toString() );
        QName assocNameQName = QName.createQName( NamespaceService.CONTENT_MODEL_1_0_URI, QName.createValidLocalName( nodeName) );
        QName typeQName = ContentModel.TYPE_BASE;
        Map<QName,Serializable> properties = new HashMap<QName,Serializable>();
        System.out.println( "about to create node...");
        ChildAssociationRef childAssocRef = nodeService.createNode( parentNodeRef, assocTypeQName, assocNameQName, typeQName, properties);
        System.out.println( "created node with ref: " + childAssocRef.getChildRef().toString() );

        String requestXMLString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><sv:request xmlns:sv=\"http://surevine.com/alfresco/gsa/1.0\"><nodeRef>" + childAssocRef.getChildRef().toString() + "</nodeRef></sv:request>";

        Request request = new PutRequest( URL_CANUSERSEEITEMS, requestXMLString, "application/xml");
        Response response = sendRequest( request, 200);
        //TestWebScriptServer server = TestWebScriptRepoServer.getTestServer();
        //Response response = server.submitRequest( request);

        //JSONObject result = new JSONObject(response.getContentAsString());

        // process response
		//byte[] content = response.getContentAsByteArray();
		String actualXMLResponse = response.getContentAsString().trim();

        String expectedXMLResponse = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><sv:response xmlns:sv=\"http://surevine.com/alfresco/gsa/1.0\" runAsUser=\"" + USER_ADMIN + "\"><nodeRef>" + childAssocRef.getChildRef().toString() + "</nodeRef></sv:response>";
		assertEquals( expectedXMLResponse, actualXMLResponse);
	}
	
	
	
	
	/*
	public void testCreateSites() throws Exception
	{
        // Create a site
        String shortName = GUID.generate();
        siteService.createSite("myPreset", shortName, "myTitle", "myDescription", SiteVisibility.PUBLIC);

        // Build the JSON membership object
        JSONObject membership = new JSONObject();
        membership.put("role", SiteModel.SITE_CONSUMER);
        JSONObject person = new JSONObject();
        person.put("userName", USER_TWO);
        membership.put("person", person);

        //Post the membership
        PostRequest postRequest = new PostRequest( URL_SITES + "/" + shortName + URL_MEMBERSHIPS, membership.toString(), "application/json");
        Response response = sendRequest( postRequest, 200);
        JSONObject result = new JSONObject(response.getContentAsString());

        // Check the result
        assertEquals( SiteModel.SITE_CONSUMER, membership.get("role") );
        assertEquals(USER_TWO, membership.getJSONObject("person").get(
                "userName"));

        // Get the membership list
        response = sendRequest(new GetRequest(URL_SITES + "/" + shortName + URL_MEMBERSHIPS), 200);
        JSONArray result2 = new JSONArray(response.getContentAsString());
        assertNotNull(result2);
        assertEquals(2, result2.length());
	}
	*/
		
}