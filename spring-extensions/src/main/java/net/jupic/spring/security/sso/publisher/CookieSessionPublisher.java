package net.jupic.spring.security.sso.publisher;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.jupic.spring.security.sso.SignedSessionService;
import net.toolab.utils.WebRequestUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CookieSessionPublisher extends AbstractSessionPublisher {

	protected Logger log = LoggerFactory.getLogger(getClass());
	
	private String cookieDomain;
	private String cookiePath;
	private boolean expireCookie = true;

	public CookieSessionPublisher(@SuppressWarnings("rawtypes") SignedSessionService sessionService) {
		super(sessionService);
	}
	
	public CookieSessionPublisher(@SuppressWarnings("rawtypes") SignedSessionService sessionService,
								  String cookieDomain,
								  String cookiePath) {
		super(sessionService);
		this.cookieDomain = cookieDomain;
		this.cookiePath = cookiePath;
	}
	
	public String getCookiePath() {
		return cookiePath;
	}
	
	public String getCookieDomain() {
		return this.cookieDomain;
	}

	public void setCookieDomain(String cookieDomain) {
		this.cookieDomain = cookieDomain;
	}

	public void setCookiePath(String cookiePath) {
		this.cookiePath = cookiePath;
	}

	public void setExpireCookie(boolean expireCookie) {
		this.expireCookie = expireCookie;
	}

	@Override
	public void publish(Object principal, HttpServletRequest request, HttpServletResponse response) {
		@SuppressWarnings("unchecked")
		String signedSessionId = getSessionService().createSession(principal);
		Cookie cookie = bakeCookie(getSessionService().getSessionParameterName(), signedSessionId);
		response.addCookie(cookie);
	}
	
	@Override
	public void expire(HttpServletRequest request, HttpServletResponse response) {
		Cookie cookie = WebRequestUtils.obtainCookie(request, getSessionService().getSessionParameterName());
		if (cookie != null) {
			getSessionService().expireSession(cookie.getValue());
			
			if (log.isDebugEnabled()) {
				log.debug("expire session.... {}", cookie.getValue());
			}		
		}
		
		if (expireCookie) {
			Cookie expire = bakeCookie(getSessionService().getSessionParameterName(), "");
			expire.setMaxAge(0);
			response.addCookie(expire);
		}
	}
	
	private Cookie bakeCookie(String key, String value) {
		Cookie cookie = new Cookie(key, value);
		
		if (getCookiePath() != null && !getCookiePath().isEmpty()) {
			cookie.setPath(getCookiePath());
		}
		if (getCookieDomain() != null && !getCookieDomain().isEmpty()) {
			cookie.setDomain( "." + getCookieDomain());
		}
		return cookie;
	}
}
