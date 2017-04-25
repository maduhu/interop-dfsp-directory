package com.l1p.interop.dfsp.directory;

import com.l1p.interop.dfsp.directory.exception.DirectoryError;
import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.api.transport.PropertyScope;
import org.mule.transformer.AbstractMessageTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by honaink on 11/22/16.
 */
public class DirectoryExceptionTransformer extends AbstractMessageTransformer {
  private Logger log = LoggerFactory.getLogger(this.getClass());

  public DirectoryExceptionTransformer() {
	  log.debug("DirectoryExceptionTransformer's constructor");
  }

  public Object transformMessage(MuleMessage muleMessage, String outputEncoding) throws TransformerException {
    String errorMessageId = muleMessage.getProperty("errorMessageId", PropertyScope.SESSION);
    String l1pTraceId = muleMessage.getProperty("L1p-Trace-Id", PropertyScope.SESSION) != null ? muleMessage.getProperty("L1p-Trace-Id", PropertyScope.SESSION).toString() : null;
    
    String rootExceptionCause = ( muleMessage.getExceptionPayload().getRootException().getMessage() == null ) ? null : muleMessage.getExceptionPayload().getRootException().getMessage() ;
    String errorMessage = "Failed to process request for L1p-Trace-Id=" + l1pTraceId + ": " + rootExceptionCause;
    
    this.log.warn(errorMessageId + ": " + rootExceptionCause);
    this.log.warn(errorMessage);
    
    return ( new DirectoryError(errorMessageId, errorMessage) );

  }
}
