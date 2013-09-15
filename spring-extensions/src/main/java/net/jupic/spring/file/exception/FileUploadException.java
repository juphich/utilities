package net.jupic.spring.file.exception;

/**
 * @author chang jung pil
 *
 */
public class FileUploadException extends RuntimeException {

	private static final long serialVersionUID = -92905581142127485L;

	public FileUploadException(String message, Throwable cause) {
		super(message, cause);
	}

	public FileUploadException(String message) {
		super(message);
	}

	public FileUploadException(Throwable cause) {
		super(cause);
	}
}
