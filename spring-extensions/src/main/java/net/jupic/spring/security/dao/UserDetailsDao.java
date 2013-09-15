package net.jupic.spring.security.dao;

import java.io.Serializable;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author chang jung pil
 *
 */
public interface UserDetailsDao<U extends UserDetails, A extends GrantedAuthority> {

	/**
	 * @param identity
	 * @return
	 */
	U findUserDetailsByIdentity(Serializable identity);

	/**
	 * @param identity
	 * @return
	 */
	List<A> selectUserAuthoritiesByIdentity(Serializable identity);

	/**
	 * @param identity
	 * @return
	 */
	List<? extends GrantedAuthority> selectGroupAuthoritesByIdentity(Serializable identity);
}
