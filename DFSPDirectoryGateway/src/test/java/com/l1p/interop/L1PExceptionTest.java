package com.l1p.interop;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.l1p.interop.L1PErrorResponse;
/**
 * Created by Bryan on 8/18/2016.
 */
public class L1PExceptionTest {

	@Test
	public void throwingAL1PExceptionShouldHaveAValidJsonStringValueTest() throws Exception {
		L1PErrorResponse l1pException = new L1PErrorResponse("", 12345, "An L1PException has been thrown", "type", new Exception());
		JsonParser parser = new JsonParser();
		JsonObject obj = parser.parse(l1pException.toString()).getAsJsonObject();
		String code = obj.get("code").getAsString();
		assertTrue("Code was not a valid value, expected 12345", code == "12345");
		assertTrue("Result jsonString was null", l1pException.toString() != null);
	}

}
