package net.jupic.spring.security.sso;

public interface SignedSessionService<P> {
	final String DEFAULT_SESSION_PARAM_NAME = "ssoSessionId";
	
	String createSession(P principal);
	
	String getSessionParameterName();
	
	SignedSession getSession(String signedSessionId);
	
	void expireSession(String signedSessionId);
}
