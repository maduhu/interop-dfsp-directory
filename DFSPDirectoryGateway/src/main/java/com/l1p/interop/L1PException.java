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

    public L1PException( final String code, final String message, final String type, Exception e ) {
        final Map errorMap = new HashMap();
        errorMap.put( "code", code );
        errorMap.put( "message", message );
        errorMap.put( "errorPrint", e.getMessage() );
        errorMap.put( "type", type );

        final Map debug = new HashMap();
        List stackMessages = parseStackTrace( e.getStackTrace() );

        debug.put( "stackInfo", stackMessages );
        debug.put( "cause", e.getCause() != null ? e.getCause().getMessage() : "" );   //not sure what should go in cause

        final Map result = new HashMap();
        result.put( "error", errorMap );
        result.put( "debug", debug );

        jsonString = JSONUtil.mapToString( result );
    }

    private List<String> parseStackTrace( StackTraceElement[] stackTrace ) {
        List<String> result = new ArrayList();

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
