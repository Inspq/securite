package ca.qc.inspq.securite.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SigningKeyResolverAdapter;

import java.security.Key;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.auth0.jwk.JwkException;
import com.auth0.jwk.JwkProvider;

@Component
public class JwtUtil {
    @Value("${security.jwt.signingKeyId}")
    private String signingKeyId;

    @Autowired
    private JwkProvider jwkProvider;

    /**
     * Tries to parse specified String as a JWT token. If successful, returns User object with username, id and role prefilled (extracted from token).
     * If unsuccessful (token is invalid or not containing all required user properties), simply returns null.
     * 
     * @param token the JWT token to parse
     * @return the JwtUserDetails object extracted from specified token or null if a token is invalid.
     */
    public JwtUserDetails parseToken(String token) {
    	if (StringUtils.isEmpty(token)) {
            throw new AccessDeniedException("Aucun jeton d'identification n'a été fourni");
    	}

    	try {
            Claims body = Jwts.parser()
            		.setSigningKeyResolver(new SigningKeyResolverAdapter() {
			                @Override
			                public Key resolveSigningKey(JwsHeader header, Claims claims) {
			                    try {
									return getSigningKey(header.getKeyId());
								} catch (JwkException e) {
									e.printStackTrace();
									throw new AccessDeniedException("La clé d'encryption fournie dans le jeton d'identification est inconnue");
								}
			                }})
                    .parseClaimsJws(token)
                    .getBody();

            return new JwtUserDetails(body);
        } catch (ExpiredJwtException e) {
            throw new AccessDeniedException("Le jeton d'authentification fourni est expiré.");
        } catch (JwtException | ClassCastException e) {
        	e.printStackTrace();
            throw new AccessDeniedException("Le jeton d'authentification fourni n'est pas valide.");
        }
    }

    /**
     * Generates a JWT token containing username as subject, and userId and role as additional claims. These properties are taken from the specified
     * User object. Tokens validity is infinite.
     * 
     * @param userDetails the user for which the token will be generated
     * @return the JWT token
     * @throws JwkException 
     */
    public String generateToken(JwtUserDetails userDetails) throws JwkException {
        Claims claims = Jwts.claims().setSubject(userDetails.getUsername());
        claims.put("userId", userDetails.getUserId() + "");

        final List<String> authorities = userDetails.getAuthorities()
        			.stream()
        			.map(grantedAuthority -> grantedAuthority.getAuthority())
        			.collect(Collectors.toList());
        if (authorities.size() > 0) {
        	claims.put("role", String.join(",", authorities.toArray(new String[(authorities.size())])));
        }

        final Key signingKey = getSigningKey(signingKeyId);
        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.forName(signingKey.getAlgorithm()), signingKey)
                .compact();
    }

    private Key getSigningKey(String keyId) throws JwkException {
		return jwkProvider.get(keyId).getPublicKey();
    }
}
