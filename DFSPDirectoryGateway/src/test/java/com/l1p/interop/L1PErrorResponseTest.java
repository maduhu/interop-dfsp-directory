package com.l1p.interop;

import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by Bryan on 8/18/2016.
 */
public class L1PErrorResponseTest {

    @Test
    /**
     * This test proves the response creates a JSON String that is convertable back to a Map structure containing
     * the correct data.
     */
    public void responseShouldBeConvertableToJSON() throws Exception {
        String id = "id_123";
        int code = 456;
        String message="test message";
        String type = "test.type";

        L1PErrorResponse errorResponse = new L1PErrorResponse( id, code, message, type, null );
        String errorResponseJson = errorResponse.toString();

        //convert output to map
        Map<String,Object> header = JsonMapStringTransformer.stringToMap( errorResponseJson );
        validateErrorResponse( header, id, code, message, type, null, null, null );
    }

    @Test
    public void errorResponseShouldIncludeExceptionInformation() throws Exception {
        String id = "id_123";
        int code = 456;
        String message="test message";
        String type = "test.type";
        Exception causeException = new RuntimeException( "test cause exception" );
        Exception testException = new RuntimeException( "test actual exception", causeException );

        L1PErrorResponse errorResponse = new L1PErrorResponse( id, code, message, type, testException );
        String errorResponseJson = errorResponse.toString();

        //convert output to map
        Map<String,Object> header = JsonMapStringTransformer.stringToMap( errorResponseJson );
        validateErrorResponse( header, id, code, message, type, testException.getMessage(), causeException.getMessage(), testException );
    }

    public void validateErrorResponse( Map<String,Object> errorResponseHeader, String expectedId, int expectedCode, String expectedMessage, String expectedType, String expectedErrorPrint, String expectedCause, Exception exception ) throws Exception {
        assertTrue( "Header map was null", errorResponseHeader != null );
        assertTrue( "Size of header map was incorrect, expected 4, received " + errorResponseHeader.size(), errorResponseHeader.size() == 4 );
        assertEquals( "Header map did not contain correct data for jsonrpc element", "2.0", errorResponseHeader.get( "jsonrpc" ) );
        assertEquals( "Header map did not contain correct data for id element", expectedId, errorResponseHeader.get( "id" ) );

        Map<String,Object> error = (Map<String,Object>)errorResponseHeader.get( "error" );
        assertTrue( "Error map was null", error != null );
        assertTrue( "Size of Error map was incorrect, expected 4, received " + error.size(), error.size() == 4 );
        assertEquals( "Header map did not contain correct data for code element", expectedCode, error.get( "code" ) );
        assertEquals( "Header map did not contain correct data for code element", expectedMessage, error.get( "message" ) );
        assertEquals( "Header map did not contain correct data for code element", expectedType, error.get( "type" ) );


        Map<String,Object> debug = (Map<String,Object>)errorResponseHeader.get( "debug" );
        assertTrue( "Debug map was null", debug != null );

        if ( exception == null ) {
            assertTrue( "Debug map was not empty", debug.size() == 0 );
            assertEquals( "Header map did not contain correct data for errorPrint element", "", error.get( "errorPrint" ) );
        } else {
            assertEquals( "Error map did not contain correct data for errorPrint element", expectedErrorPrint, error.get( "errorPrint" ) );
            assertEquals( "Error map did not contain correct data for errorPrint element", expectedCause, debug.get( "cause" ) );
        }

    }

}
