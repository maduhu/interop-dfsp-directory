package com.l1p.interop.dfsp.directory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.Map;

import com.l1p.interop.JSONRPCRequest;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mule.tck.junit4.FunctionalTestCase;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DFSPDirectoryGatewayTest extends FunctionalTestCase {

	private final String getUserPath="/directory/user/get";
	private final String addUserPath="/directory/user/add";
	private final String serviceHost = "http://localhost:8081";

	protected Logger logger = LoggerFactory.getLogger(getClass());

	WebResource webService;

	@Override
	protected String getConfigResources() {
		return "DFSPDirectoryGatewayAPI.xml,DFSPDirectoryGateway.xml";
	}

	@BeforeClass
	public static void initEnv() {
		System.setProperty("MULE_ENV", "test");
		System.setProperty("spring.profiles.active", "test");
	}

	@Before
	public void initSslClient() throws NoSuchAlgorithmException, KeyStoreException, CertificateException, IOException,
			KeyManagementException {
		ClientConfig config = new DefaultClientConfig();
		webService = Client.create(config).resource(serviceHost);
	}

	@Test(timeout = 20000)
	public void testInvalidPathShouldReturn404() throws Exception {
		final String invalidPath = "path/shouldnt/exist";
		final String notJSON = "<BadRequest>This is not JSON</BadRequest>";

		logger.info("Posting event to web services");

		ClientResponse response = postRequest(invalidPath, notJSON);
		logger.info( "Received response from web service: " + response );

		assertEquals("Server did not respond with status 404 when presented with path " + invalidPath + " on port ", 404, response.getStatus() );

	}

	@Test(timeout = 20000)
	public void testValidGetUserRequestShouldReturnValidResponse() throws Exception {
		//todo - load in account data
		final String addUsersJSON = loadResourceAsString("testData/validAccountData.json");
		ClientResponse addUsersResponse = webService.path( addUserPath ).type( "application/json").post(ClientResponse.class, addUsersJSON);
		assertEquals("Server did not respond with status 200 for addUsers when presented with path " + addUserPath, 200, addUsersResponse.getStatus() );

		Map<String,String> paramMap = new HashMap<String,String>();
		paramMap.put( "userURI", "userdata.com/chrisg" );
		final String validRequest1 = new JSONRPCRequest( "id1", "directory.user.get", paramMap ).toJSONString();
		paramMap.put( "userURI", "userdata.com/magoo" );
		final String validRequest2 = new JSONRPCRequest( "id2", "directory.user.get", paramMap ).toJSONString();
		paramMap.put( "userURI", "userdata.com/sonof" );
		final String validRequest3 = new JSONRPCRequest( "id3", "directory.user.get", paramMap ).toJSONString();
		paramMap.put( "userURI", "userdata.com/mitty" );
		final String validRequest4 = new JSONRPCRequest( "id4", "directory.user.get", paramMap ).toJSONString();
		paramMap.put( "userURI", "userdata.com/missing" );
		final String missingAccountRequest = new JSONRPCRequest( "id5", "directory.user.get", paramMap ).toJSONString();

		logger.info("Posting events to web services");

		//test retrieving info for chrisg
		ClientResponse account1Response = postRequest( getUserPath, validRequest1);
		assertEquals("Server did not respond with status 200 for account1 when presented with path " + getUserPath, 200, account1Response.getStatus() );
		String account1ResponseContent = account1Response.getEntity( String.class );
		assertTrue( "Response for account1 did not contain expected name chrisg: " + account1ResponseContent, account1ResponseContent != null && account1ResponseContent.contains( "chrisg" ));

		//test retrieving info for magoo
		ClientResponse account2Response = postRequest( getUserPath, validRequest2);
		assertEquals("Server did not respond with status 200 for account2 when presented with path " + getUserPath, 200, account2Response.getStatus() );
		String account2ResponseContent = account2Response.getEntity( String.class );
		assertTrue( "Response for account2 did not contain expected name magoo: " + account2ResponseContent, account2ResponseContent != null && account2ResponseContent.contains( "magoo" ));

		//test retrieving info for sonof
		ClientResponse account3Response = postRequest( getUserPath, validRequest3);
		assertEquals("Server did not respond with status 200 for account3 when presented with path " + getUserPath, 200, account3Response.getStatus() );
		String account3ResponseContent = account3Response.getEntity( String.class );
		assertTrue( "Response for account3 did not contain expected name sonof: " + account3ResponseContent, account3ResponseContent != null && account3ResponseContent.contains( "sonof" ));

		//test retrieving info for mitty
		ClientResponse account4Response = postRequest( getUserPath, validRequest4);
		assertEquals("Server did not respond with status 200 for account4 when presented with path " + getUserPath, 200, account4Response.getStatus() );
		String account4ResponseContent = account4Response.getEntity( String.class );
		assertTrue( "Response for account4 did not contain expected name mitty: " + account4ResponseContent, account4ResponseContent != null && account4ResponseContent.contains( "mitty" ));

		//test retrieving info for a missing account
		ClientResponse missingAccountResponse = postRequest( getUserPath, missingAccountRequest);
		assertEquals("Server did not respond with status 500 for missingAccount when presented with path " + getUserPath, 500, missingAccountResponse.getStatus() );
		String expectedMissingAccountContent = "Account not found for userURI=userdata.com/missing";
		String missingAccountResponseContent = missingAccountResponse.getEntity( String.class );
		assertTrue( "Response for missingAccount did not contain expected text '" + expectedMissingAccountContent + "': " + missingAccountResponseContent, missingAccountResponseContent != null && missingAccountResponseContent.contains( expectedMissingAccountContent ));
	}

	@Test(timeout = 20000)
	public void testValidAddUserRequestShouldReturnValidResponse() throws Exception {
		final String addUsersJSON = loadResourceAsString("testData/validAccountData.json");
		logger.info("Posting request to path " + addUserPath );

		ClientResponse response = postRequest( addUserPath, addUsersJSON);
		logger.info( "Received response from web service: " + response );

		//validate response status is 200
		assertEquals("Server did not respond with status 200 when presented with path " + addUserPath + " on port ", 200, response.getStatus() );

		//validate response content
		String responseContent = response.getEntity( String.class );
		assertTrue( "Expected number of entities were not updated", responseContent != null && responseContent.contains( "updated 4 entities") );
	}

	/**
	 * Convenience method to post a request to the specified path.
	 *
	 * @param path - path to post to
	 * @param requestData - JSON formatted request string
     * @return ClientResponse instance representing the response from the service
     */
	private ClientResponse postRequest( String path, String requestData ) {
		return webService.path( path ).type( "application/json").post(ClientResponse.class, requestData);
	}
}