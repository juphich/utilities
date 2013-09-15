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


public class RequestHeaderBasedSsoAuthenticationFilter extends AbstractSingleSignOnAuthenticationFilter {

	@SuppressWarnings("rawtypes")
	protected SignedSessionService signedSessionService;
	private String sessionIdHeaderName = SignedSessionService.DEFAULT_SESSION_PARAM_NAME;
	
	public void setSessionIdHeaderName(String sessionIdHeaderName) {
		this.sessionIdHeaderName = sessionIdHeaderName;
	}
	
	@Autowired(required = false)
	public void setSignedSessionService(@SuppressWarnings("rawtypes") SignedSessionService signedSessionService) {
		this.signedSessionService = signedSessionService;
	}

	@Override
	protected SignedSessionAuthenticationToken obtainSignedAuthenticationToken(
			HttpServletRequest request, HttpServletResponse response) {
		String signedSessionId = request.getHeader(sessionIdHeaderName);
		
		SignedSession signedSession = new SimpleSignedSession(null);
		if (signedSessionId != null && !signedSessionId.isEmpty()) {
			signedSession = signedSessionService.getSession(signedSessionId);
		}
		
		return new SignedSessionAuthenticationToken(signedSession);
	}
	
	@Override
	protected void initFilterBean() throws ServletException {
		super.initFilterBean();
		Assert.notNull(signedSessionService, "SignedSessionService must be set on RequestHeaderBasedSsoAuthenticationFilter.");
	}
}
