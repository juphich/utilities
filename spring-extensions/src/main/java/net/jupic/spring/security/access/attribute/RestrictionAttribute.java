package net.jupic.spring.security.access.attribute;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import net.jupic.spring.security.access.matcher.TargetMatcher;


public class RestrictionAttribute<R> {

	private TargetMatcher matcher;
	private Set<R> restrictions;
	
	public RestrictionAttribute(TargetMatcher matcher, Collection<R> restrictions) {
		this.matcher = matcher;
		this.restrictions = new HashSet<R>(restrictions);
	}
	
	@SafeVarargs
	public RestrictionAttribute(TargetMatcher matcher, R... restrictions) {
		this(matcher, Arrays.asList(restrictions));
	}
	
	public TargetMatcher getTargetMatcher() {
		return this.matcher;
	}
	
	public Collection<R> getRestrictions() {
		return Collections.unmodifiableSet(restrictions);
	}
}
