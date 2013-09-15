package net.jupic.spring.security.service;

import java.util.Collection;

import net.jupic.spring.security.exception.UserDetailsCreationException;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


/**
 * @author chang jung pil
 *
 */
public interface AuthorityBinder <U extends UserDetails, A extends GrantedAuthority> {

	/**
	 * @param userDetails
	 * @param authorities
	 * @return
	 */
	U bindToUserDetails(U userDetails, Collection<A> authorities) throws UserDetailsCreationException;

}
