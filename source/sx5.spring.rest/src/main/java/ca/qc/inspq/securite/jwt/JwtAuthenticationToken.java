package ca.qc.inspq.securite.jwt;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class JwtAuthenticationToken extends UsernamePasswordAuthenticationToken {
	private static final long serialVersionUID = -4942746242847182790L;

	public JwtAuthenticationToken(JwtUserDetails jwtUserDetails) {
		super(jwtUserDetails, null, jwtUserDetails.getAuthorities());
	}
}
