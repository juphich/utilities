package net.jupic.spring.security.domain;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import net.jupic.spring.security.support.AuthorityAssistor;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;


/**
 * @author chang jung pil
 *
 */
public class GenericUserDetails implements UserDetails {

	private static final long serialVersionUID = -6850537297785728060L;
	
	private String principal;
	private String credential;
	private boolean enabled;
	private boolean accountNonExpired;
	private boolean accountNonLocked;
	private boolean credentialsNonExpired;

	private Set<? extends GrantedAuthority> grantedAuthorities;
	
	/**
	 * @param principal
	 * @param credential
	 * @param enabled
	 */
	public GenericUserDetails(String principal, String credential, boolean enabled) {
		this(principal, credential, enabled, true, true, true, AuthorityUtils.NO_AUTHORITIES);
	}
	
	/**
	 * @param principal
	 * @param credential
	 * @param enabled
	 * @param authorities
	 */
	public GenericUserDetails(String principal,
							  String credential,
							  boolean enabled,
							  Collection<? extends GrantedAuthority> authorities) {
		this(principal, credential, enabled, true, true, true, authorities);
	}
	
	/**
	 * @param principal
	 * @param credential
	 * @param enabled
	 * @param accountNonExpired
	 * @param accountNonLocked
	 * @param credentialsNonExpired
	 * @param authorities
	 */
	public GenericUserDetails(String principal,
							  String credential,
							  boolean enabled,
							  boolean accountNonExpired,
							  boolean accountNonLocked,
							  boolean credentialsNonExpired,
							  Collection<? extends GrantedAuthority> authorities) {
		
		this.principal = principal;
		this.credential = credential;
		this.enabled = enabled;
		this.accountNonExpired = accountNonExpired;
		this.accountNonLocked = accountNonLocked;
		this.credentialsNonExpired = credentialsNonExpired;
		this.grantedAuthorities = AuthorityAssistor.sortAuthorities(authorities);
	}

	public static UserDetails getUserDetailsWithAuthorities(GenericUserDetails userDetails,
			Collection<? extends GrantedAuthority> authorities) {
		userDetails.setGrantedAuthorities(authorities);
		return userDetails;
	}
	
	/**
	 * Spring Security의 principal에 부여된 권한 목록을 반환 하도록 한다.
	 * UserDetails interface의 구현 메소드이다.
	 * 
	 * @return list of granted authorities
	 */
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Collections.unmodifiableSet(this.grantedAuthorities);
	}
	
	/**
	 * Spring Security 는 username 속성을 principal 로 사용한다. 따라서  UserDetails 를 확장하여 사용하는 경우 
	 * principal 로 사용할 속성을 username 으로 사용해야 한다.
	 * UserDetails 인터페이스 구현 method 이다.
	 * 
	 * @return user identity (principal)
	 */
	@Override
	public String getUsername() {
		return principal;
	}
	
	/**
	 * 인증 Credential (사용자 비민번호)을 반환한다.
	 * UserDetails 인터페이스의 구현 method 이다.
	 * 
	 * @return password;
	 */
	@Override
	public String getPassword() {
		return credential;
	}
	
	/**
	 * UserDetails 인터페이스 구현 method 이다.
	 * 
	 * @return enabled;
	 */
	@Override
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * 계정 기한 만료 여부를 반환한다.
	 * UserDetails 인터페이스 구현 method 이다.
	 * 
	 * @return account non expired
	 */
	@Override
	public boolean isAccountNonExpired() {
		return accountNonExpired;
	}

	/**
	 * 계정 잠김 여부를 반환한다.
	 * UserDetails 인터페이스 구현 method 이다.
	 * 
	 * @return account non locked
	 */
	@Override
	public boolean isAccountNonLocked() {
		return accountNonLocked;
	}

	/**
	 * Credential 만료 여부를 반환한다.
	 * UserDetails 인터페이스 구현 method 이다.
	 * 
	 * @return credentials non expired;
	 */
	@Override
	public boolean isCredentialsNonExpired() {
		return credentialsNonExpired;
	}

	/**
	 * Principal (User id) 를 주입한다. 
	 * @param principal the principal to set
	 */
	protected void setPrincipal(String principal) {
		this.principal = principal;
	}

	/**
	 * Credential (password) 를 주입한다.
	 * @param credential the credential to set
	 */
	protected void setCredential(String credential) {
		this.credential = credential;
	}

	/**
	 * @param enabled the enabled to set
	 */
	protected void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	/**
	 * @param grantedAuthorities the grantedAuthorities to set
	 */
	protected void setGrantedAuthorities(Collection<? extends GrantedAuthority> grantedAuthorities) {
		this.grantedAuthorities = AuthorityAssistor.sortAuthorities(grantedAuthorities);
	}

	/**
	 * @param accountNonExpired the accountNonExpired to set
	 */
	protected void setAccountNonExpired(boolean accountNonExpired) {
		this.accountNonExpired = accountNonExpired;
	}

	/**
	 * @param accountNonLocked the accountNonLocked to set
	 */
	protected void setAccountNonLocked(boolean accountNonLocked) {
		this.accountNonLocked = accountNonLocked;
	}

	/**
	 * @param credentialsNonExpired the credentialsNonExpired to set
	 */
	protected void setCredentialsNonExpired(boolean credentialsNonExpired) {
		this.credentialsNonExpired = credentialsNonExpired;
	}
}
