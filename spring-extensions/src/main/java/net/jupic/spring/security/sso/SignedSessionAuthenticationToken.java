package net.jupic.spring.security.sso;

import org.springframework.security.authentication.AbstractAuthenticationToken;

public class SignedSessionAuthenticationToken extends AbstractAuthenticationToken {

	private static final long serialVersionUID = 5244037378263121247L;
	
	private SignedSession signedSession;
	private Object principal;
	
	public SignedSessionAuthenticationToken(SignedSession session) {
		super(null);
		this.signedSession = session;
	}
	
	public SignedSession getSignedSession() {
		return signedSession;
	}

	/**
	 * @return
	 */
	@Override
	public Object getCredentials() {
		return null;
	}

	/**
	 * @return
	 */
	@Override
	public Object getPrincipal() {
		return principal;
	}
	
	@Override
	public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        if (isAuthenticated) {
            throw new IllegalArgumentException(
                "Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        }

        super.setAuthenticated(false);
    }
	
	/**
	 * @param principal the principal to set
	 */
	public void setPrincipal(Object principal) {
		this.principal = principal;
	}
}
