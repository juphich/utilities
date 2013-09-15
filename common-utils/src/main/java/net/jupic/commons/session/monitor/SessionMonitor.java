package net.jupic.commons.session.monitor;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import net.jupic.commons.session.GaSessionContext;
import net.jupic.commons.session.manager.SessionManager;


/**
 * @author chang jung pil
 *
 */
public class SessionMonitor {
	private GaSessionContext sessionContext;
	private ConcurrentHashMap<String, MonitorWorker> threadMap;
	
	private int intervalSecond = 60;
	
	public SessionMonitor(GaSessionContext context) {
		this.sessionContext = context;
		this.threadMap = new ConcurrentHashMap<String, MonitorWorker>();
	}

	public int getIntervalSecond() {
		return intervalSecond;
	}

	public void setIntervalSecond(int intervalSecond) {
		this.intervalSecond = intervalSecond;
	}

	public void start() {
		List<SessionManager> managers = sessionContext.getManagers();
		int idx = 1;
		for (SessionManager manager : managers) {
			MonitorWorker worker = new MonitorWorker("monitor-worker-" + idx, manager, intervalSecond);
			worker.start();
			
			threadMap.put(worker.getName(), worker);
			idx++;
		}
	}

	public void stop() {
		Collection<MonitorWorker> workers = threadMap.values();
		for (MonitorWorker worker : workers) {
			worker.stop();
		}
	}
}
