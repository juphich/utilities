package net.jupic.commons.session.monitor;

import net.jupic.commons.session.manager.SessionManager;

/**
 * @author chang jung pil
 *
 */
class MonitorWorker implements Runnable {	
	private SessionManager sessionManager;
	private boolean stopped = false;
	private boolean suspended = false;
	private int intervalSecond;
	
	private Thread workerThread;
	
	MonitorWorker(String name, SessionManager sessionManager) {
		this(name, sessionManager, 60);
	}
	
	MonitorWorker(String name, SessionManager sessionManager, int intervalSecond) {
		this.sessionManager = sessionManager;
		this.workerThread = new Thread(this, name);
		this.workerThread.setDaemon(true);
		this.intervalSecond = intervalSecond;
	}
	
	public String getName() {
		return workerThread.getName();
	}
	
	@Override
	public void run() {
		long interval = intervalSecond * 1000;
		
		while (!stopped) {
			if (!suspended) {
				try {
					Thread.sleep(interval);
				} catch (InterruptedException e) { }
			} else {
				Thread.yield();
			}
			
			expireSession();
		}
	}
	
	private void expireSession() {
		sessionManager.clearExpired();
	}
	
	public void start() {
		workerThread.start();
	}
	
	public void suspend() {
		this.suspended = true;
		workerThread.interrupt();
	}
	
	public void resume() {
		this.suspended = false;
	}
	
	public void stop() {
		this.stopped = true;
		workerThread.interrupt();
	}
}
