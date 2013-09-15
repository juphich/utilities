package net.jupic.commons.session;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Set;

import net.jupic.commons.session.key.SimpleSessionKeyGenerator;
import net.jupic.commons.session.repository.SessionRepository;


/**
 * @author chang jung pil
 *
 */
public class DefaultGaSession implements GaSession {

	private SessionRepository sessionRepository;
	private Serializable sessionId;
	private long sessionTime;
	
	private InternalSesionEntry sessionEntry;
	
	public DefaultGaSession(SessionRepository sessionRepository) {
		this(new SimpleSessionKeyGenerator().generateKey(), sessionRepository);
	}
	
	public DefaultGaSession(Serializable sessionId, SessionRepository sessionRepository) {
		this.sessionId = sessionId;
		this.sessionRepository = sessionRepository;
		this.sessionEntry = new InternalSesionEntry();
		renewSessionTime();
	}

	@Override
	public Serializable getSessionId() {
		return sessionId;
	}
	
	@Override
	public Object getAttribute(String key) {
		return sessionEntry.getAttribute(key);
	}

	@Override
	public void setAttribute(String key, Serializable value) {
		sessionEntry.setAttribute(key, value);
		renewSessionTime();
		sessionRepository.setSession(this);
	}
	
	public void bindEntry(SessionEntry sessionEntry) {
		for (String key : sessionEntry.keySet()) {
			this.sessionEntry.setAttribute(key, sessionEntry.getAttribute(key));
		}
	}

	@Override
	public void removeAttribute(String key) {
		sessionEntry.removeAttribute(key);
		renewSessionTime();
		sessionRepository.setSession(this);
	}

	@Override
	public void expire() {
		sessionRepository.expireSession(sessionId);
	}
	
	@Override
	public SessionEntry getSessionEntry() {
		return this.sessionEntry;
	}

	@Override
	public long getSessionTimeMillis() {
		return sessionTime;
	}

	@Override
	public int hashCode() {
		int prime = 79;
		
		return prime + sessionId.hashCode() * 22;
	}
	
	private void renewSessionTime() {
		sessionTime = System.currentTimeMillis();
	}
}

class InternalSesionEntry implements SessionEntry {
	private static final long serialVersionUID = 4312470763246329061L;
	
	private HashMap<String, Serializable> attributes = new HashMap<String, Serializable>();
	
	@Override
	public Serializable getAttribute(String key) {
		return attributes.get(key);
	}
	
	@Override
	public Set<String> keySet() {
		return attributes.keySet();
	}
	
	void setAttribute(String key, Serializable value) {
		attributes.put(key, value);
	}
	
	void removeAttribute(String key) {
		attributes.remove(key);
	}
}
