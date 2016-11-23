package com.l1p.interop.dfsp.directory;

import com.l1p.interop.L1PErrorResponse;
import com.l1p.interop.dfsp.directory.exception.DirectoryError;
import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.api.transport.PropertyScope;
import org.mule.transformer.AbstractMessageTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

/**
 * Created by honaink on 11/22/16.
 */
public class DirectoryExceptionTransformer extends AbstractMessageTransformer {
  private Logger log = LoggerFactory.getLogger(this.getClass());

  public DirectoryExceptionTransformer() {
  }

  public Object transformMessage(MuleMessage muleMessage, String outputEncoding) throws TransformerException {
    String errorMessageId = muleMessage.getProperty("errorMessageId", PropertyScope.SESSION);
    String interopID = muleMessage.getProperty("interopID", PropertyScope.SESSION) != null ? muleMessage.getProperty("interopID", PropertyScope.SESSION).toString() : null;
    String rootExceptionCause = muleMessage.getExceptionPayload().getRootException().getMessage();
    String errorMessage = "Failed to process request for interopID=" + interopID + ": " + rootExceptionCause;
    this.log.warn(errorMessageId + ": " + rootExceptionCause);
    this.log.warn(errorMessage);
    DirectoryError directoryError = new DirectoryError(errorMessageId, errorMessage);
    return directoryError;
  }
}
