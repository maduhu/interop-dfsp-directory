package com.l1p.interop.dfsp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

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

	private final String getUserPath="/api/directory/user/get";
	private final String addUserPath="/api/directory/user/add";

	protected Logger logger = LoggerFactory.getLogger(getClass());

	WebResource webService;
	private final String validHost = "http://localhost:8081";

	@Override
	protected String getConfigResources() {
		return "api.xml";
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
		webService = Client.create(config).resource(validHost);
	}

	@Test(timeout = 100000)
	public void testInvalidPathShouldReturn404() throws Exception {
		final String invalidPath = "path/shouldnt/exist";
		final String notJSON = "<BadRequest>This is not JSON</BadRequest>";

		logger.info("Posting event to web services");

		ClientResponse response = webService.path( invalidPath ).post(ClientResponse.class, notJSON);
		logger.info( "Received response from web service: " + response );

		assertEquals("Server did not respond with status 404 when presented with path " + invalidPath + " on port ", 404, response.getStatus() );

	}

	@Test(timeout = 100000)
	public void testValidGetUserRequestShouldReturnValidResponse() throws Exception {
		//todo - load in account data
		final String addUsersJSON = loadResourceAsString("validAccountData.json");
		ClientResponse addUsersResponse = webService.path( addUserPath ).type( "application/json").post(ClientResponse.class, addUsersJSON);
		assertEquals("Server did not respond with status 200 for addUsers when presented with path " + addUserPath, 200, addUsersResponse.getStatus() );

		final String account1JSON = "{\"jsonrpc\": \"2.0\",\"id\": \"incididunt eiusmod ut ex\",\"method\": \"directory.user.get\",\"params\": {\"userURI\": \"userdata.com/chrisg\"}}";
		final String account2JSON = "{\"jsonrpc\": \"2.0\",\"id\": \"incididunt eiusmod ut ex\",\"method\": \"directory.user.get\",\"params\": {\"userURI\": \"userdata.com/magoo\"}}";
		final String account3JSON = "{\"jsonrpc\": \"2.0\",\"id\": \"incididunt eiusmod ut ex\",\"method\": \"directory.user.get\",\"params\": {\"userURI\": \"userdata.com/sonof\"}}";
		final String account4JSON = "{\"jsonrpc\": \"2.0\",\"id\": \"incididunt eiusmod ut ex\",\"method\": \"directory.user.get\",\"params\": {\"userURI\": \"userdata.com/mitty\"}}";
		final String missingAccountJSON = "{\"jsonrpc\": \"2.0\",\"id\": \"incididunt eiusmod ut ex\",\"method\": \"directory.user.get\",\"params\": {\"userURI\": \"userdata.com/missing\"}}";
		logger.info("Posting event to web services");

		ClientResponse account1Response = webService.path( getUserPath ).type( "application/json").post(ClientResponse.class, account1JSON);
		assertEquals("Server did not respond with status 200 for account1 when presented with path " + getUserPath, 200, account1Response.getStatus() );
		String account1ResponseContent = account1Response.getEntity( String.class );
		assertTrue( "Response for account1 did not contain expected name chrisg: " + account1ResponseContent, account1ResponseContent != null && account1ResponseContent.contains( "chrisg" ));

		ClientResponse account2Response = webService.path( getUserPath ).type( "application/json").post(ClientResponse.class, account2JSON);
		assertEquals("Server did not respond with status 200 for account2 when presented with path " + getUserPath, 200, account2Response.getStatus() );
		String account2ResponseContent = account2Response.getEntity( String.class );
		assertTrue( "Response for account2 did not contain expected name magoo: " + account2ResponseContent, account2ResponseContent != null && account2ResponseContent.contains( "magoo" ));

		ClientResponse account3Response = webService.path( getUserPath ).type( "application/json").post(ClientResponse.class, account3JSON);
		assertEquals("Server did not respond with status 200 for account3 when presented with path " + getUserPath, 200, account3Response.getStatus() );
		String account3ResponseContent = account3Response.getEntity( String.class );
		assertTrue( "Response for account3 did not contain expected name sonof: " + account3ResponseContent, account3ResponseContent != null && account3ResponseContent.contains( "sonof" ));

		ClientResponse account4Response = webService.path( getUserPath ).type( "application/json").post(ClientResponse.class, account4JSON);
		assertEquals("Server did not respond with status 200 for account4 when presented with path " + getUserPath, 200, account4Response.getStatus() );
		String account4ResponseContent = account4Response.getEntity( String.class );
		assertTrue( "Response for account4 did not contain expected name mitty: " + account4ResponseContent, account4ResponseContent != null && account4ResponseContent.contains( "mitty" ));

		ClientResponse missingAccountResponse = webService.path( getUserPath ).type( "application/json").post(ClientResponse.class, missingAccountJSON);
		assertEquals("Server did not respond with status 500 for missingAccount when presented with path " + getUserPath, 500, missingAccountResponse.getStatus() );
		String expectedMissingAccountContent = "Account not found for userURI=userdata.com/missing";
		String missingAccountResponseContent = missingAccountResponse.getEntity( String.class );
		assertTrue( "Response for missingAccount did not contain expected text '" + expectedMissingAccountContent + "': " + missingAccountResponseContent, missingAccountResponseContent != null && missingAccountResponseContent.contains( expectedMissingAccountContent ));
	}

	@Test(timeout = 100000)
	public void testValidAddUserRequestShouldReturnValidResponse() throws Exception {
		final String addUsersJSON = loadResourceAsString("validAccountData.json");
		logger.info("Posting request to path " + addUserPath );

		ClientResponse response = webService.path( addUserPath ).type( "application/json").post(ClientResponse.class, addUsersJSON);
		logger.info( "Received response from web service: " + response );

		//validate response status is 200
		assertEquals("Server did not respond with status 200 when presented with path " + addUserPath + " on port ", 200, response.getStatus() );

		//validate response content
		String responseContent = response.getEntity( String.class );
		assertTrue( "Expected number of entities were not updated", responseContent != null && responseContent.contains( "updated 4 entities") );
	}


}