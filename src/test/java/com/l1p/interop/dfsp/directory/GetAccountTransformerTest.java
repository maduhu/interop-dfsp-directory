package com.l1p.interop.dfsp.directory;


import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.mule.api.transport.PropertyScope;

import com.l1p.interop.TestMuleMessageImpl;

public class GetAccountTransformerTest {
	
	@Test
	public void testWithUriAndAccont() throws Exception {

		
		
		System.out.println("starting");
		
		/* Prepare inbound message. */
		TestMuleMessageImpl muleMessage = new TestMuleMessageImpl();
        muleMessage.setPayload("Hi there");
        muleMessage.setProperty("id", "some name", PropertyScope.SESSION);
        
        Object payload = buildTransformerData();
        
        
        muleMessage.setPayload(payload);
        
        Object x = muleMessage.getPayload();
        
        GetAccountTransformer transformer = new GetAccountTransformer( createStoreData("userURI") );
        transformer.transformMessage(muleMessage, "UTF-8");

        
        assertTrue(1 == 1);
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
	
	
	private Map<String, Object> buildTransformerData() {
		
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		Map<String, Object> uriMap = new HashMap<String, Object>();
		
		uriMap.put("userURI", "userURI");
		paramsMap.put("params", uriMap);
		
		return paramsMap;
	}

}
