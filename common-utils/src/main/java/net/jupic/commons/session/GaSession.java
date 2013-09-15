package net.jupic.commons.session;

import java.io.Serializable;

/**
 * @author chang jung pil
 *
 */
public interface GaSession {
	
	Object getAttribute(String key);
	
	Serializable getSessionId();
	
	long getSessionTimeMillis();
	
	void setAttribute(String key, Serializable value);
	
	void removeAttribute(String key);
	
	SessionEntry getSessionEntry();
	
	void expire();
}
