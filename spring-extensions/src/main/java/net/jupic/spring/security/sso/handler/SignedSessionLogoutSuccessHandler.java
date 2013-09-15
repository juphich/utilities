package net.jupic.spring.security.sso.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.jupic.spring.security.sso.publisher.SessionPublisher;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.util.Assert;


/**
 * @author chang jung pil
 *
 */
public class SignedSessionLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler implements InitializingBean {

	protected SessionPublisher sessionPublisher;
			
	public void setSessionPublisher(SessionPublisher sessionPublisher) {
		this.sessionPublisher = sessionPublisher;
	}

	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		sessionPublisher.expire(request, response);
		
		additionalProcess(request, response, authentication);		
		super.onLogoutSuccess(request, response, authentication);
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(sessionPublisher, "SessionPublisher must be set!!");
	}
	
	/**
	 * overriding enable hook method.
	 * 
	 * @param request
	 * @param response
	 * @param authentication
	 */
	protected void additionalProcess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
	}
}
