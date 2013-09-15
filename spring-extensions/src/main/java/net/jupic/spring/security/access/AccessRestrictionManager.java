package net.jupic.spring.security.access;

import java.util.Collection;

import net.jupic.spring.security.access.attribute.RestrictionAttribute;

import org.springframework.security.core.Authentication;


public interface AccessRestrictionManager {

	boolean supports(Class<?> type);
	
	void inspect(Authentication authentication, Collection<RestrictionAttribute<?>> attributes);

}