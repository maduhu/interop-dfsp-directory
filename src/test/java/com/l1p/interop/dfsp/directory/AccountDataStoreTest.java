package com.l1p.interop.dfsp.directory;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Walter on 8/18/2016.
 */
public class AccountDataStoreTest {
	private AccountDataStore accountDataStore = null;
	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Before
	public void InitAccountDataStore(){
		accountDataStore = new AccountDataStore();
	}

	private Map<String, String> buildTestAccount(final String name) {
		Map<String,String> accountData = new HashMap<>();
		accountData.put("name",name);
		accountData.put("account",name);
		accountData.put("currency","USD");
		return accountData;
	}
	
	private String getTestURI(final String name){
		return "userdata.com/" + name;
	}

	@Test
	public void testAddValidAccountAndRetrievesIt() throws Exception {
		//Given
		String userName = "user1";
		String URI = getTestURI(userName);
		Map<String, String> accountData = buildTestAccount(userName);
		
		//When
		accountDataStore.addAccount(URI, accountData);
		Map<String,String> account = accountDataStore.getAccount(URI);
		
		//Then
		assertTrue("The getAccount method returned null for " + URI, account != null);
		assertEquals("The account inserted name was not returned",userName, account.get("name"));
	}

	@Test
	public void testaddAccountWithoutCurrencyFails() throws Exception {
		//Given
		String userName = "user2";
		String URI = getTestURI(userName);
		RuntimeException exception = null;
		Map<String, String> accountData = buildTestAccount(userName);
		accountData.remove("currency");
		
		//When
		try{
			accountDataStore.addAccount(URI, accountData);
		}catch(RuntimeException ex){
			exception = ex;
		}
		
		//Then
		assertTrue("With no currency property accountdatastore should have returned a RuntimeException saying there is a missing field.", exception != null && exception.getMessage().contains("Required field"));
	}
	
	@Test
	public void testaddAccountWithoutNameFails() throws Exception {
		//Given
		String userName = "user3";
		String URI = getTestURI(userName);
		RuntimeException exception = null;
		Map<String, String> accountData = buildTestAccount(userName);
		accountData.remove("name");
		
		//When
		try{
			accountDataStore.addAccount(URI, accountData);
		}catch(RuntimeException ex){
			exception = ex;
		}
		
		//Then
		assertTrue("With no name property accountdatastore should have returned a RuntimeException saying there is a missing field.", exception != null && exception.getMessage().contains("Required field"));
	}
	
	@Test
	public void testaddAccountWithoutAccountFails() throws Exception {
		//Given
		String userName = "user4";
		String URI = getTestURI(userName);
		RuntimeException exception = null;
		Map<String, String> accountData = buildTestAccount(userName);
		accountData.remove("account");
		
		//When
		try{
			accountDataStore.addAccount(URI, accountData);
		}catch(RuntimeException ex){
			exception = ex;
		}
		
		//Then
		assertTrue("With no account property accountdatastore should have returned a RuntimeException saying there is a missing field.", exception != null && exception.getMessage().contains("Required field"));
	}
	
	@Test
	public void testAddAccountsWithValidData() throws Exception{
		//Given
		String userName1 = "user5";
		String userName1URI = getTestURI(userName1);
		Map<String, String> accountData1 = buildTestAccount(userName1);
		accountData1.put("uri", userName1URI);
		
		String userName2 = "user6";
		String userName2URI = getTestURI(userName2);
		Map<String, String> accountData2 = buildTestAccount(userName2);
		accountData2.put("uri", getTestURI(userName2));
		
		List<Map<String, String>> listOfAccounts = new ArrayList<>();
		listOfAccounts.add(accountData1);
		listOfAccounts.add(accountData2);
		
		//When
		accountDataStore.addAccounts(listOfAccounts);
		Map<String, String> account1 = accountDataStore.getAccount(userName1URI);
		Map<String, String> account2 = accountDataStore.getAccount(userName2URI);
		
		//Then
		assertTrue("The getAccount method returned null for " + userName1URI, account1 != null);
		assertEquals("The account inserted name was not returned",userName1, account1.get("name"));
		

		assertTrue("The getAccount method returned null for " + userName2URI, account2 != null);
		assertEquals("The account inserted name was not returned",userName2, account2.get("name"));
	}
	
	@Test
	public void testAddAccountsWithInvalidDataFails() throws Exception{
		//Given
		String userName1 = "user7";
		String userName1URI = getTestURI(userName1);
		Map<String, String> accountData1 = buildTestAccount(userName1);
		accountData1.put("uri", userName1URI);
		accountData1.remove("account");
		
		List<Map<String, String>> listOfAccounts = new ArrayList<>();
		listOfAccounts.add(accountData1);
		
		RuntimeException exception = null;
		
		//When
		try{
			accountDataStore.addAccounts(listOfAccounts);
		}catch(RuntimeException ex){
			exception = ex;
		}
		
		//Then
		assertTrue("With no account property accountdatastore should have returned a RuntimeException saying there is a missing field.", exception != null && exception.getMessage().contains("Required field"));
	}
	
	@Test
	public void testAddAccountFailsWhenPressentedWithMoreFields() throws Exception{
		//Given
		RuntimeException exception = null;
		String userName1 = "user9";
		String userName1URI = getTestURI(userName1);
		Map<String, String> accountData1 = buildTestAccount(userName1);
		accountData1.put("addedProperty", "addedValue");
		
		//When
		try{
			accountDataStore.addAccount(userName1URI, accountData1);
		}catch(RuntimeException ex){
			exception = ex;
		}
		
		//Then
		assertTrue("Accountdatastore didnt fail when presented with extra properties", exception != null && exception.getMessage().contains("Account data contains unexpected number of fields"));
			
	}
}
