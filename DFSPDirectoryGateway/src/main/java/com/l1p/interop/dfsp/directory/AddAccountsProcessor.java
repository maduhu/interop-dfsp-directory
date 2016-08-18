package com.l1p.interop.dfsp.directory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mule.api.MuleEventContext;
import org.mule.api.lifecycle.Callable;

import java.util.List;
import java.util.Map;

/**
 * Created by Bryan on 8/17/2016.
 */
public class AddAccountsProcessor implements Callable {
    private static final Logger logger = LogManager.getLogger(AddAccountsProcessor.class);
    private final AccountDataStore accountStore;

    public AddAccountsProcessor( AccountDataStore accountStore ) {
        this.accountStore = accountStore;
    }

    public AccountDataStore getAccountStore() {
        return accountStore;
    }

    public Object onCall(MuleEventContext muleEventContext) throws Exception {

        Object payload = muleEventContext.getMessage().getPayload();

        //make sure payload 1) isnt null 2) is a map and throw smart exception if not

        List<Map<String,String>> accountsToAdd = (List<Map<String,String>>)((Map)payload).get("users");
        logger.info( "Received request to add " + accountsToAdd.size() + " accounts" );

        accountStore.addAccounts( accountsToAdd );
        logger.info( "Added " + accountsToAdd.size() + " accounts" );

        return "{\"response\": {\"message\": \"updated " + accountsToAdd.size() + " entities\"}}";
    }
}
