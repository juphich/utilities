package net.jupic.commons.http.mock;

public class Message {

	private String code;
	private String message;
	
	public Message(String code, String message) {
		this.code = code;
		this.message = message;
	}
	
	public String getCode() {
		return code;
	}
	public String getMessage() {
		return message;
	}
}
