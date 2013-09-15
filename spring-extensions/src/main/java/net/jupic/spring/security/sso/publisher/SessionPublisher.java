package net.jupic.spring.security.sso.publisher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface SessionPublisher {
	public void publish(Object principal, HttpServletRequest request, HttpServletResponse response);
	
	public void expire(HttpServletRequest request, HttpServletResponse response);
}
