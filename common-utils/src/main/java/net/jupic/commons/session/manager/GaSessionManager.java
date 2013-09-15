package net.jupic.commons.session.manager;

import java.io.Serializable;
import java.util.List;

import net.jupic.commons.session.DefaultGaSession;
import net.jupic.commons.session.GaSession;
import net.jupic.commons.session.exception.InvalideSessionException;
import net.jupic.commons.session.repository.InMemorySessionRepository;
import net.jupic.commons.session.repository.SessionRepository;


/**
 * @author chang jung pil
 *
 */
public class GaSessionManager implements SessionManager {

	private SessionRepository sessionRepository;

	private long sessionTimeoutSecond;
	
	public GaSessionManager() {
		this(1800);
	}
	
	public GaSessionManager(long sessionTimeoutSecond) {
		this(new InMemorySessionRepository(), sessionTimeoutSecond);
	}
	
	public GaSessionManager(SessionRepository sessionRepository) {
		this(sessionRepository, 1800);
	}
	
	public GaSessionManager(SessionRepository sessionRepository, long sessionTimeoutSecond) {
		this.sessionRepository = sessionRepository;
		this.sessionTimeoutSecond = sessionTimeoutSecond;
	}

	public void setSessionRepository(SessionRepository sessionRepository) {
		this.sessionRepository = sessionRepository;
	}

	@Override
	public GaSession getSession(Serializable sessionId) {
		GaSession session = sessionRepository.getSession(sessionId);
			
		if (session != null) {
			return session;
		} else {
			throw new InvalideSessionException("session dose not exist.");
		}
	}
	
	@Override
	public List<GaSession> getSessions() {
		return sessionRepository.getSessions();
	}
	
	@Override
	public GaSession createSession() {
		GaSession session = new DefaultGaSession(sessionRepository);
		sessionRepository.setSession(session);
		return session;
	}

	@Override
	public void expireSession(Serializable sessionId) {
		sessionRepository.expireSession(sessionId);
	}

	@Override
	public void expireAll() {
		sessionRepository.initialize();
	}
	
	@Override
	public void clearExpired() {
		sessionRepository.clear(sessionTimeoutSecond);
	}

	@Override
	public long getSessionTimeoutSecond() {
		return sessionTimeoutSecond;
	}
}
