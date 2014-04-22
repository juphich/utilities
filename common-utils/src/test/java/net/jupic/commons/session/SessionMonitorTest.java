package net.jupic.commons.session;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.util.Date;

import net.jupic.commons.session.manager.GaSessionManager;
import net.jupic.commons.session.manager.SessionManager;
import net.jupic.commons.session.monitor.SessionMonitor;

public class SessionMonitorTest {

	private GaSessionContext context;
	
	private SessionManager sessionManager1;
	private SessionManager sessionManager2;
	
	//@Before
	public void init() {
		context = new GaSessionContext();
		
		sessionManager1 = new GaSessionManager(15);
		sessionManager2 = new GaSessionManager(20);
		
		context.addManager("sessionManager1", sessionManager1);
		context.addManager("sessionManager2", sessionManager2);
	}
	
	//@Test
	public void testSessionMonitor() throws InterruptedException {
		SessionMonitor sessionMonitor = new SessionMonitor(context);
		sessionMonitor.setIntervalSecond(1);
		
		System.out.println("start monitoring.. : " + new Date(System.currentTimeMillis()));
		sessionMonitor.start();
		
		for (int i = 0; i < 10; i++) {
			String id1 = (String) sessionManager1.createSession().getSessionId();
			System.out.println("create session manager1 : " + id1 + ", time - " + new Date(System.currentTimeMillis()));
			String id2 = (String)sessionManager2.createSession().getSessionId();
			System.out.println("create session manager2 : " + id2 + ", time - " + new Date(System.currentTimeMillis()));
			Thread.sleep(5000);
		}
		
		sessionMonitor.stop();
		System.out.println("end monitoring.. : " + new Date(System.currentTimeMillis()));
		
		int manager1 = sessionManager1.getSessions().size();
		int manager2 = sessionManager2.getSessions().size();
		System.out.println("remained sessions(manager1) : " + manager1);
		System.out.println("remained sessions(manager2) : " + manager2);
		
		assertThat(manager1, is(3));
		assertThat(manager2, is(4));
	}
}
