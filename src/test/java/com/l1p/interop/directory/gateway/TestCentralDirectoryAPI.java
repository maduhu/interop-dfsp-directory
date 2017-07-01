package com.l1p.interop.directory.gateway;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.mule.tck.junit4.FunctionalTestCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.tomakehurst.wiremock.junit.WireMockRule;

public class TestCentralDirectoryAPI extends FunctionalTestCase {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Rule
	public WireMockRule mockCentralDirectory = new WireMockRule(8090);

	@Override
	protected String getConfigResources() {
		return "test-resources.xml,central-directory-api.xml,central-directory.xml";
	}

	@BeforeClass
	public static void initEnv() {
		System.setProperty("MULE_ENV", "test");
	}

	@Test
	public void testRegisterDFSP() throws Exception {

		String centralDirAddDFSPMockResponse = loadResourceAsString("test_data/centralDirAddDFSPMockResponse.json");
		mockCentralDirectory.stubFor(post(urlMatching("/commands/register"))
				.willReturn(aResponse().withBody(centralDirAddDFSPMockResponse)));
		
		String centralDirAddDFSPRequest = loadResourceAsString("test_data/centralDirAddDFSPRequest.json");
		given()
			.contentType("application/json")
			.auth().basic("admin", "admin")
			.body(centralDirAddDFSPRequest)
		.when()
			.post("http://localhost:8088/directory/gateway/v1/commands/register")
		.then()
			.statusCode(200)
			.body("name",equalTo("The first DFSP"))
			.body("shortName", equalTo("dfsp1"))
			.body("providerUrl", equalTo("http://url.com"))
			.body("schemeCode", equalTo("001"))
			.body("dfspCode", equalTo("123"))
			.body("key", equalTo("dfsp_key"))
			.body("secret",equalTo("dfsp_secret"));
	}
	
	@Test
	public void testAddResource() throws Exception {

		String centralDirAddResourceMockResponse = loadResourceAsString("test_data/centralDirAddResourceMockResponse.json");
		mockCentralDirectory.stubFor(post(urlMatching("/resources"))
				.willReturn(aResponse().withBody(centralDirAddResourceMockResponse)));
		
		String centralDirAddResourceRequest = loadResourceAsString("test_data/centralDirAddResourceRequest.json");
		given()
			.contentType("application/json")
			.auth().basic("key", "secret")
			.body(centralDirAddResourceRequest)
		.when()
			.post("http://localhost:8088/directory/gateway/v1/resources")
		.then()
			.statusCode(200)
			.body("name",equalTo("The first DFSP"))
			.body("shortName", equalTo("dfsp1"))
			.body("providerUrl", equalTo("localhost:8088/scheme/adapter/v1"))
			.body("preferred", equalTo(true))
			.body("registered", equalTo(true));
	}

}
