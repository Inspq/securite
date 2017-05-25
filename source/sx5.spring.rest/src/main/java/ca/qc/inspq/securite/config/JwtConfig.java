package ca.qc.inspq.securite.config;

import java.net.URL;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.UrlJwkProvider;

@Configuration
public class JwtConfig {
    @Value("${security.jwt.jwks_uri}")
    private String keyStoreUri;

	@Bean
	public JwkProvider urlJwkProvider() throws Exception {
	      return new UrlJwkProvider(new URL(keyStoreUri));
	}
}