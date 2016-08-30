package com.l1p.interop;

import java.util.HashMap;
import java.util.Map;

/**
 * This class's constructor adds the needed header details to comply with
 * JSONRpc conventions and is used as the response object to JSONRpc commands.
 * 
 * Created by Bryan on 8/17/2016.
 */
public class JsonRpcResponse {

	public static final String ID_FIELD = "id";
	public static final String RESULT_FIELD = "result";

	final String responseJSON;

	public JsonRpcResponse(String id, Map result) {
		Map header = new HashMap();

		// makes sure a Map is always returned
		if (result == null)
			result = new HashMap();

		if (id == null)
			id = "";

		header.put("jsonrpc", "2.0");
		header.put(ID_FIELD, id);
		header.put(RESULT_FIELD, result);

		responseJSON = JsonTransformer.mapToString(header);
	}

	@Override
	public String toString() {
		return responseJSON;
	}
}
