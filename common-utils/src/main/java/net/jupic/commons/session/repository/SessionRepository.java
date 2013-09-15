package net.jupic.commons.session.repository;

import java.io.Serializable;
import java.util.List;

import net.jupic.commons.session.GaSession;


/**
 * @author chang jung pil
 *
 */
public interface SessionRepository {
	void initialize();

	void setSession(GaSession session);

	GaSession getSession(Serializable sessionId);

	List<GaSession> getSessions();

	void expireSession(Serializable sessionId);

	void clear(long sessionTimeoutSecond);
}
