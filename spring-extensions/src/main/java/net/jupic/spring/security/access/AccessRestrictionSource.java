package net.jupic.spring.security.access;

import java.util.Collection;

import net.jupic.spring.security.access.attribute.RestrictionAttribute;


public interface AccessRestrictionSource<R> {

	void setRestrictionAttribute(RestrictionAttribute<R> attribute);

	Collection<RestrictionAttribute<R>> getRestrictionAttributes();

	Collection<RestrictionAttribute<R>> getRestrictionAttributes(String pattern);

}