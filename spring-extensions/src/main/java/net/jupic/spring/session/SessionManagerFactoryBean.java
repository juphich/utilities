package net.jupic.spring.session;

import net.jupic.commons.session.GaSessionContext;
import net.jupic.commons.session.manager.GaSessionManager;
import net.jupic.commons.session.manager.SessionManager;
import net.jupic.commons.session.repository.InMemorySessionRepository;
import net.jupic.commons.session.repository.SessionRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * @author chang jung pil
 *
 */
public class SessionManagerFactoryBean implements FactoryBean<SessionManager>, InitializingBean, DisposableBean, BeanNameAware {

	private static Logger log = LoggerFactory.getLogger(SessionManagerFactoryBean.class);
	
	private SessionManager sessionManager;

	private String  managerName = "sessionManager";
	private boolean initializing = true;
	private boolean startMonitoring = false;
	
	private SessionRepository sessionRepository;
	
	private long sessionTimeoutSecond = 1800;
	private int  intervalSecond = 60;
	
	@Autowired(required=false)
	public void setSessionRepository(SessionRepository sessionAttrRepository) {
		this.sessionRepository = sessionAttrRepository;
	}
	
	public void setInitializing(boolean initailizing) {
		this.initializing = initailizing;
	}

	public void setSessionTimeoutSecond(long sessionTimeoutSecond) {
		this.sessionTimeoutSecond = sessionTimeoutSecond;
	}

	public void setIntervalSecond(int intervalSecond) {
		this.intervalSecond = intervalSecond;
	}

	public void setStartMonitoring(boolean startMonitoring) {
		this.startMonitoring = startMonitoring;
	}

	@Override
	public SessionManager getObject() throws Exception {
		return this.sessionManager;
	}

	@Override
	public Class<? extends SessionManager> getObjectType() {
		return GaSessionManager.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}
	
	@Override
	public void setBeanName(String beanName) {
		managerName = beanName;
	}
	
	private void createSessionManager() {
		GaSessionManager sessionManager = new GaSessionManager(sessionTimeoutSecond);
		
		if (sessionRepository != null) {
			if (initializing) {
				sessionRepository.initialize();
			}
			sessionManager.setSessionRepository(sessionRepository);
			log.info("Session Repository has been initialized...");
		}
		
		GaSessionContext.initContext();
		GaSessionContext.setMonitoringIntervalSecond(intervalSecond);
		GaSessionContext.addSessionManager(managerName, sessionManager);
		log.info("Session Manager({}) has been registered...", managerName);
		this.sessionManager = sessionManager;
		
		if (startMonitoring) {
			GaSessionContext.startMonitoring();
			log.info("Session monitor has been started...");
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if (sessionRepository == null) {
			log.info("SessionAttributeRepository has not been set. it will use InMemorySessionAttributeRepository.");
			this.sessionRepository = new InMemorySessionRepository();
		}
		
		createSessionManager();
	}

	@Override
	public void destroy() throws Exception {
		GaSessionContext.stopMonitoring();
		log.info("Session monitor has been stoped...");
	}
}
