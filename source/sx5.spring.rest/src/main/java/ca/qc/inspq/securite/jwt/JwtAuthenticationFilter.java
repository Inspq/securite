package ca.qc.inspq.securite.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

public class JwtAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    @Autowired
    private JwtUtil jwtUtil;

    public JwtAuthenticationFilter() {
        super("/**");
    }

    @Override
    protected boolean requiresAuthentication(HttpServletRequest request, HttpServletResponse response) {
        return true;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        final String header = request.getHeader("Authorization");

        if (header == null || !(header.startsWith("Bearer ") || header.startsWith("bearer "))) {
            throw new AccessDeniedException("Aucun jeton d'identification n'a été fourni");
        }

        String authToken = header.substring(7).trim();

        try {
        	JwtAuthenticationToken authRequest = new JwtAuthenticationToken(jwtUtil.parseToken(authToken));

            return getAuthenticationManager().authenticate(authRequest);
        } catch (AccessDeniedException exception) {
        	try {
				response.sendError(HttpStatus.UNAUTHORIZED.value(), exception.getMessage());
			} catch (IOException e) {
				e.printStackTrace();
			}
        } catch (Exception exception) {
        	try {
				response.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.getMessage());
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
		return null;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult)
            throws IOException, ServletException {
        super.successfulAuthentication(request, response, chain, authResult);

        // As this authentication is in HTTP header, after success we need to continue the request normally
        // and return the response as if the resource was not secured at all
        chain.doFilter(request, response);
    }
}
