package net.jupic.commons.session.manager;

import java.io.Serializable;
import java.util.List;

import net.jupic.commons.session.GaSession;


/**
 * @author chang jung pil
 *
 */
public interface SessionManager {

	GaSession getSession(Serializable sessionId);

	GaSession createSession();
	
	void expireSession(Serializable sessionId);

	void expireAll();
	
	void clearExpired();

	List<GaSession> getSessions();

	long getSessionTimeoutSecond();
}