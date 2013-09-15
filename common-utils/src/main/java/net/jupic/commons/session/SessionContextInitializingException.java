package net.jupic.commons.session;

public class SessionContextInitializingException extends RuntimeException {

	private static final long serialVersionUID = -6215750469149806960L;

	public SessionContextInitializingException(String message, Throwable cause) {
		super(message, cause);
	}

	public SessionContextInitializingException(String message) {
		super(message);
	}
}
