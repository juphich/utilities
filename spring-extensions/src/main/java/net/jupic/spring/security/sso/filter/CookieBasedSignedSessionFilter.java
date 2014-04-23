package net.jupic.spring.security.sso.filter;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.jupic.spring.security.sso.SignedSession;
import net.jupic.spring.security.sso.SignedSessionAuthenticationToken;
import net.jupic.spring.security.sso.SignedSessionService;
import net.toolab.utils.WebRequestUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;


public class CookieBasedSignedSessionFilter extends AbstractSignedSessionFilter {

	@SuppressWarnings("rawtypes")
	protected SignedSessionService signedSessionService;
	
	@Autowired(required = false)
	public void setSignedSessionService(@SuppressWarnings("rawtypes") SignedSessionService signedSessionService) {
		this.signedSessionService = signedSessionService;
	}
	
	@Override
	protected SignedSessionAuthenticationToken signedSession(HttpServletRequest request, HttpServletResponse response) {
		Cookie cookie = WebRequestUtils.obtainCookie(request, this.signedSessionService.getSessionParameterName());
		
		SignedSession session = null;
		
		if (cookie != null) {
			session = signedSessionService.getSession(cookie.getValue());
		}
		
		return new SignedSessionAuthenticationToken(session);
	}

	@Override
	protected void initFilterBean() throws ServletException {
		super.initFilterBean();
		Assert.notNull(signedSessionService, "SignedSessionService must be set on CookieBasedSignedSessionFilter.");
	}
}
