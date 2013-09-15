package net.jupic.spring.security.sso;

import java.io.Serializable;
import java.util.Date;

public interface SignedSession extends Serializable {

	String getSignedSessionId();

	Date getCreatedTime();

	Date getExpiredTime();

	boolean isExpired();

	Object getPrincipal();

	Object getCredential();

	String getPrincipalAsString();

}