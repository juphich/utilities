package net.jupic.mybatis.exception;

/**
 * @author chang jung pil
 *
 */
public class InvalidStatementExceptions extends RuntimeException {
	
	private static final long serialVersionUID = 426998555201622635L;
	
	/**
	 * @param message
	 * @param cause
	 */
	public InvalidStatementExceptions(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public InvalidStatementExceptions(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public InvalidStatementExceptions(Throwable cause) {
		super(cause);
	}
}
