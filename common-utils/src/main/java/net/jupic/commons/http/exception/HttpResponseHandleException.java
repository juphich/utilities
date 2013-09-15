package net.jupic.commons.http.exception;

import org.apache.http.client.HttpResponseException;

public class HttpResponseHandleException extends HttpResponseException {
	private static final long serialVersionUID = -6531610260893753061L;
	
	private Object statusMessage;

	public HttpResponseHandleException(final int statusCode, final String message) {
		super(statusCode, message);
	}
	
	public HttpResponseHandleException(final int statusCode, final String message, Object statusMessage) {
		this(statusCode, message);
		this.statusMessage = statusMessage;
	}
	
	public Object getStatusMessage() {
		return statusMessage;
	}
}
