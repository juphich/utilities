package net.jupic.spring.security.sso.filter;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.jupic.spring.security.sso.SignedSession;
import net.jupic.spring.security.sso.SignedSessionAuthenticationToken;
import net.jupic.spring.security.sso.SignedSessionService;
import net.jupic.spring.security.sso.SimpleSignedSession;
import net.toolab.utils.WebRequestUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;


public class CookieBasedSsoAuthenticationFilter extends AbstractSingleSignOnAuthenticationFilter {

	@SuppressWarnings("rawtypes")
	protected SignedSessionService signedSessionService;
	private String sessionIdCookieName = SignedSessionService.DEFAULT_SESSION_PARAM_NAME;
	
	public void setSessionIdCookieName(String sessionIdCookieName) {
		this.sessionIdCookieName = sessionIdCookieName;
	}
	
	@Autowired(required = false)
	public void setSignedSessionService(@SuppressWarnings("rawtypes") SignedSessionService signedSessionService) {
		this.signedSessionService = signedSessionService;
	}

	@Override
	protected SignedSessionAuthenticationToken obtainSignedAuthenticationToken(
			HttpServletRequest request, HttpServletResponse response) {
		
		Cookie cookie = WebRequestUtils.obtainCookie(request, sessionIdCookieName);
		
		SignedSession signedSession = new SimpleSignedSession(null);
		if (cookie != null) {
			signedSession = signedSessionService.getSession(cookie.getValue());
		}
		
		return new SignedSessionAuthenticationToken(signedSession);
	}

	@Override
	protected void initFilterBean() throws ServletException {
		super.initFilterBean();
		Assert.notNull(signedSessionService, "SignedSessionService must be set on CookieBasedSsoAuthenticationFilter.");
	}
}
