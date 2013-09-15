package net.jupic.commons.parser.exception;


/**
 * @author Chang jung pil
 *
 */
public class ApiXmlParsingException extends MessageParsingException {

	public enum Code { UNKNWON, NORESULT }
	
	private static final long serialVersionUID = -1458992612227365429L;

	private final Code code;
	
	/**
	 * @param message
	 * @param t
	 */
	public ApiXmlParsingException(String message, Throwable t) {
		super(message, t);
		this.code = Code.UNKNWON;
	}

	/**
	 * @param message
	 */
	public ApiXmlParsingException(String message) {
		this(message, Code.UNKNWON);
	}

	/**
	 * @param message
	 * @param code
	 */
	public ApiXmlParsingException(String message, Code code) {
		super(message);
		this.code = code;
	}

	/**
	 * @return
	 */
	public Code getCode() {
		return this.code;
	}
}
