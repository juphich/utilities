package net.jupic.commons.parser.exception;

public class MessageParsingException extends RuntimeException {
	
	private static final long serialVersionUID = -2677949860261426420L;

	public MessageParsingException(String message, Throwable cause) {
		super(message, cause);
	}

	public MessageParsingException(String message) {
		super(message);
	}
}
