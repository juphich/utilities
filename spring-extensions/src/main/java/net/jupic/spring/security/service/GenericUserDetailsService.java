package net.jupic.spring.security.service;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import net.jupic.spring.security.dao.UserDetailsDao;
import net.jupic.spring.security.domain.GenericGrantedAuthority;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.Assert;


/**
 * @author chang jung pil
 *
 */
public class GenericUserDetailsService implements UserDetailsService, InitializingBean {

	private UserDetailsDao<? extends UserDetails, ? extends GrantedAuthority> userDetailsDao;
	
	@SuppressWarnings("rawtypes")
	private AuthorityBinder authorityBinder;
	
	private String rolePrefix;
	private boolean enableGroups;
	private boolean enableAuthorities = true;

	/**
	 * @param userDetailsDao data access object for user detail infomations
	 */
	//@Autowired(required=false)
	public void setUserDetailsDao(UserDetailsDao<? extends UserDetails, ? extends GrantedAuthority> userDetailsDao) {
		this.userDetailsDao = userDetailsDao;
	}
	
	/**
	 * @param authorityBinder the userDetailTemplate to set
	 */
	@Autowired(required=false)
	public void setAuthorityBinder(@SuppressWarnings("rawtypes") AuthorityBinder authorityBinder) {
		this.authorityBinder = authorityBinder;
	}
	
	public UserDetailsDao<? extends UserDetails, ? extends GrantedAuthority> getUserDetailsDao() {
		return userDetailsDao;
	}

	@SuppressWarnings("rawtypes")
	public AuthorityBinder getAuthorityBinder() {
		return authorityBinder;
	}

	/**
	 * @param arg0
	 * @return
	 * @throws UsernameNotFoundException
	 */
	@Override
	public UserDetails loadUserByUsername(String principal) throws UsernameNotFoundException {
		UserDetails userDetails = findUserDetailsByIdentity(principal);
		
		Collection<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
		
		if (userDetails == null) {
			throw new UsernameNotFoundException("It's not correct principal. check user identity : " + principal);
		}
		
		if (enableAuthorities) {
			authorities.addAll(retrieveUserAuthorities(principal));
		}
		
		if (enableGroups) {
			authorities.addAll(retrieveGroupAuthorities(principal));
		}
		
		if (authorities.size() == 0) {
			throw new UsernameNotFoundException("User(" + principal + ") has no authorities.");
		}
		
		if (rolePrefix != null && !rolePrefix.equals("")) {
			addPrefixWithAutorities(authorities);
		}
		
		return createUserDetailsWithAuthorities(userDetails, authorities);
	}
	
	/**
	 * @param authorities
	 */
	private void addPrefixWithAutorities(Collection<GrantedAuthority> authorities) {
		for (GrantedAuthority authority : authorities) {
			((GenericGrantedAuthority)authority).addRolePrefix(rolePrefix);
		}
	}

	/**
	 * @param userDetails
	 * @param authorities
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private UserDetails createUserDetailsWithAuthorities(UserDetails userDetails,
			Collection<? extends GrantedAuthority> authorities) {
		
		return authorityBinder.bindToUserDetails(userDetails, authorities);
	}

	/**
	 * @param identity user identity, user principal
	 * @return user details object
	 */
	private UserDetails findUserDetailsByIdentity(String identity) {
		return userDetailsDao.findUserDetailsByIdentity(identity);
	}
	
	/**
	 * @param identity user identity, user principal
	 * @return granted authorities
	 */
	private List<? extends GrantedAuthority> retrieveUserAuthorities(String identity) {
		return userDetailsDao.selectUserAuthoritiesByIdentity(identity);
	}
	
	/**
	 * @param identity user identity, user principal
	 * @return granted authorities as group
	 */
	private List<? extends GrantedAuthority> retrieveGroupAuthorities(String identity) {
		return userDetailsDao.selectGroupAuthoritesByIdentity(identity);
	}

	/**
	 * @return the rolePrefix
	 */
	public String getRolePrefix() {
		return rolePrefix;
	}

	/**
	 * @return the enableGroups
	 */
	public boolean isEnableGroups() {
		return enableGroups;
	}

	/**
	 * @return the enableAuthorities
	 */
	public boolean isEnableAuthorities() {
		return enableAuthorities;
	}

	/**
	 * @param rolePrefix the rolePrefix to set
	 */
	public void setRolePrefix(String rolePrefix) {
		this.rolePrefix = rolePrefix;
	}

	/**
	 * @param enableGroups the enableGroups to set
	 */
	public void setEnableGroups(boolean enableGroups) {
		this.enableGroups = enableGroups;
	}

	/**
	 * @param enableAuthorities the enableAuthorities to set
	 */
	public void setEnableAuthorities(boolean enableAuthorities) {
		this.enableAuthorities = enableAuthorities;
	}

	/**
	 * @throws Exception
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(userDetailsDao, "Concreate UserDetailsDao has to be injected..");
		Assert.notNull(authorityBinder, "Concreate AuthorityBinder has to be injected..");
	}
}
