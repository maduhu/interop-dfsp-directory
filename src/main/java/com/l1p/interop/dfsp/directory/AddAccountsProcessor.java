package com.l1p.interop.dfsp.directory;

import com.l1p.interop.JsonRpcResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mule.api.MuleEventContext;
import org.mule.api.MuleMessage;
import org.mule.api.lifecycle.Callable;
import org.mule.api.transport.PropertyScope;

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
        MuleMessage muleMessage = muleEventContext.getMessage();
        String interopID = muleMessage.getProperty("interopID", PropertyScope.SESSION).toString();
        
        List<Map<String,String>> accountsToAdd = (List<Map<String,String>>)payload.get("users");
        logger.info( "Received request to add " + accountsToAdd.size() + " accounts, interopID=" + interopID);

        accountStore.addAccounts( accountsToAdd );
        String message = "Updated " + accountsToAdd.size() + " entities for request with interopID=" + interopID;
        logger.info( message );

        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put( "message", message );

        return new JsonRpcResponse( id, resultMap ).toString();
    }
}
