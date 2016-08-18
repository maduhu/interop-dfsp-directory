package com.l1p.interop.dfsp.directory;

import com.l1p.interop.JSONRPCResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mule.api.MuleEventContext;
import org.mule.api.lifecycle.Callable;

import java.util.HashMap;
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

        Map payload = (Map)muleEventContext.getMessage().getPayload();

        final String id = (String)payload.get( "id" );
        List<Map<String,String>> accountsToAdd = (List<Map<String,String>>)payload.get("users");
        logger.info( "Received request id=" + id + " to add " + accountsToAdd.size() + " accounts" );

        accountStore.addAccounts( accountsToAdd );
        String message = "updated " + accountsToAdd.size() + " entities for request id=" + id;
        logger.info( message );

        Map resultMap = new HashMap();
        resultMap.put( "message", message );

        return new JSONRPCResponse( id, resultMap ).toString();
    }
}
