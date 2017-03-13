package com.l1p.interop;

import java.util.HashMap;
import java.util.Map;

/**
 * Helper class to construct a request that matches the JSON RPC request syntax.
 *
 * Created by Bryan on 8/17/2016.
 */
public class JsonRpcRequest {
    private final Map<String,Object> requestData = new HashMap<>();
    private final Map<String,String> params;

    public JsonRpcRequest(final String id, final String method, final Map<String,String> params ) {
        this.params = params;

        requestData.put( "jsonrpc", "2.0" );
        requestData.put( "id", id );
        requestData.put( "method", method );
        requestData.put( "params", params );
    }

    public void addParameter(String key, String value ) {
        params.put( key, value );
    }

    public String toJSONString() {
        return JsonTransformer.mapToString( requestData );
    }

    @Override
    public String toString() {
        return "JsonRpcRequest{" +
                "requestData=" + requestData +
                ", params=" + params +
                '}';
    }
}
