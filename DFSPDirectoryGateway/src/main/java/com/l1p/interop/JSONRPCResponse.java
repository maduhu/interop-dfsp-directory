package com.l1p.interop;


import java.util.HashMap;
import java.util.Map;

/**
 * Created by Bryan on 8/17/2016.
 */
public class JSONRPCResponse {

    public static final String ID_FIELD = "id";
    public static final String RESULT_FIELD = "result";

    final String responseJSON;

    public JSONRPCResponse( String id, Map result ) {
        Map header = new HashMap();

        if ( result == null )
            result = new HashMap();

        if ( id == null )
            id = "";

        header.put( "jsonrpc", "2.0" );
        header.put( ID_FIELD, id );
        header.put( RESULT_FIELD, result );

        responseJSON = JSONUtil.mapToString( header );
    }

    @Override
    public String toString() {
        return responseJSON;
    }
}
