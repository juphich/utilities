package net.jupic.commons.session;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import net.jupic.commons.session.exception.SessionManagerRegistraionException;
import net.jupic.commons.session.manager.SessionManager;
import net.jupic.commons.session.monitor.SessionMonitor;


/**
 * @author chang jung pil
 *
 */
public class GaSessionContext {

	private static GaSessionContext sessionContext;
	
	private ConcurrentHashMap<String, SessionManager> sessionManagers;
	private SessionMonitor sessionMonitor;

	
	protected GaSessionContext() {
		this.sessionManagers = new ConcurrentHashMap<String, SessionManager>();
		this.sessionMonitor = new SessionMonitor(this);
	}
	
	public static synchronized void initContext() { 
		if (sessionContext == null) {
			sessionContext = new GaSessionContext();
		}
	}
	
	public static void addSessionManager(String managerName, SessionManager sessionManager) {
		if (sessionContext == null) {
			throw new SessionContextInitializingException("session context has not initialized!!");
		}
		
		if (!sessionContext.sessionManagers.containsKey(managerName)) {
			sessionContext.addManager(managerName, sessionManager);
		} else {
			throw new SessionManagerRegistraionException(managerName + " already exists!!");
		}
	}
	
	public static SessionManager getSessionManager(String managerName) {
		if (sessionContext == null) {
			throw new SessionContextInitializingException("session context has not initialized!!");
		}
		
		return sessionContext.getManager(managerName);
	}
	
	public static List<SessionManager> getSessionManagers() {
		if (sessionContext == null) {
			throw new SessionContextInitializingException("session context has not initialized!!");
		}
		
		return sessionContext.getManagers();
	}
	
	public static void setMonitoringIntervalSecond(int intervalSecond) {
		if (sessionContext == null) {
			throw new SessionContextInitializingException("session context has not initialized!!");
		}
		
		sessionContext.sessionMonitor.setIntervalSecond(intervalSecond);
	}
	
	public static void startMonitoring() {
		if (sessionContext == null) {
			throw new SessionContextInitializingException("session context has not initialized!!");
		}
		
		sessionContext.sessionMonitor.start();
	}
	
	public static void stopMonitoring() {
		if (sessionContext == null) {
			throw new SessionContextInitializingException("session context has not initialized!!");
		}
		
		sessionContext.sessionMonitor.stop();
	}
	
	protected final void addManager(String managerName, SessionManager sessionManager) {
		sessionManagers.put(managerName, sessionManager);
	}
	
	protected final SessionManager getManager(String managerName) {
		return sessionManagers.get(managerName);
	}

	public final List<SessionManager> getManagers() {
		return new ArrayList<SessionManager>(sessionManagers.values());
	}
}
