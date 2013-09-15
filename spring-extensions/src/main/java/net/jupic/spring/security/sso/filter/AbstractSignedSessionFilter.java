package net.jupic.spring.security.sso.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.jupic.spring.security.sso.SignedSession;
import net.jupic.spring.security.sso.SignedSessionAuthenticationToken;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.Assert;
import org.springframework.web.filter.OncePerRequestFilter;


public abstract class AbstractSignedSessionFilter extends OncePerRequestFilter {

	protected final Logger log = LoggerFactory.getLogger(getClass());
	protected AuthenticationDetailsSource<HttpServletRequest,?> authenticationDetailsSource = new WebAuthenticationDetailsSource();
	protected AuthenticationManager authenticationManager;
	protected AuthenticationFailureHandler authenticationFailureHandler;
	
	private SessionStatus sessionStatus = new DefaultSessionStatus();
	
	private boolean invalidateHttpSession = true;
	
	public void setSessionStatus(SessionStatus sessionStatus) {
		this.sessionStatus = sessionStatus;
	}

	public void setInvalidateHttpSession(boolean invalidateHttpSession) {
        this.invalidateHttpSession = invalidateHttpSession;
    }

	@Autowired(required = false)
	public void setAuthenticationManager(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}
	
	public void setAuthenticationDetailsSource(AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource) {
		Assert.notNull(authenticationDetailsSource, "AuthenticationDetailsSource required");
		this.authenticationDetailsSource = authenticationDetailsSource;
	}

	public void setAuthenticationFailureHandler(AuthenticationFailureHandler authenticationFailureHandler) {
		this.authenticationFailureHandler = authenticationFailureHandler;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		SignedSessionAuthenticationToken authenticationToken = signedSession(request, response);
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		SignedSession session = authenticationToken.getSignedSession();
		if (sessionStatus.isRequiredSignOn(session, authentication)) {
			authenticationToken.setDetails(authenticationDetailsSource.buildDetails(request));
			
			try {
				signOn(request, response, authenticationToken);
			} catch (AuthenticationException e) {
				signOnAsAnonymous(authenticationToken);
				if (authenticationFailureHandler != null) {
					authenticationFailureHandler.onAuthenticationFailure(request, response, e);
					return;
				}
			}
		} else if (sessionStatus.isRequiredSignOff(session, authentication)) {
			signOff(request, response);
		}
		
		chain.doFilter(request, response);
	}
	
	protected abstract SignedSessionAuthenticationToken signedSession(HttpServletRequest request, HttpServletResponse response);
	
	protected Authentication signOn(HttpServletRequest request, HttpServletResponse response, Authentication authenticationToken) {
		Authentication authentication = authenticationManager.authenticate(authenticationToken);
		if (log.isDebugEnabled()) {
			log.debug("Automatic login with cookie session.. ");
		}
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
		return authentication;
	}
	
	protected void signOff(HttpServletRequest request, HttpServletResponse response) {
		if (invalidateHttpSession) {
            HttpSession session = request.getSession(false);
            if (session != null) {
                log.debug("Invalidating session: " + session.getId());
                session.invalidate();
            }
        }

        SecurityContextHolder.clearContext();
        
		if (log.isDebugEnabled()) {
			log.debug("Automatic logout with cookie session.. ");
		}
	}
	
	private void signOnAsAnonymous(Authentication authenticationToken) {
		Authentication authentication = new AnonymousAuthenticationToken(
				"BFIWJ547284GDERTFWER", 
				((SignedSessionAuthenticationToken) authenticationToken).getSignedSession(),
				AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS"));
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}

	@Override
	protected void initFilterBean() throws ServletException {
		super.initFilterBean();
		Assert.notNull(authenticationManager, "authentication manager has not been set!! a authentication manager must be set!!");
		Assert.notNull(sessionStatus, "session status bean must be set!!");
	}
	
	private class DefaultSessionStatus implements SessionStatus {

		@Override
		public boolean isRequiredSignOn(SignedSession session, Authentication authentication) {
			return (session != null && !session.isExpired()) 
					&& (authentication == null || !authentication.isAuthenticated());
		}

		@Override
		public boolean isRequiredSignOff(SignedSession session, Authentication authentication) {
			return (authentication != null && authentication.isAuthenticated()) 
					&& (session == null || session.isExpired());
		}
		
	}
}
