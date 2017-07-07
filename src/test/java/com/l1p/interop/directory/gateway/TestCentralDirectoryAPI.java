package com.l1p.interop.directory.gateway;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
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
	
	@Rule
	public WireMockRule mockDfspApi = new WireMockRule(8091);
	
	@Rule
	public WireMockRule mockCentralFraudSharing = new WireMockRule(8092);

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
	public void testAddResouce() throws Exception {

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
	
	@Test
	public void testGetResouce() throws Exception {

		String centralDirGetResourceMockResponse = loadResourceAsString("test_data/centralDirGetResourceMockResponse.json");
		mockCentralDirectory.stubFor(get(urlEqualTo("/resources?identifier=eur%3A123"))
				.willReturn(aResponse().withBody(centralDirGetResourceMockResponse)));
		
		String dfspReceiverMockResponse = loadResourceAsString("test_data/dfspReceiverMockResponse.json");
		mockDfspApi.stubFor(get(urlEqualTo("/scheme/adapter/v1/receivers/123"))
				.willReturn(aResponse().withBody(dfspReceiverMockResponse)));
		
		String centralFraudSharingRequest = "{\"identifier\":\"123\",\"identifierType\":\"eur\"}";
		String centralFraudSharingMockResponse = loadResourceAsString("test_data/centralFraudSharingMockResponse.json");
		mockCentralFraudSharing.stubFor(post(urlEqualTo("/score/user")).withRequestBody(com.github.tomakehurst.wiremock.client.WireMock.equalTo(centralFraudSharingRequest))
				.willReturn(aResponse().withBody(centralFraudSharingMockResponse)));
		
		given()
			.contentType("application/json")
			.auth().basic("key", "secret")
			.queryParam("identifier", "123")
			.queryParam("identifierType", "eur")
		.when()
			.get("http://localhost:8088/directory/gateway/v1/resources")
		.then()
			.statusCode(200);
	}

}
