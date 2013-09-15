package net.jupic.commons.exception;

/**
 * @author chang jung pil
 *
 */
public class UtilityExecutionException extends RuntimeException {

	private static final long serialVersionUID = 7131261160255006797L;

	public UtilityExecutionException(String message, Throwable cause) {
		super(message, cause);
	}

	public UtilityExecutionException(String message) {
		super(message);
	}

	public UtilityExecutionException(Throwable cause) {
		super(cause);
	}
}
