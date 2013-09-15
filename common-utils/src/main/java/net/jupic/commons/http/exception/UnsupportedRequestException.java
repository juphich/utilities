package net.jupic.commons.http.exception;

public class UnsupportedRequestException extends RuntimeException {

	private static final long serialVersionUID = 6612764855071901260L;

	public UnsupportedRequestException(String message, Throwable cause) {
		super(message, cause);
	}

	public UnsupportedRequestException(String message) {
		super(message);
	}
}
