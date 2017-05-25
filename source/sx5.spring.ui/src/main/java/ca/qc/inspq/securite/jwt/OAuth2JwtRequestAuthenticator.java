package ca.qc.inspq.securite.jwt;

import org.springframework.http.client.ClientHttpRequest;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RequestAuthenticator;
import org.springframework.security.oauth2.client.http.AccessTokenRequiredException;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

public class OAuth2JwtRequestAuthenticator implements OAuth2RequestAuthenticator {
	@Override
	public void authenticate(OAuth2ProtectedResourceDetails resource, OAuth2ClientContext clientContext,
			ClientHttpRequest request) {
		final OAuth2AccessToken accessToken = clientContext.getAccessToken();
		if (accessToken == null) {
			throw new AccessTokenRequiredException(resource);
		}
        final String idToken = accessToken.getAdditionalInformation().get("id_token").toString();

		request.getHeaders().set("Authorization", 
				String.format("%s %s", OAuth2AccessToken.BEARER_TYPE, idToken));
	}
}
