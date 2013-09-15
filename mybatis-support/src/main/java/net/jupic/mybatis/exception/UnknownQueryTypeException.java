package net.jupic.mybatis.exception;

/**
 * @author chang jung pil
 *
 */
public class UnknownQueryTypeException extends RuntimeException {

	private static final long serialVersionUID = -1297312397908201123L;


	public UnknownQueryTypeException(String message, Throwable cause) {
		super(message, cause);
	}

	public UnknownQueryTypeException(String message) {
		super(message);
	}

	public UnknownQueryTypeException(Throwable cause) {
		super(cause);
	}
}
