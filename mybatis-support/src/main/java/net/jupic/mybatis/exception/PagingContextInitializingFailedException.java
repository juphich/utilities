package net.jupic.mybatis.exception;

/**
 * @author chang jung pil
 *
 */
public class PagingContextInitializingFailedException extends RuntimeException {

	private static final long serialVersionUID = 9056878232037170500L;

	public PagingContextInitializingFailedException(String message, Throwable cause) {
		super(message, cause);
	}

	public PagingContextInitializingFailedException(String message) {
		super(message);
	}
}
