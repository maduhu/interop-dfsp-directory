package com.l1p.interop.dfsp.directory;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jettison.json.JSONObject;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mule.DefaultMuleEventContext;
import org.mule.api.MuleEvent;
import org.mule.api.MuleEventContext;
import org.mule.component.SimpleCallableJavaComponentTestCase;

public class AddAccountsProcessorTest extends SimpleCallableJavaComponentTestCase {

	private AddAccountsProcessor proc = null;
	private AccountDataStore store = null;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		store = createStoreData("test1");
		proc = new AddAccountsProcessor(store);
	}

	@After
	public void tearDown() throws Exception {
	}

	
	
	@Test
	public void testAddAccountsProcessor() {
		Map<String,Object> payload = createPayloadTestData("users");
		List<Map<String,String>> accountsToAdd = (List<Map<String,String>>)payload.get("users");
		store.addAccounts(accountsToAdd);
		
		// as long as the addAccounts does not thrown an exception, then we know we are good
		assertTrue(1==1);  
	}

	@Test
	public void testGetAccountStore() {
		AccountDataStore store = proc.getAccountStore();
		assertTrue("getAccountStore", store != null);
	}

	
	@Test
	public void testPayload() {
		Map<String,Object> payload = createPayloadTestData("bill");
		assertTrue("payload1 test", payload != null);
	}
	
	
	@Test
	public void testPayloadForUsers() {
		Map<String,Object> payload = createPayloadTestData("users");
		List<Map<String,String>> accountsToAdd = (List<Map<String,String>>)payload.get("users");
		assertTrue("payload2 test", accountsToAdd != null);
	}
	
	
	@Test
	public void testOnCall() throws Exception {
		Map<String,Object> payload = createPayloadTestData("users");
		MuleEvent event = getTestEvent(payload, muleContext);
		MuleEventContext context = new DefaultMuleEventContext(event);
		String response = (String) proc.onCall(context);
		
		System.out.println("response from OnMessage: " + response);

		JSONObject jObject = new JSONObject(response); 	// json
		JSONObject data    = jObject.getJSONObject("result"); 	// get data object
	        
		assertEquals("Updated 3 entities based on request", data.getString("message"));
		assertNotNull((String) jObject.get("id"));
		assertEquals((String) jObject.get("jsonrpc"), "2.0");
	}
	
	
	private AccountDataStore createStoreData(String accountName) {
		AccountDataStore store = new AccountDataStore();
		store.addAccount(accountName, createAccount("SomeName", "Some AccountName", "USD") );
		return store;
	}
	
	
	
	
	private Map<String,Object> createPayloadTestData(String name) {
		
		/*
		 * The payload we need to create is a Map.  The key is a string and by value is an array of 
		 * The payload/Map has to have an entry of "users", which is which is an List if Map of String, String.
		 */
		Map<String,Object> payload = new HashMap<String, Object>();
		
		List<Map<String,String>> accounts = new ArrayList<Map<String, String>>();
		accounts.add(createAccount("Name1", "AccountName1", "USD"));
		accounts.add(createAccount("Name2", "AccountName2", "USD"));
		accounts.add(createAccount("Name3", "AccountName3", "USD"));
		
		payload.put(name, accounts);		
		
		return payload;
	}
	
	
	private Map<String, String> createAccount(String name, String accountName, String currency) {
		Map<String, String> account = new HashMap<String, String>();
		account.put("name", name);
		account.put("account", accountName);
		account.put("currency", currency);
		
		return account;
	}

}
