package net.jupic.spring.security.sso.filter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.jupic.spring.security.sso.SignedSession;
import net.jupic.spring.security.sso.SignedSessionAuthenticationToken;
import net.jupic.spring.security.sso.SignedSessionService;
import net.jupic.spring.security.sso.SimpleSignedSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;


public class RequestParameterBasedSsoAuthenticationFilter extends AbstractSingleSignOnAuthenticationFilter {

	@SuppressWarnings("rawtypes")
	protected SignedSessionService signedSessionService;
	private String sessionIdParameter = SignedSessionService.DEFAULT_SESSION_PARAM_NAME;
	
	public void setSessionIdParameter(String sessionIdParameter) {
		this.sessionIdParameter = sessionIdParameter;
	}
	
	@Autowired(required = false)
	public void setSignedSessionService(@SuppressWarnings("rawtypes") SignedSessionService signedSessionService) {
		this.signedSessionService = signedSessionService;
	}

	@Override
	protected SignedSessionAuthenticationToken obtainSignedAuthenticationToken(
			HttpServletRequest request, HttpServletResponse response) {
		String signedSessionId = request.getParameter(sessionIdParameter);
		
		SignedSession signedSession = new SimpleSignedSession(null);
		if (signedSessionId != null && !signedSessionId.isEmpty()) {
			signedSession = signedSessionService.getSession(signedSessionId);
		}
		
		return new SignedSessionAuthenticationToken(signedSession);
	}
	
	@Override
	protected void initFilterBean() throws ServletException {
		super.initFilterBean();
		Assert.notNull(signedSessionService, "SignedSessionService must be set on RequestParameterBasedSsoAuthenticationFilter.");
	}
}
