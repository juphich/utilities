package net.jupic.spring.security.domain;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.Assert;

/**
 * @author chang jung pil
 *
 */
public class GenericGrantedAuthority implements GrantedAuthority {


	private static final long serialVersionUID = 3075839036825197374L;
	
	private final String role;
	private final String roleName;

	private String rolePrefix = "";
	
	/**
	 * @param description
	 * @param role
	 * @param roleName
	 */
	public GenericGrantedAuthority(String role, String roleName) {
		Assert.hasText(role, "A granted authority textual representation is required");
		this.role = role;
		this.roleName = roleName;
	}

	public String getAuthority() {
		return rolePrefix + role;
	}
	
	public String getRoleName() {
		return roleName;
	}
	
	@Override
	public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj instanceof GenericGrantedAuthority) {
            return role.equals(((GenericGrantedAuthority) obj).role);
        }

        return false;
    }

	@Override
    public int hashCode() {
        return this.role.hashCode();
    }

	@Override
    public String toString() {
        return this.role;
    }

	/**
	 * @param rolePrefix
	 */
	public void addRolePrefix(String rolePrefix) {
		this.rolePrefix = rolePrefix;
	}
}
