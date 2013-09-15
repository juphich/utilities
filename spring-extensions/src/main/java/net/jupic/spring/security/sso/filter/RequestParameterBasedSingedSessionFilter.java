package net.jupic.spring.security.sso.filter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.jupic.spring.security.sso.SignedSession;
import net.jupic.spring.security.sso.SignedSessionAuthenticationToken;
import net.jupic.spring.security.sso.SignedSessionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;


public class RequestParameterBasedSingedSessionFilter extends AbstractSignedSessionFilter {

	@SuppressWarnings("rawtypes")
	protected SignedSessionService signedSessionService;
	
	@Autowired(required = false)
	public void setSignedSessionService(@SuppressWarnings("rawtypes") SignedSessionService signedSessionService) {
		this.signedSessionService = signedSessionService;
	}
	
	@Override
	protected SignedSessionAuthenticationToken signedSession(HttpServletRequest request, HttpServletResponse response) {
		String sessionId = request.getParameter(this.signedSessionService.getSessionParameterName());
		
		SignedSession session = null;
		
		if (sessionId != null && !sessionId.isEmpty()) {
			session = signedSessionService.getSession(sessionId);
		}
		
		return new SignedSessionAuthenticationToken(session);
	}

	@Override
	protected void initFilterBean() throws ServletException {
		super.initFilterBean();
		Assert.notNull(signedSessionService, "SignedSessionService must be set on RequestParameterBasedSingedSessionFilter.");
	}
}
