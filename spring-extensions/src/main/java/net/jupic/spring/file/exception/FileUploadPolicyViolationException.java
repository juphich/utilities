package net.jupic.spring.file.exception;

/**
 * @author chang jung pil
 *
 */
public class FileUploadPolicyViolationException extends FileUploadException {
	private static final long serialVersionUID = 3728054633121552492L;

	public FileUploadPolicyViolationException(String message) {
		super(message);
	}
}
