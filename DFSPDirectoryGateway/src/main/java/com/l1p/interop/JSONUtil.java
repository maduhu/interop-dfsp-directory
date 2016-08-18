package com.l1p.interop;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.Map;

/**
 * Utility class to convert a Map into a JSON String.
 *
 * Created by Bryan on 8/17/2016.
 */
public class JSONUtil {

    /**
     * Converts a Map into its JSON representation
     *
     * @param source - Map to convert
     * @return String representation of the provided map in JSON format
     * @throws RuntimeException if the conversion fails
     */
    public static String mapToJSONString( final Map source ) {
        try {
            return new ObjectMapper().writeValueAsString(source);
        } catch( IOException e ) {
            throw new RuntimeException( "Failed to convert source map = " + source + " to JSON: " + e.getMessage() );
        }
    }
}
