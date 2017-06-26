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
		return "test-resources.xml,scheme-adapter-api.xml,scheme-adapter.xml";
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

		given()
			.contentType("application/json")
			.queryParam("receiver", "localhost:8090/scheme/adapter/v1/receivers/123456").
		when()
			.post("http://localhost:8088/directory/gateway/v1/commads/register")
		.then()
			.statusCode(200)
			.body("name",equalTo("The first DFSP"))
			.body("shortName", equalTo("dfsp1"))
			.body("url", equalTo("http://url.com"))
			.body("schemeCode", equalTo("001"))
			.body("dfspCode", equalTo("123"))
			.body("key", equalTo("dfsp_key"))
			.body("secret",equalTo("dfsp_secret"));
	}

}
