package net.jupic.spring.security.domain;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author chang jung pil
 *
 */
public class LoginFailureInfo implements Serializable {

	private static final long serialVersionUID = 2596214466525529817L;

	private static final String FAIL_INFO = "FailureInfo";
	
	private Map<String, Object> failureInfos = new HashMap<String, Object>();
	
	private String retryUrl;
	
	public void setInfo(String key, Object value) {
		failureInfos.put(key, value);
	}
	
	public void setRetryUrl(String retryUrl) {
		this.retryUrl = retryUrl;
	}
	
	public String getRetryUrl() {
		return retryUrl;
	}
	
	public Object getInfo(String key) {
		return failureInfos.get(key);
	}
	
	public Map<String, Object> getInfoMap() {
		return failureInfos;
	}
	
	public void register(HttpServletRequest request) {
		HttpSession session = request.getSession();
		session.setAttribute(FAIL_INFO, this);
	}
	
	public static LoginFailureInfo get(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session != null) {
			return (LoginFailureInfo) session.getAttribute(FAIL_INFO);
		}
		
		return null;
	}
	
	public static void clear(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.removeAttribute(FAIL_INFO);
		}
	}
}
