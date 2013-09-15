package net.jupic.spring.security.sso.filter;

import net.jupic.spring.security.sso.SignedSession;

import org.springframework.security.core.Authentication;


public interface SessionStatus {

	boolean isRequiredSignOn(SignedSession session, Authentication authentication);
	
	boolean isRequiredSignOff(SignedSession session, Authentication authentication);
}
