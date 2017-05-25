package ca.qc.inspq.securite.jwt;

import io.jsonwebtoken.Claims;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;

public class JwtUserDetails implements UserDetails {
	private static final long serialVersionUID = -5992860806427644607L;

	private final Claims claims;

    public JwtUserDetails(Claims claims) {
        this.claims = claims;
    }

    public String getUserId() {
        return claims.get("userId", String.class);
    }

    public String getUsername() {
        return claims.getSubject();
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
    	final String roles = claims.get("role", String.class);
    	if (!StringUtils.isEmpty(roles)) {
            return AuthorityUtils.commaSeparatedStringToAuthorityList(roles);
    	}
    	return Collections.emptyList();
    }

    public String getPassword() {
        return null;
    }

    public boolean isAccountNonExpired() {
        return true;
    }

    public boolean isAccountNonLocked() {
        return true;
    }

    public boolean isCredentialsNonExpired() {
        return true;
    }

    public boolean isEnabled() {
        return true;
    }
}