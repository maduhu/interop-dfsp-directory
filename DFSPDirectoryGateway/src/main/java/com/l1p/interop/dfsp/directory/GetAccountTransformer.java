package com.l1p.interop.dfsp.directory;

import com.l1p.interop.JsonRpcResponse;
import com.l1p.interop.L1PErrorResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.config.i18n.MessageFactory;
import org.mule.transformer.AbstractMessageTransformer;
import org.mule.transformer.types.DataTypeFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Bryan on 8/17/2016.
 */
public class GetAccountTransformer extends AbstractMessageTransformer {

    private static final Logger logger = LogManager.getLogger(GetAccountTransformer.class);
    private final AccountDataStore accountStore;

    public GetAccountTransformer(AccountDataStore accountStore) {
        registerSourceType(DataTypeFactory.create(Map.class));
        setReturnDataType((DataTypeFactory.create(String.class)));
        setName("GetAccountTransformer");
        this.accountStore = accountStore;
    }

    public Object transformMessage(MuleMessage muleMessage, String s) throws TransformerException {
    	
        final Map<String, Object> payload = (Map<String, Object>)muleMessage.getPayload();
        final String id = (String)payload.get( "id" );

        
        String userURI = (String)((Map<String, Object>)payload.get( "params" )).get( "userURI" );

        if ( userURI == null ) {
            throw new TransformerException(MessageFactory.createStaticMessage( "Missing required request parameter 'userURI'" ) );
        }

        Map<String, String> account = accountStore.getAccount( userURI );

        if ( account == null ) {
            throw new TransformerException(MessageFactory.createStaticMessage( "Account not found for userURI=" + userURI ) );
        }

        return new JsonRpcResponse( id, account ).toString();
    }
}
