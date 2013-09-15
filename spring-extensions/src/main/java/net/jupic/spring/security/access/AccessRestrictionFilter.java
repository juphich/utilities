package net.jupic.spring.security.access;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.jupic.spring.security.access.attribute.RestrictionAttribute;

import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.Assert;
import org.springframework.web.filter.OncePerRequestFilter;


/**
 * 사용자 접근 제한 필터 (권한 인증 필터와는 역할이 다르다)
 * 
 * @author chang jung pil
 *
 */
public class AccessRestrictionFilter extends OncePerRequestFilter {

	private AccessRestrictionManager accessRestrictionManager;

	@SuppressWarnings("rawtypes")
	private AccessRestrictionSource accessRestrictionSource;
	
	
	public void setAccessRestrictionManager(AccessRestrictionManager accessRestrictionManager) {
		this.accessRestrictionManager = accessRestrictionManager;
	}

	public void setAccessRestrictionSource(AccessRestrictionSource<?> accessRestrictionSource) {
		this.accessRestrictionSource = accessRestrictionSource;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		doInspection(request, response);
		filterChain.doFilter(request, response);
	}
	
	private void doInspection(HttpServletRequest request, HttpServletResponse response) {
		@SuppressWarnings("unchecked")
		Collection<RestrictionAttribute<?>> attributes = accessRestrictionSource.getRestrictionAttributes(request.getRequestURI());
		
		if (attributes == null || attributes.isEmpty()) {
			return;
		}
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		if (SecurityContextHolder.getContext().getAuthentication() == null) {
			throw new AuthenticationCredentialsNotFoundException("An Authentication object was not found in the SecurityContext");
		}
		
		accessRestrictionManager.inspect(authentication, attributes);		
	}

	@Override
	public void afterPropertiesSet() throws ServletException {
		super.afterPropertiesSet();
		Assert.notNull(this.accessRestrictionManager, "AccessRestrictionManager must be set!!");
		Assert.notNull(this.accessRestrictionSource, "AccessRestrictionsSource must be set!!");
	}
}
