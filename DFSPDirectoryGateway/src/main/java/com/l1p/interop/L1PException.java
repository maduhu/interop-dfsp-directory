package com.l1p.interop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Exception class to deal with custom exceptions
 *
 * Created by Bryan on 8/17/2016.
 */
public class L1PException {

    private final String jsonString;

    public L1PException( final String id, final int code, final String message, final String type, Exception e ) {
        final Map<String, Object> errorMap = new HashMap<String, Object>();
        errorMap.put( "code", code );
        errorMap.put( "message", message );
        errorMap.put( "type", type );
        errorMap.put("errorPrint", e != null ? e.getMessage() : "" );

        final Map<String, Object> debug = new HashMap<String, Object>();

        if ( e != null ) {
            List<String> stackMessages = parseStackTrace(e.getStackTrace());

            debug.put("stackInfo", stackMessages);
            debug.put("cause", e.getCause().getMessage() );   //not sure what should go in cause
        }

        final Map<String, Object> result = new HashMap<String, Object>();
        
        result.put( "jsonrpc", "2.0" );
        result.put( "id", id );
        
        result.put( "error", errorMap );
        result.put( "debug", debug );

        jsonString = JsonMapStringTransformer.mapToString( result );
    }

    private List<String> parseStackTrace( StackTraceElement[] stackTrace ) {
        List<String> result = new ArrayList<String>();

        if ( stackTrace != null && stackTrace.length > 0 ) {
            for ( int i = 0; i < stackTrace.length; i++ ) {
                StackTraceElement nextElement = stackTrace[i];
                result.add( nextElement.toString() );
            }
        }

        return result;
    }

    @Override
    public String toString() {
        return jsonString;
    }
}
