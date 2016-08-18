package com.l1p.interop.dfsp.directory;

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
        final Map payload = (Map)muleMessage.getPayload();

        String userURI = (String)((Map)payload.get( "params" )).get( "userURI" );

        if ( userURI == null ) {
            throw new TransformerException(MessageFactory.createStaticMessage( "Missing required request parameter 'userURI'" ) );
        }

        Map<String,String> account = accountStore.getAccount( userURI );

        if ( account == null ) {
            throw new TransformerException(MessageFactory.createStaticMessage( "Account not found for userURI=" + userURI ) );
        }

        logger.info( "Returning account for userURI=" + userURI + ": " + account );

        Map<String,Object> getUserResponse = new HashMap<String,Object>();
        getUserResponse.put( "jsonrpc", "2.0" );
        getUserResponse.put( "id", "ullamco eiusmod" );
        getUserResponse.put( "result", account );

        try {
            return new ObjectMapper().writeValueAsString(getUserResponse);
        } catch( IOException e ) {
            throw new TransformerException(MessageFactory.createStaticMessage( "Failed to convert user account data to JSON: " + e.getMessage() ) );
        }
    }
}
