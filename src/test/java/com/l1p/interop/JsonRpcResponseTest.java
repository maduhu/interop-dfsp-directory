package com.l1p.interop;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * JUnit Test for JsonRpcResponse.
 *
 * Created by Bryan on 8/17/2016.
 */
public class JsonRpcResponseTest {

    @Test
    /**
     * This test proves the response creates a JSON String that is convertible back to a Map structure containing
     * the correct data.
     */
    public void responseShouldBeConvertableToJSON() throws Exception {
        Map<String,Object> resultData = new HashMap<>();
        resultData.put( "key1", "value1" );
        resultData.put( "Key2", "Value2" );
        JsonRpcResponse response = new JsonRpcResponse( "12345", resultData );

        // convert JSON string to Map
        Map<String,Object> header = JsonTransformer.stringToMap( response.toString() );
        //validate contents
        assertTrue( "Header map was null", header != null );
        assertTrue( "Size of header map was incorrect, expected 3, received " + header.size(), header.size() == 3 );
        assertEquals( "Header map did not contain correct data for jsonrpc element", "2.0", header.get( "jsonrpc" ) );
        assertEquals( "Header map did not contain correct data for id element", "12345", header.get( "id" ) );
        Map result = (Map)header.get( "result" );
        assertTrue( "Result map was null", result != null );
        assertTrue( "Size of result map was incorrect, expected 2, received " + result.size(), result.size() == 2 );
        assertEquals( "Result map did not contain correct data for key1 element", "value1", result.get( "key1" ) );
        assertEquals( "Result map did not contain correct data for key2 element", "Value2", result.get( "Key2" ) );
    }

    @Test
    /**
     * We expect null values to produce an empty string for id, and an empty map for result.
     */
    public void nullValuesShouldNotThrowException() throws Exception {
        JsonRpcResponse response = new JsonRpcResponse( null, null );

        // convert JSON string to Map
        Map<String,Object> header = JsonTransformer.stringToMap( response.toString() );
        //validate contents
        assertTrue( "Header map was null", header != null );
        assertTrue( "Size of header map was incorrect, expected 3, received " + header.size(), header.size() == 3 );
        assertEquals( "Header map did not contain correct data for jsonrpc element", "2.0", header.get( "jsonrpc" ) );
        assertEquals( "Header map did not contain correct data for id element", "", header.get( "id" ) );
        Map result = (Map)header.get( "result" );
        assertTrue( "Result map was null", result != null );
        assertTrue( "Size of result map was incorrect, expected 0, received " + result.size(), result.size() == 0 );
    }
}
