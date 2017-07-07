package com.l1p.interop.dfsp.directory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is a wrapper on a map of Accounts
 * 
 * Resolve possible issue with using the name Account vs User as it might
 * create issues in future URIs as well as general confusion to programmers
 *
 * Created by Bryan on 8/17/2016.
 */
public class AccountDataStore {

	private final Map<String, Map<String, String>> cache = new HashMap<String, Map<String, String>>();

	public synchronized void addAccounts(final List<Map<String, String>> accounts) {
		for (Map<String, String> nextAccount : accounts) {
			String uri = nextAccount.remove("uri");
			addAccount(uri, nextAccount);
		}
	}

	public synchronized void addAccount(final String uri, final Map<String, String> account) {
		validateAccountFields(account);
		cache.put(uri, account);
	}

	private void validateAccountFields(Map<String, String> account) {
		if (account.get("name") == null) {
			throw new ValidateAccountException("Required field \"name\" not found in account data");
		} else if (account.get("account") == null) {
			throw new ValidateAccountException("Required field \"account\" not found in account data");
		} else if (account.get("currency") == null) {
			throw new ValidateAccountException("Required field \"currency\" not found in account data");
		} else if (account.size() != 3) {
			throw new ValidateAccountException(
					"Account data contains unexpected number of fields, expected 3, contains " + account.size());
		}
	}

	/**
	 *
	 * @param uri - userUri to look up
	 * @return Map containing account or null if the provided uri is not mapped
	 */
	public synchronized Map<String, String> getAccount(String uri) {
		return cache.get(uri);
	}

}
