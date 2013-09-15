package net.jupic.commons.session.exception;

/**
 * @author chang jung pil
 *
 */
public class SessionAttributeOptainException extends RuntimeException {
	
	private static final long serialVersionUID = 3288267628925467012L;

	public SessionAttributeOptainException(String message, Throwable cause) {
		super(message, cause);
	}

	public SessionAttributeOptainException(String message) {
		super(message);
	}

	public SessionAttributeOptainException(Throwable cause) {
		super(cause);
	}
}
