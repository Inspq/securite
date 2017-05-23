package ca.qc.inspq.securite.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

import ca.qc.inspq.securite.filter.OpenIdConnectFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
    private OAuth2RestTemplate restTemplate;

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(HttpMethod.GET, "/static/**");
    }

    @Bean
    protected OpenIdConnectFilter oidcFilter() {
        final OpenIdConnectFilter filter = new OpenIdConnectFilter("/home");
        filter.setRestTemplate(restTemplate);
        return filter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.addFilterAfter(new OAuth2ClientContextFilter(), AbstractPreAuthenticatedProcessingFilter.class)
        	.addFilterAfter(oidcFilter(), OAuth2ClientContextFilter.class)
        	.httpBasic()
        	.realmName("/")
        	.authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/home"))
        	.and()
        		.authorizeRequests()
        		.antMatchers("/index").permitAll()
        		.anyRequest().authenticated()
        	.and()
        		.logout()
        		.deleteCookies("JSESSIONID")
        		.invalidateHttpSession(false)
 				.logoutSuccessUrl("/index?logout");
    }
}