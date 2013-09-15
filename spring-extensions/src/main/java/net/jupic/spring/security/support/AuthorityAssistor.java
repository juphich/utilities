package net.jupic.spring.security.support;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.util.Assert;

public final class AuthorityAssistor {

	public static <A extends GrantedAuthority> SortedSet<A> sortAuthorities(Collection<A> authorities) {
		return sortAuthorities(authorities, new AuthorityComparator<A>());
	}
	
	public static <A extends GrantedAuthority> SortedSet<A> sortAuthorities(Collection<A> authorities, Comparator<A> comparator) {
        Assert.notNull(authorities, "Cannot pass a null GrantedAuthority collection");
        // Ensure array iteration order is predictable (as per UserDetails.getAuthorities() contract and SEC-717)
        SortedSet<A> sortedAuthorities = new TreeSet<A>(comparator);

        for (A grantedAuthority : authorities) {
            Assert.notNull(grantedAuthority, "GrantedAuthority list cannot contain any null elements");
            sortedAuthorities.add(grantedAuthority);
        }

        return sortedAuthorities;
    }
	
	public static <A extends GrantedAuthority> List<A> noAuthority() {
		return Collections.emptyList();
	}

	private static class AuthorityComparator<A extends GrantedAuthority> implements Comparator<A>, Serializable {
        private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

        public int compare(A g1, A g2) {
            // Neither should ever be null as each entry is checked before adding it to the set.
            // If the authority is null, it is a custom authority and should precede others.
            if (g2.getAuthority() == null) {
                return -1;
            }

            if (g1.getAuthority() == null) {
                return 1;
            }

            return g1.getAuthority().compareTo(g2.getAuthority());
        }
    }
}
