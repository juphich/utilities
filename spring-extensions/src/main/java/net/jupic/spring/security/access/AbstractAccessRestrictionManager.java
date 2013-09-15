package net.jupic.spring.security.access;

import java.util.Collection;

import net.jupic.spring.security.access.attribute.RestrictionAttribute;

import org.springframework.security.core.Authentication;


public abstract class AbstractAccessRestrictionManager implements AccessRestrictionManager {

	@Override
	public void inspect(Authentication authentication, Collection<RestrictionAttribute<?>> attributes) {
		if (supports(authentication.getClass())) {
			
		}
	}
}
