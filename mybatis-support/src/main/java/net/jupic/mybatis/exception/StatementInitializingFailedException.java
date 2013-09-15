package net.jupic.mybatis.exception;

/**
 * @author chang jung pil
 *
 */
public class StatementInitializingFailedException extends RuntimeException {

	private static final long serialVersionUID = 7504463262305523640L;

	/**
	 * @param message
	 * @param cause
	 */
	public StatementInitializingFailedException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public StatementInitializingFailedException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public StatementInitializingFailedException(Throwable cause) {
		super(cause);
	}
}
