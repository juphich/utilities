package net.jupic.spring.security.sso.filter;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.jupic.spring.security.sso.SignedSessionAuthenticationToken;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;


public abstract class AbstractSingleSignOnAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

	
	protected AbstractSingleSignOnAuthenticationFilter() {
		super("/j_spring_sso_security_filter");
	}
	
	protected AbstractSingleSignOnAuthenticationFilter(String defaultFilterProcessesUrl) {
		super(defaultFilterProcessesUrl);
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException, ServletException {
		
		Authentication singedAuthenticationToken = obtainSignedAuthenticationToken(request, response);
		return this.getAuthenticationManager().authenticate(singedAuthenticationToken);
	}

	protected abstract SignedSessionAuthenticationToken obtainSignedAuthenticationToken(HttpServletRequest request, HttpServletResponse response);
}
