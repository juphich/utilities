package net.jupic.commons.http.exception;

public class RequesterCreationException extends RuntimeException {

	private static final long serialVersionUID = 9068907078357887972L;
	
	public RequesterCreationException(String message, Throwable cause) {
		super(message, cause);
	}

	public RequesterCreationException(String message) {
		super(message);
	}
}
