package com.l1p.interop.dfsp.directory;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.jettison.json.JSONObject;
import org.junit.Test;
import org.mule.api.MuleEvent;
import org.mule.api.MuleMessage;
import org.mule.api.transport.PropertyScope;
import org.mule.component.SimpleCallableJavaComponentTestCase;

public class GetAccountTransformerTest extends SimpleCallableJavaComponentTestCase {
	
	@Test
	public void testWithUriAndAccunt() throws Exception {

		/* Prepare inbound message. */
		Object payload = buildTransformerData("userURI", "userURI");
		MuleEvent event = getTestEvent(payload, muleContext);
		MuleMessage muleMessage = event.getMessage();
        muleMessage.setProperty("id", "some name", PropertyScope.SESSION);
        
        GetAccountTransformer transformer = new GetAccountTransformer( createStoreData("userURI") );
        String response = (String) transformer.transformMessage(muleMessage, "UTF-8");

        JSONObject jObject  = new JSONObject(response); // json
        JSONObject data = jObject.getJSONObject("result"); // get data object
        
        assertEquals("name of account", data.getString("name"));
        assertEquals("USD", data.getString("currency"));
        assertEquals("some account name", data.getString("account"));
	}
	
	
	@Test
	public void testWithUriAndNoAccount() throws Exception {

		/* Prepare inbound message. */
		Object payload = buildTransformerData("userURI", "userURI");
		MuleEvent event = getTestEvent(payload, muleContext);
		MuleMessage muleMessage = event.getMessage();
        muleMessage.setProperty("id", "some name", PropertyScope.SESSION);
        
        GetAccountTransformer transformer = new GetAccountTransformer( createStoreData("userURIX") );
        String response = (String) transformer.transformMessage(muleMessage, "UTF-8");

        JSONObject jObject  = new JSONObject(response); // json response
        
        JSONObject data;
		try {
			data = jObject.getJSONObject("error");
			assertEquals( "Account not found for userURI=userURI", ((String) data.get("message"))  );
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.toString());
		}
        
	}
	
	
	@Test
	public void testWithNoUriWithAccount() throws Exception {

		/* Prepare inbound message. */
		Object payload = buildTransformerData("userURIx", "userURIx");
		MuleEvent event = getTestEvent(payload, muleContext);
		MuleMessage muleMessage = event.getMessage();
		
        muleMessage.setProperty("id", "some name", PropertyScope.SESSION);
        
        GetAccountTransformer transformer = new GetAccountTransformer( createStoreData("userURI") );
        String response = (String) transformer.transformMessage(muleMessage, "UTF-8");

        JSONObject jObject  = new JSONObject(response); // json
        
        JSONObject data;
		try {
			data = jObject.getJSONObject("error");
			assertEquals( "Missing required request parameter 'userURI' ", ((String) data.get("message"))  );
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.toString());
		}
        
	}
	
	
	private AccountDataStore createStoreData(String accountName) {
		AccountDataStore store = new AccountDataStore();
		
		Map<String, String> account = new HashMap<String, String>();
		account.put("name", "name of account");
		account.put("account", "some account name");
		account.put("currency", "USD");
		store.addAccount(accountName, account);
		return store;
	}
	
	
	private Map<String, Object> buildTransformerData(String keyName, String keyValue) {
		
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		Map<String, Object> uriMap = new HashMap<String, Object>();
		
		uriMap.put(keyName, keyValue);
		paramsMap.put("params", uriMap);
		
		return paramsMap;
	}

}
