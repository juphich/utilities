package net.jupic.commons.exception;

public class NotAPojoException extends Exception {

	private static final long serialVersionUID = -6481516272329806799L;
	
	public NotAPojoException(Object source) {
		super("it's not a pojo - type is " + source.getClass().getCanonicalName());
	}
}
