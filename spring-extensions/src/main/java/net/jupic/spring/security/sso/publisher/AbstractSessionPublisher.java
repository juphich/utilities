package net.jupic.spring.security.sso.publisher;

import net.jupic.spring.security.sso.SignedSessionService;

public abstract class AbstractSessionPublisher implements SessionPublisher {

	@SuppressWarnings("rawtypes")
	private SignedSessionService sessionService;
	
	@SuppressWarnings("rawtypes")
	public AbstractSessionPublisher(SignedSessionService sessionService) {
		this.sessionService = sessionService;
	}
	
	@SuppressWarnings("rawtypes")
	public SignedSessionService getSessionService() {
		return sessionService;
	}
}
