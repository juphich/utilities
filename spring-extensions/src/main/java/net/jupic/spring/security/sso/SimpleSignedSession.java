package net.jupic.spring.security.sso;

import java.util.Date;

public class SimpleSignedSession implements SignedSession {

	private static final long serialVersionUID = 6030400934242788109L;

	private Object principal;
	
	private Object credential;
	
	private String signedSessionId;
	
	private Date createdTime;
	
	private Date expiredTime;
	
	private boolean isExpired;
	
	public SimpleSignedSession(String signedSessionId) {
		this.signedSessionId = signedSessionId;
	}

	@Override
	public String getSignedSessionId() {
		return signedSessionId;
	}

	@Override
	public Date getCreatedTime() {
		return createdTime;
	}

	@Override
	public Date getExpiredTime() {
		return expiredTime;
	}

	@Override
	public boolean isExpired() {
		return isExpired;
	}

	@Override
	public Object getPrincipal() {
		return this.principal;
	}
	
	@Override
	public Object getCredential() {
		return this.credential;
	}

	public void setPrincipal(Object principal) {
		this.principal = principal;
	}

	@Override
	public String getPrincipalAsString() {
		return principal.toString();
	}
}
