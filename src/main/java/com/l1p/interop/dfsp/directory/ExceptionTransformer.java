package com.l1p.interop.dfsp.directory;

import com.l1p.interop.L1PErrorResponse;

import java.util.Map;
import java.util.HashMap;

import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.api.transport.PropertyScope;
import org.mule.transformer.AbstractMessageTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class takes a mule exception and creates a L1PErrorResponse that is formatted as json string. 
 * Created by Bryan on 8/31/2016
 */
public class ExceptionTransformer extends AbstractMessageTransformer {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public synchronized Object transformMessage(MuleMessage muleMessage, String outputEncoding) throws TransformerException {
        final String errorMessageId = muleMessage.getProperty( "errorMessageId", PropertyScope.SESSION );
        final String errorMessage = muleMessage.getProperty( "errorMessage", PropertyScope.SESSION );
        
        Map<String, Object> addProperty = new HashMap<String, Object>();
        addProperty.put("Content-Type", "application/json");

        Throwable t = null;
        if ( muleMessage.getExceptionPayload() != null ) {
            t = muleMessage.getExceptionPayload().getException();
        }

        muleMessage.addProperties(addProperty, PropertyScope.INVOCATION);
        L1PErrorResponse errorResponse = new L1PErrorResponse( errorMessageId, errorMessage, t );
        
        return errorResponse.getJsonString();
    }

}

