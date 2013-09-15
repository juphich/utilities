package net.jupic.spring.security.sso.authentication;

import net.jupic.spring.security.sso.SignedSession;
import net.jupic.spring.security.sso.SignedSessionAuthenticationToken;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.Assert;


public class SingleSignOnAuthenticationProvider extends DaoAuthenticationProvider {

	/**
	 * @param authentication
	 * @return
	 * @throws AuthenticationException
	 */
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		
		Assert.isInstanceOf(SignedSessionAuthenticationToken.class, authentication, 
				messages.getMessage("SingleSignOnAuthenticationProvider.onlySupports",
						"Only SignedSessionAuthenticationToken is supported."));
		
		SignedSession session = ((SignedSessionAuthenticationToken) authentication).getSignedSession();
		
		if (session == null) {
			throw new BadCredentialsException(
					messages.getMessage("SingleSignOnAuthenticationProvider.noSessionId",
							"there is no session value. try to login."));
		}
		
		String username = session.getPrincipalAsString();
		UserDetails user = null;
		
		try {
            user = retrieveUser(username, null);
        } catch (UsernameNotFoundException notFound) {
            logger.debug("User '" + username + "' not found");

            if (hideUserNotFoundExceptions) {
                throw new BadCredentialsException(messages.getMessage(
                        "AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
            } else {
                throw notFound;
            }
        }

        Assert.notNull(user, "retrieveUser returned null - a violation of the interface contract");
        
        try {
        	getPreAuthenticationChecks().check(user);
        	additionalAuthenticationChecks(user, (SignedSessionAuthenticationToken)authentication);
        } catch (AuthenticationException exception) {
        	throw exception;
        }

        getPostAuthenticationChecks().check(user);
		
		Object principalToReturn = user; 
		return createSuccessAuthentication(principalToReturn, authentication, user);
	}
	
	@Override
	protected final void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication)
			throws AuthenticationException {
		throw new UnsupportedOperationException("this method does not support on SsoSessionProvider.");
	}


	/**
	 * @param userDetails
	 * @param authentication
	 * @throws AuthenticationException
	 */
	protected void additionalAuthenticationChecks(UserDetails userDetails, SignedSessionAuthenticationToken authentication) 
			throws AuthenticationException {
		//no ckecking anymore
	}
	
	/**
	 * @param authentication
	 * @return
	 */
	@Override
	public boolean supports(Class<?> authentication) {
		return (SignedSessionAuthenticationToken.class.isAssignableFrom(authentication));
	}
}
