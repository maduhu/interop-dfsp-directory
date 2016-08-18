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

    public JSONRPCResponse( final String id, final Map result ) {
        Map header = new HashMap();
        header.put( "jsonrpc", "2.0" );
        header.put( ID_FIELD, id );
        header.put( RESULT_FIELD, result );

        responseJSON = JSONUtil.mapToJSONString( header );
    }

    @Override
    public String toString() {
        return responseJSON;
    }
}
