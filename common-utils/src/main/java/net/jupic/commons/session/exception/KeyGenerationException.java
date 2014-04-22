package net.jupic.commons.session.exception;

public class KeyGenerationException extends RuntimeException {

	private static final long serialVersionUID = 1894423922958419069L;

	public KeyGenerationException(String message, Throwable cause) {
		super(message, cause);
	}

	public KeyGenerationException(String message) {
		super(message);
	}

	public KeyGenerationException(Throwable cause) {
		super(cause);
	}
}
