package net.jupic.spring.security.access;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.jupic.spring.security.access.attribute.RestrictionAttribute;
import net.jupic.spring.security.access.matcher.TargetMatcher;


public abstract class AbstractAccessRestrictionSoruce<R> implements AccessRestrictionSource<R> {

	private Map<TargetMatcher, RestrictionAttribute<R>> restrictionAttributes;
	
	public AbstractAccessRestrictionSoruce() {
		restrictionAttributes = new HashMap<TargetMatcher, RestrictionAttribute<R>>();
	}
	
	@Override
	public void setRestrictionAttribute(RestrictionAttribute<R> attribute) {
		this.restrictionAttributes.put(attribute.getTargetMatcher(), attribute);
	}
	
	@Override
	public Collection<RestrictionAttribute<R>> getRestrictionAttributes() {
		return Collections.unmodifiableCollection(restrictionAttributes.values());
	}

	@Override
	public Collection<RestrictionAttribute<R>> getRestrictionAttributes(String pattern) {
		List<RestrictionAttribute<R>> list = new ArrayList<RestrictionAttribute<R>>();
		
		for (Entry<TargetMatcher, RestrictionAttribute<R>> entry : restrictionAttributes.entrySet()) {
			if (entry.getKey().matches(pattern)) {
				list.add(entry.getValue());
			}
		}
		
		return Collections.unmodifiableList(list);
	}
}
