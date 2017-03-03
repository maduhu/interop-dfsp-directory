package com.l1p.interop;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.activation.DataHandler;

import org.mule.api.ExceptionPayload;
import org.mule.api.MuleContext;
import org.mule.api.MuleEvent;
import org.mule.api.MuleException;
import org.mule.api.MuleMessage;
import org.mule.api.transformer.DataType;
import org.mule.api.transformer.Transformer;
import org.mule.api.transformer.TransformerException;
import org.mule.api.transport.PropertyScope;

public class TestMuleMessageImpl implements MuleMessage {
	
	private  Object payload = null;

	/**
	 * 
	 */
	private static final long serialVersionUID = -1185596438176080087L;
	private Map<String, Object> propertyMapSession = new HashMap<String, Object>();
	private Map<String, Object> propertyMapApplication = new HashMap<String, Object>();
	private Map<String, Object> propertyMapInvocation = new HashMap<String, Object>();

	@Override
	public void addProperties(Map<String, Object> properties) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addProperties(Map<String, Object> properties, PropertyScope scope) {
		// TODO Auto-generated method stub

	}

	@Override
	public void clearProperties() {
		// TODO Auto-generated method stub

	}

	@Override
	public void clearProperties(PropertyScope scope) {
		// TODO Auto-generated method stub

	}

	@Override
	public Object getProperty(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setProperty(String key, Object value) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setInvocationProperty(String key, Object value) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setInvocationProperty(String key, Object value, DataType<?> dataType) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setOutboundProperty(String key, Object value) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setOutboundProperty(String key, Object value, DataType<?> dataType) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setProperty(String key, Object value, PropertyScope scope) {
		// TODO Auto-generated method stub
		if (scope.equals(PropertyScope.SESSION)) this.propertyMapSession.put(key, value);
		else if (scope.equals(PropertyScope.APPLICATION)) this.propertyMapApplication.put(key, value);
		else if (scope.equals(PropertyScope.INVOCATION)) this.propertyMapInvocation.put(key, value);
	}

	@Override
	public void setProperty(String key, Object value, PropertyScope scope, DataType<?> dataType) {
		// TODO Auto-generated method stub

	}

	@Override
	public Object removeProperty(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object removeProperty(String key, PropertyScope scope) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<String> getPropertyNames() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<String> getPropertyNames(PropertyScope scope) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<String> getInvocationPropertyNames() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<String> getInboundPropertyNames() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<String> getOutboundPropertyNames() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<String> getSessionPropertyNames() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getPayload() {
		System.out.println("get payload ");
		return this.payload;
	}

	@Override
	public String getUniqueId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getMessageRootId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setMessageRootId(String rootId) {
		// TODO Auto-generated method stub

	}

	@Override
	public void propagateRootId(MuleMessage parent) {
		// TODO Auto-generated method stub

	}

	@Override
	public Object getProperty(String name, Object defaultValue) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T getProperty(String name, PropertyScope scope) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T getInboundProperty(String name, T defaultValue) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T getInboundProperty(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T getInvocationProperty(String name, T defaultValue) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T getInvocationProperty(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T getOutboundProperty(String name, T defaultValue) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T getOutboundProperty(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T findPropertyInAnyScope(String name, T defaultValue) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T getProperty(String name, PropertyScope scope, T defaultValue) {
		if (scope.equals(PropertyScope.SESSION)) 
			return (T) this.propertyMapSession.get(name);
		else if (scope.equals(PropertyScope.APPLICATION)) 
			return (T) this.propertyMapApplication.get(name);
		else if (scope.equals(PropertyScope.INVOCATION)) 
			return (T) this.propertyMapInvocation.get(name);
		else
			return defaultValue;
	}

	@Override
	public DataType<?> getPropertyDataType(String name, PropertyScope scope) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getIntProperty(String name, int defaultValue) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getLongProperty(String name, long defaultValue) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getDoubleProperty(String name, double defaultValue) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getStringProperty(String name, String defaultValue) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean getBooleanProperty(String name, boolean defaultValue) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setBooleanProperty(String name, boolean value) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setIntProperty(String name, int value) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setLongProperty(String name, long value) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setDoubleProperty(String name, double value) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setStringProperty(String name, String value) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setCorrelationId(String id) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getCorrelationId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getCorrelationSequence() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setCorrelationSequence(int sequence) {
		// TODO Auto-generated method stub

	}

	@Override
	public int getCorrelationGroupSize() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setCorrelationGroupSize(int size) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setReplyTo(Object replyTo) {
		// TODO Auto-generated method stub

	}

	@Override
	public Object getReplyTo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ExceptionPayload getExceptionPayload() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setExceptionPayload(ExceptionPayload payload) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addAttachment(String name, DataHandler dataHandler) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void addOutboundAttachment(String name, DataHandler dataHandler) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void addOutboundAttachment(String name, Object object, String contentType) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeAttachment(String name) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeOutboundAttachment(String name) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public DataHandler getAttachment(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DataHandler getInboundAttachment(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DataHandler getOutboundAttachment(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<String> getAttachmentNames() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<String> getInboundAttachmentNames() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<String> getOutboundAttachmentNames() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getEncoding() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setEncoding(String encoding) {
		// TODO Auto-generated method stub

	}

	@Override
	public void release() {
		// TODO Auto-generated method stub

	}

	@Override
	public void applyTransformers(MuleEvent event, List<? extends Transformer> transformers) throws MuleException {
		// TODO Auto-generated method stub

	}

	@Override
	public void applyTransformers(MuleEvent event, Transformer... transformers) throws MuleException {
		// TODO Auto-generated method stub

	}

	@Override
	public void applyTransformers(MuleEvent event, List<? extends Transformer> transformers, Class<?> outputType)
			throws MuleException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setPayload(Object payload) {
		System.out.println("set payload");
		this.payload = payload;
	}

	@Override
	public void setPayload(Object payload, DataType<?> dataType) {
		System.out.println("set payload with type");
		this.payload = payload;
	}

	@Override
	public <T> T getPayload(Class<T> outputType) throws TransformerException {
		System.out.println("get payload with type");
		return (T) this.payload;
	}

	@Override
	public <T> T getPayload(DataType<T> outputType) throws TransformerException {
		System.out.println("get payload with type");
		return (T) this.payload;
	}

	@Override
	public String getPayloadAsString(String encoding) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPayloadAsString() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public byte[] getPayloadAsBytes() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getOriginalPayload() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPayloadForLogging() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPayloadForLogging(String encoding) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MuleContext getMuleContext() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DataType<?> getDataType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T getSessionProperty(String name, T defaultValue) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T getSessionProperty(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setSessionProperty(String key, Object value) {
		// TODO Auto-generated method stub

	}

	@Override
	public MuleMessage createInboundMessage() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void clearAttachments() {
		// TODO Auto-generated method stub

	}

}
