package net.jupic.spring.security.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * @author chang jung pil
 *
 */
public class UserDetailsCreationException extends AuthenticationException {

	private static final long serialVersionUID = -1156017841286687596L;

	/**
	 * @param message
	 */
	public UserDetailsCreationException(String message) {
		super(message);
	}
	
	/**
	 * @param message
	 * @param t
	 */
	public UserDetailsCreationException(String message, Throwable t) {
		super(message, t);
	}
}
