package net.jupic.spring.file.exception;

/**
 * @author chang jung pil
 *
 */
public class FileSizeOverException extends FileUploadException {

	private static final long serialVersionUID = -8294648411196905054L;

	public FileSizeOverException(String message) {
		super(message);
	}
}
