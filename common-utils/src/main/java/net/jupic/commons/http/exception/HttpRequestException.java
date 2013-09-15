package net.jupic.commons.http.exception;

public class HttpRequestException extends RuntimeException {

	private static final long serialVersionUID = 1874673275023440798L;

	public HttpRequestException(String message, Throwable cause) {
		super(message, cause);
	}

	public HttpRequestException(String message) {
		super(message);
	}
}
