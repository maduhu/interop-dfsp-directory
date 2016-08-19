package com.l1p.interop.dfsp.directory;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Bryan on 8/17/2016.
 */
public class AccountDataStoreTest {
	private AccountDataStore accountDataStore = null;
	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Before
	public void InitAccountDataStore(){
		accountDataStore = new AccountDataStore();
	}
	
	private void addNewTestAccount(final String name){
		String actualName = name;
		
		Map<String,String> accountData = new HashMap<String, String>();
		accountData.put("name",name);
		accountData.put("account",actualName);
		accountData.put("currency","USD");
		
		accountDataStore.addAccount(getTestURI(actualName), accountData);
	}
	
	private String getTestURI(final String name){
		return "userdata.com/" + name;
	}


	@Test
	public void testaddAccountSetsANewAccountAndRetrievesIt() throws Exception {
		//Given
		String userName = "walt";
		String URI = getTestURI(userName);
		
		//When
		addNewTestAccount(userName);
		Map<String,String> account = accountDataStore.getAccount(URI);
		
		//Then
		assertTrue("The getAccount method returned null for " + URI, account != null);
		assertEquals("The account inserted name was not returned",userName, account.get("name"));
	}

}
