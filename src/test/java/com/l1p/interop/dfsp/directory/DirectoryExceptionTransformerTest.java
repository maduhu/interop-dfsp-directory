package com.l1p.interop.dfsp.directory;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.mule.api.MuleEvent;
import org.mule.api.MuleMessage;
import org.mule.api.transport.PropertyScope;
import org.mule.component.SimpleCallableJavaComponentTestCase;
import org.mule.message.DefaultExceptionPayload;

import com.l1p.interop.dfsp.directory.exception.DirectoryError;
import com.l1p.interop.dfsp.directory.exception.Param;
import com.l1p.interop.dfsp.directory.exception.ValidationError;

public class DirectoryExceptionTransformerTest extends SimpleCallableJavaComponentTestCase {
	
	@Test
	public void testDirectoryExceptionTransformer() throws Exception {

		/* Prepare mule message. */
		Object payload = buildTransformerData("errorMessageID", "errorMessageID" );
		final String secondaryId = "SecondaryId";
		final String secondaryMessage = "SecondaryMessage";
		
		Param params = new Param();
		params.setKey("key");
		params.setValue("value");
		params.setChild("child");
		
		assertEquals( "key", params.getKey() );
		assertEquals("value", params.getValue() );
		assertEquals("child", params.getChild() );
		
		ValidationError vError1 = new ValidationError();
		vError1.setMessage("message");
		vError1.setParams(params);
		
		assertEquals("message", vError1.getMessage() );
		assertTrue("The object set is not being returned for params", vError1.getParams().equals(params) );
		
		List<ValidationError> vErrorList = new ArrayList<ValidationError>();
		vErrorList.add(vError1);
		
		MuleEvent event = getTestEvent(payload, muleContext);
		MuleMessage muleMessage = event.getMessage();
        muleMessage.setProperty("traceID", "1cd3fa9f-e518-42d6-ae42-6c79b3b9a07c", PropertyScope.SESSION);
        muleMessage.setProperty("errorMessageId", "errorID", PropertyScope.SESSION);
        event.getMessage().setExceptionPayload( new DefaultExceptionPayload( new Exception() ) );
        
        DirectoryExceptionTransformer transformer = new DirectoryExceptionTransformer();
        DirectoryError dError= (DirectoryError) transformer.transformMessage(muleMessage, "UTF-8");
        
        dError.setValidationErrors(vErrorList);
        assertTrue("Does not contain expected List", dError.getValidationErrors().containsAll(vErrorList) );
        
        assertEquals("errorID", dError.getId() );
        assertTrue("Does not contain traceID as expected:", dError.getMessage().contains(muleMessage.getProperty("traceID", PropertyScope.SESSION).toString()));
        
        dError.setId(secondaryId);
        dError.setMessage(secondaryMessage);
        assertEquals(secondaryId, dError.getId() );
        assertTrue("Does not contain traceID as expected:", (dError.getMessage() ).contains( secondaryMessage ) );
        
	}
	
	private Map<String, Object> buildTransformerData(String keyName, String keyValue) {
		
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		Map<String, Object> uriMap = new HashMap<String, Object>();
		
		uriMap.put(keyName, keyValue);
		paramsMap.put("params", uriMap);
		
		return paramsMap;
	}

}
