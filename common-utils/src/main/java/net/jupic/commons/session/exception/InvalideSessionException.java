package net.jupic.commons.session.exception;

/**
 * @author chang jung pil
 *
 */
public class InvalideSessionException extends RuntimeException {

	private static final long serialVersionUID = -3741462215299854732L;

	public InvalideSessionException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalideSessionException(String message) {
		super(message);
	}
}
