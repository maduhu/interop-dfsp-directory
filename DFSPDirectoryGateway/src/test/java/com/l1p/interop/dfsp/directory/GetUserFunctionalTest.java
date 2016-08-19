package com.l1p.interop.dfsp.directory;

import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.Map;

import com.l1p.interop.JsonRpcRequest;
import com.l1p.interop.JsonMapStringTransformer;
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

public class GetUserFunctionalTest extends FunctionalTestCase {

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

	@Test
	public void testInvalidPathShouldReturn404() throws Exception {
		final String invalidPath = "path/shouldnt/exist";
		final String notJSON = "<BadRequest>This is not JSON</BadRequest>";

		logger.info("Posting event to web services");

		ClientResponse response = postRequest(invalidPath, notJSON);
		logger.info( "Received response from web service: " + response );

		assertEquals("Server did not respond with status 404 when presented with path " + invalidPath + " on port ", 404, response.getStatus() );

	}

	@Test
	public void testValidGetUserRequestShouldReturnValidResponse() throws Exception {
		//populate DFSPDirectoryGateway with test data
		try {
			final String addUsersJSON = loadResourceAsString("testData/validAccountData.json");
			ClientResponse addUsersResponse = webService.path(addUserPath).type("application/json").post(ClientResponse.class, addUsersJSON);
			assertEquals("Server did not respond with status 200 for addUsers when presented with path " + addUserPath, 200, addUsersResponse.getStatus());
		} catch( Exception e ) {
			fail( "Loading test account data to DFSPDirectoryGateway via " + addUserPath + " produced an unexpected exception: " + e.getMessage() );
		}

		Map<String,String> paramMap = new HashMap<String,String>();

		logger.info("Posting events to web services");

		//test retrieving info for chrisg
		paramMap.put( "userURI", "userdata.com/chrisg" );
		final String validRequest1 = new JsonRpcRequest( "id1", "directory.user.get", paramMap ).toJSONString();
		ClientResponse clientResponse = postRequest( getUserPath, validRequest1);
		verifyGetUserResponse( "validRequest1", clientResponse, 200, "id1", "chrisg", "chrisg_12345", "USD" );

		//test retrieving info for magoo
		paramMap.put( "userURI", "userdata.com/magoo" );
		final String validRequest2 = new JsonRpcRequest( "id2", "directory.user.get", paramMap ).toJSONString();
		clientResponse = postRequest( getUserPath, validRequest2);
		verifyGetUserResponse( "validRequest2", clientResponse, 200, "id2", "magoo", "magoo_12345", "USD" );

		//test retrieving info for sonof
		paramMap.put( "userURI", "userdata.com/sonof" );
		final String validRequest3 = new JsonRpcRequest( "id3", "directory.user.get", paramMap ).toJSONString();
		clientResponse = postRequest( getUserPath, validRequest3);
		verifyGetUserResponse( "validRequest3", clientResponse, 200, "id3", "sonof", "sonof_12345", "INR" );

		//test retrieving info for walt
		paramMap.put( "userURI", "userdata.com/walt" );
		final String validRequest4 = new JsonRpcRequest( "id4", "directory.user.get", paramMap ).toJSONString();
		clientResponse = postRequest( getUserPath, validRequest4);
		verifyGetUserResponse( "validRequest4", clientResponse, 200, "id4", "walt", "walt_12345", "ARS" );


		//TODO: make the code below use verifyL1PErrorResponse and finish implementing that method

		//test retrieving info for a missing account
		paramMap.put( "userURI", "userdata.com/missing" );
		final String missingAccountRequest = new JsonRpcRequest( "id5", "directory.user.get", paramMap ).toJSONString();
		ClientResponse missingAccountResponse = postRequest( getUserPath, missingAccountRequest);
		assertEquals("Server did not respond with status 500 for missingAccount when presented with path " + getUserPath, 500, missingAccountResponse.getStatus() );
		String expectedMissingAccountContent = "Account not found for userURI=userdata.com/missing";
		String missingAccountResponseContent = missingAccountResponse.getEntity( String.class );
		assertTrue( "Response for missingAccount did not contain expected text '" + expectedMissingAccountContent + "': " + missingAccountResponseContent, missingAccountResponseContent != null && missingAccountResponseContent.contains( expectedMissingAccountContent ));
	}

	/**
	 * Utility method to verify the content of a response from a call to /directory/user/get
	 *
	 * @param testIdentidier - phrase than identifies the test for debugging purposes
	 * @param clientResponse - ClientResponse instance to validate
	 * @param expectedStatus - expected status code
	 * @param expectedId - expected id value
	 * @param expectedName - expected name value
	 * @param expectedAccount - expected account value
	 * @param expectedCurrency - expected currency value
     * @throws Exception
     */
	private void verifyGetUserResponse( String testIdentidier, ClientResponse clientResponse, int expectedStatus, String expectedId, String expectedName, String expectedAccount, String expectedCurrency ) throws Exception {
		//verify status
		assertEquals( testIdentidier + ": Server did not respond with status " + expectedStatus, expectedStatus, clientResponse.getStatus() );

		//read response content as string
		String responseContent = null;
		try {
			responseContent = clientResponse.getEntity(String.class);
		} catch ( Exception e ) {
			fail( testIdentidier + ": parsing client response content produced an unexpected exception: " + e.getMessage() );
		}

		//convert response content from JSON string to Map
		Map header = null;
		try {
			// convert JSON string to Map
			header = JsonMapStringTransformer.stringToMap(responseContent);
		} catch( Exception e ) {
			fail( testIdentidier + ": conversion of client response content to a map produced an unexpected exception: " + e.getMessage() );
		}

		//validate content of map
		assertTrue( testIdentidier + ": Header map was null", header != null );
		assertTrue( testIdentidier + ": Size of header map was incorrect, expected 3, received " + header.size(), header.size() == 3 );
		assertEquals( testIdentidier + ": Header map did not contain correct data for jsonrpc element", "2.0", header.get( "jsonrpc" ) );
		assertEquals( testIdentidier + ": Header map did not contain correct data for id element", expectedId, header.get( "id" ) );
		Map result = (Map)header.get( "result" );
		assertTrue( testIdentidier + ": Result map was null", result != null );
		assertTrue( testIdentidier + ": Size of result map was incorrect, expected 3, received " + result.size(), result.size() == 3 );
		assertEquals( testIdentidier + ": Result map did not contain correct data for name element", expectedName, result.get( "name" ) );
		assertEquals( testIdentidier + ": Result map did not contain correct data for account element", expectedAccount, result.get( "account" ) );
		assertEquals( testIdentidier + ": Result map did not contain correct data for currency element", expectedCurrency, result.get( "currency" ) );
	}

	@Test
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