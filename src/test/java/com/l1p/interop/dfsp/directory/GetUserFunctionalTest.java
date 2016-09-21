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
import com.l1p.interop.JsonTransformer;
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

	private final String getUserPath="/directory/v1/user/get";
	private final String addUserPath="/directory/v1/user/add";
	private final String serviceHost = "http://localhost:8081";

	protected Logger logger = LoggerFactory.getLogger(getClass());

	WebResource webService;

	@Override
	protected String getConfigResources() {
		return "test-resources.xml,interop-dfsp-directory-api.xml,interop-dfsp-directory.xml,mock-interop-dfsp-directory-api.xml,mock-interop-dfsp-directory.xml";
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

		ClientResponse clientResponse = postRequest(invalidPath, notJSON);
		validateResponse( "InvalidPathShouldReturn404", clientResponse, 404, "Resource not found");
	}

	@Test
	public void testValidGetUserRequestShouldReturnValidResponse() throws Exception {
		//populate DFSPDirectoryGateway with test data
		try {
			final String addUsersJSON = loadResourceAsString("testData/validAccountData.json");
			ClientResponse addUsersResponse = webService.path(addUserPath).type("application/json").post(ClientResponse.class, addUsersJSON);
			assertEquals("Server did not respond with status 200 for addUsers when presented with path " + addUserPath, 200, addUsersResponse.getStatus());
		} catch( Exception e ) {
			fail( "Loading test account data to interop-dfsp-directory via " + addUserPath + " produced an unexpected exception: " + e.getMessage() );
		}

		Map<String,String> paramMap = new HashMap<String,String>();

		logger.info("Posting events to web services");

		//test retrieving info for chrisg
		paramMap.put( "userURI", "http://centraldirectory.com/griffin" );
		final String validRequest1 = new JsonRpcRequest( "id1", "directory.user.get", paramMap ).toJSONString();
		ClientResponse clientResponse = postRequest( getUserPath, validRequest1);
		verifyGetUserResponse( "validRequest1", clientResponse, 200, "id1", "Chris Griffin", "http://receivingdfsp.com/griffin_12345", "USD" );

		//test retrieving info for magoo
		paramMap.put( "userURI", "http://centraldirectory.com/magoo" );
		final String validRequest2 = new JsonRpcRequest( "id2", "directory.user.get", paramMap ).toJSONString();
		clientResponse = postRequest( getUserPath, validRequest2);
		verifyGetUserResponse( "validRequest2", clientResponse, 200, "id2", "Mr. Magoo", "http://receivingdfsp.com/magoo_12345", "USD" );

		//test retrieving info for sonof
		paramMap.put( "userURI", "http://centraldirectory.com/yosemite" );
		final String validRequest3 = new JsonRpcRequest( "id3", "directory.user.get", paramMap ).toJSONString();
		clientResponse = postRequest( getUserPath, validRequest3);
		verifyGetUserResponse( "validRequest3", clientResponse, 200, "id3", "Yosemite Sam", "http://receivingdfsp.com/yosemite_12345", "INR" );

		//test retrieving info for walt
		paramMap.put( "userURI", "http://centraldirectory.com/mitty" );
		final String validRequest4 = new JsonRpcRequest( "id4", "directory.user.get", paramMap ).toJSONString();
		clientResponse = postRequest( getUserPath, validRequest4);
		verifyGetUserResponse( "validRequest4", clientResponse, 200, "id4", "Walter Mitty", "http://receivingdfsp.com/mitty_12345", "ARS" );

		//test retrieving info for a missing account
		paramMap.put( "userURI", "userdata.com/missing" );
		final String missingAccountRequest = new JsonRpcRequest( "id5", "directory.user.get", paramMap ).toJSONString();
		ClientResponse missingAccountResponse = postRequest( getUserPath, missingAccountRequest);
		assertEquals("Server did not respond with status 200 for missingAccount when presented with path " + getUserPath, 200, missingAccountResponse.getStatus() );
		String expectedMissingAccountContent = "Account not found for userURI=userdata.com/missing";
		String missingAccountResponseContent = missingAccountResponse.getEntity( String.class );
		assertTrue( "Response for missingAccount did not contain expected text '" + expectedMissingAccountContent + "': " + missingAccountResponseContent, missingAccountResponseContent != null && missingAccountResponseContent.contains( expectedMissingAccountContent ));
		
		//test with empty params
		paramMap.clear();
		final String badRequest = new JsonRpcRequest( "id5", "directory.user.get", paramMap ).toJSONString();
		ClientResponse responseForBadRequest = postRequest( getUserPath, badRequest);
		//assertEquals("Server did not respond with status 400 for badRequest when presented with path " + getUserPath, 400, responseForBadRequest.getStatus() );
		verifyL1PErrorResponse("GetUserNoParams", responseForBadRequest, 400, "500", "bad request");
	}

	/**
	 * Utility method to verify the content of a response from a call to /directory/user/get
	 *
	 * @param testIdentifier - phrase than identifies the test for debugging purposes
	 * @param clientResponse - ClientResponse instance to validate
	 * @param expectedStatus - expected status code
	 * @param expectedId - expected id value
	 * @param expectedName - expected name value
	 * @param expectedAccount - expected account value
	 * @param expectedCurrency - expected currency value
     * @throws Exception
     */
	private void verifyGetUserResponse( String testIdentifier, ClientResponse clientResponse, int expectedStatus, String expectedId, String expectedName, String expectedAccount, String expectedCurrency ) throws Exception {
		//verify status
		assertEquals( testIdentifier + ": Server did not respond with status " + expectedStatus, expectedStatus, clientResponse.getStatus() );

		//read response content as string
		String responseContent = null;
		try {
			responseContent = clientResponse.getEntity(String.class);
		} catch ( Exception e ) {
			fail( testIdentifier + ": parsing client response content produced an unexpected exception: " + e.getMessage() );
		}

		//convert response content from JSON string to Map
		Map<String,Object> header = null;
		try {
			// convert JSON string to Map
			header = JsonTransformer.stringToMap(responseContent);
		} catch( Exception e ) {
			fail( testIdentifier + ": conversion of client response content to a map produced an unexpected exception: " + e.getMessage() );
		}

		//validate content of map
		assertTrue( testIdentifier + ": Header map was null", header != null );
		assertTrue( testIdentifier + ": Size of header map was incorrect, expected 3, received " + header.size(), header.size() == 3 );
		assertEquals( testIdentifier + ": Header map did not contain correct data for jsonrpc element", "2.0", header.get( "jsonrpc" ) );
		//This needs discussion - there is no ID in the input, if we are arbitrarily generating it then we need to verify against that
		//assertEquals( testIdentifier + ": Header map did not contain correct data for id element", expectedId, header.get( "id" ) );
		Map<String, Object> result = (Map<String, Object>)header.get( "result" );
		assertTrue( testIdentifier + ": Result map was null", result != null );
		assertTrue( testIdentifier + ": Size of result map was incorrect, expected 3, received " + result.size(), result.size() == 3 );
		assertEquals( testIdentifier + ": Result map did not contain correct data for name element", expectedName, result.get( "name" ) );
		assertEquals( testIdentifier + ": Result map did not contain correct data for account element", expectedAccount, result.get( "account" ) );
		assertEquals( testIdentifier + ": Result map did not contain correct data for currency element", expectedCurrency, result.get( "currency" ) );
	}

	/**
	 * Utility method to verify the content of L1P ErrorResponse from a call to /directory/user/get
	 *
	 * @param testIdentifier - phrase than identifies the test for debugging purposes
	 * @param clientResponse - ClientResponse instance to validate
	 * @param expectedStatus - expected HTTP status code
	 * @param expectedCode - expected error code
	 * @param expectedMessage - expected error message
	 * @throws Exception
     */
	private void verifyL1PErrorResponse( String testIdentifier, ClientResponse clientResponse, int expectedStatus, String expectedCode, String expectedMessage ) {
		//verify status
		assertEquals( testIdentifier + ": Server did not respond with status " + expectedStatus, expectedStatus, clientResponse.getStatus() );

		//read response content as string
		String responseContent = null;
		try {
			responseContent = clientResponse.getEntity(String.class);
		} catch ( Exception e ) {
			fail( testIdentifier + ": parsing client response content produced an unexpected exception: " + e.getMessage() );
		}

		//convert response content from JSON string to Map
		Map<String,Object> header = null;
		try {
			// convert JSON string to Map
			header = JsonTransformer.stringToMap(responseContent);
		} catch( Exception e ) {
			fail( testIdentifier + ": conversion of client response content to a map produced an unexpected exception: " + e.getMessage() );
		}

		//validate content of map
		assertTrue( testIdentifier + ": Header map was null", header != null );
		assertTrue( testIdentifier + ": Size of header map was incorrect, expected 2, received " + header.size(), (header.size() == 1 || header.size() == 2) );
		
		Map<String, Object> result = (Map<String, Object>)header.get( "error" );
		assertTrue( testIdentifier + ": Result map was null", result != null );
		assertTrue( testIdentifier + ": Size of result map was incorrect, expected 2, received " + result.size(), result.size() == 2 );
		assertTrue( testIdentifier + ": Error id not as expected: ", result.get("id").toString().contains("Bad request") );
		assertTrue( testIdentifier + ": Error message not as expected: ", result.get("message").toString().contains("object has missing required properties ([\"userURI\"])") );
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
		assertTrue( "Expected number of entities were not updated", responseContent != null && responseContent.contains( "Updated 4 entities") );
	}
	
	/**
	 * Utility method to verify the content of a response from a call given the criteria
	 *
	 * @param testName - name that identifies the test for debugging purposes
	 * @param clientResponse - ClientResponse instance to validate
	 * @param expectedStatus - expected status code
	 * @param expectedContent - string expected to be present in the result
     * @throws Exception
     */
	private void validateResponse( String testName, ClientResponse clientResponse, int expectedStatus, String expectedContent ) throws Exception {

		assertEquals( testName + ": Did not receive status 200", expectedStatus, clientResponse.getStatus());
		String responseContent = null;
		try {
			responseContent = clientResponse.getEntity(String.class);
		} catch ( Exception e ) {
			fail( testName + ": parsing client response content produced an unexpected exception: " + e.getMessage() );
		}

		assertTrue( testName + ": received unexpected response", responseContent.contains( expectedContent ) );

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