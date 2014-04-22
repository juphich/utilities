package net.jupic.commons.session.repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import net.jupic.commons.session.GaSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * @author chang jung pil
 *
 */
public class InMemorySessionRepository implements SessionRepository {

private static Logger logger = LoggerFactory.getLogger(InMemorySessionRepository.class);
	
	private Map<Serializable, GaSession> sessions;

	public InMemorySessionRepository() {
		this.sessions = new ConcurrentHashMap<Serializable, GaSession>();
	}
	
	@Override
	public void initialize() {
		this.sessions.clear();
	}

	@Override
	public synchronized void setSession(GaSession session) {
		sessions.put(session.getSessionId(), session);
	}

	@Override
	public GaSession getSession(Serializable sessionId) {
		return sessions.get(sessionId);
	}

	@Override
	public List<GaSession> getSessions() {
		return new ArrayList<GaSession>(sessions.values());
	}

	@Override
	public void expireSession(Serializable sessionId) {
		GaSession session = sessions.get(sessionId);
		if (session != null) {
			session.expire();
			synchronized(sessions) {
				sessions.remove(sessionId);
			}
		}
	}

	@Override
	public void clear(long sessionTimeoutSecond) {
		for (Entry<Serializable, GaSession> entry : sessions.entrySet()) {
			long sessionTime = entry.getValue().getSessionTimeMillis();
			
			boolean isExpired = (System.currentTimeMillis() - sessionTime) > (sessionTimeoutSecond * 1000);
			
			if (isExpired) {
				synchronized(sessions) {
					sessions.remove(entry.getKey());
				}
				
				if (logger.isDebugEnabled()) {
					logger.debug("session ({}) has been expired!! time : {}", entry.getKey(), new Date(System.currentTimeMillis()));
				}
			}
		}
	}
}
