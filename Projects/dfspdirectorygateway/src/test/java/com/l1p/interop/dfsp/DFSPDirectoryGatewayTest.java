package com.l1p.interop.dfsp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

	protected Logger logger = LoggerFactory.getLogger(getClass());
//
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
		webService = Client.create(config).resource( validHost );
	}

	@Test(timeout = 100000)
	public void testInvalidPathShouldReturn404() throws Exception {
		final String invalidPath="/path/shouldnt/exist";
		final String notJSON = "<BadRequest>This is not JSON</BadRequest>";

		logger.info("Posting event to web services");

		ClientResponse response = webService.path( invalidPath ).post(ClientResponse.class, notJSON);
		logger.info( "Received response from web service: " + response );

		assertEquals("Server did not respond with status 404 when presented with path " + invalidPath + " on port ", 404, response.getStatus() );

	}

	@Test(timeout = 100000)
	public void testValidRequestShouldReturnValidResponse() throws Exception {
		final String validPath="/api/directory/user/get";
		final String validJSON = "{\"jsonrpc\": \"2.0\",\"id\": \"incididunt eiusmod ut ex\",\"method\": \"directory.user.get\",\"params\": {\"userURI\": \"http://userdata.com/user\"}}";
		logger.info("Posting event to web services");

		ClientResponse response = webService.path( validPath ).type( "application/json").post(ClientResponse.class, validJSON);
		logger.info( "Received response from web service: " + response );

		assertEquals("Server did not respond with status 200 when presented with path " + validPath + " on port ", 200, response.getStatus() );
	}

	@Test(timeout = 100000)
	public void testValidAddUserRequestShouldReturnValidResponse() throws Exception {
		final String validPath="/api/directory/user/add";
		final String addUsersJSON = loadResourceAsString("sampleUserData.json");
		logger.info("Posting request to path " + validPath );

		ClientResponse response = webService.path( validPath ).type( "application/json").post(ClientResponse.class, addUsersJSON);
		logger.info( "Received response from web service: " + response );

		//validate response status is 200
		assertEquals("Server did not respond with status 200 when presented with path " + validPath + " on port ", 200, response.getStatus() );

		//validate response content
		String responseContent = response.getEntity( String.class );
		assertTrue( "Expected number of entities were not updated", responseContent != null && responseContent.contains( "updated 3 entities") );
	}
}