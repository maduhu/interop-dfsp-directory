package com.l1p.interop;

import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * JUNit Test for JSONUtil.  This test currently only tests failure modes.
 *
 * Created by Bryan on 8/18/2016.
 */
public class JSONUtilTest {

    @Test
    public void invalidInputShouldProduceRuntimeException() throws Exception {
        //test map creation from non-json input
        try {
            JSONUtil.stringToMap( "<NotJSON/>" );
        } catch( NullPointerException e ) {
            fail( "Conversion of invalid data to map produced a NullPointerException" );
        } catch( RuntimeException e ) {
            assertTrue( "Conversion of invalid data to map produced a RuntimeException of an unexpected type: " + e.getMessage(), e.getMessage().contains( "Failed to convert source String" ) );
        } catch( Exception e ) {
            fail( "Conversion of invalid data to map produced an unexpected exception: " + e.getClass().getName() );
        }

        //test map creation from null input - should return empty Map
        Map<String,Object> map = JSONUtil.stringToMap( null );
        assertTrue( "Map produced from null input was not empty", map.isEmpty() );

        //test string creation from null input
        assertEquals( "Conversion of null data to string produced unexpected result", JSONUtil.mapToString( null ), "{}" );

        Map badDataMap = new HashMap();
        badDataMap.put( "key", System.err );
        try {
            JSONUtil.mapToString( badDataMap );
        } catch( NullPointerException e ) {
            fail( "Conversion of invalid map data to string produced a NullPointerException" );
        } catch( RuntimeException e ) {
            assertTrue( "Conversion of invalid map data to string produced a RuntimeException of an unexpected type: " + e.getMessage(), e.getMessage().contains( "Failed to convert source Map to JSON" ) );
        } catch( Exception e ) {
            fail( "Conversion of invalid map data to string produced an unexpected exception: " + e.getClass().getName() );
        }
    }
}
