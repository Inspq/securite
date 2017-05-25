package ca.qc.inspq.securite.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

import ca.qc.inspq.securite.jwt.JwtAuthenticationFilter;
import ca.qc.inspq.securite.jwt.JwtAuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	public AuthenticationManager authenticationManager;

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/static/**");
    }

    @Bean
    public JwtAuthenticationFilter jwtFilter() {
    	final JwtAuthenticationFilter jwtFilter = new JwtAuthenticationFilter();
    	jwtFilter.setAuthenticationManager(authenticationManager);
    	jwtFilter.setAuthenticationSuccessHandler(new JwtAuthenticationSuccessHandler());
    	return jwtFilter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.addFilterAfter(new OAuth2ClientContextFilter(), AbstractPreAuthenticatedProcessingFilter.class)
        	.addFilterAfter(jwtFilter(), OAuth2ClientContextFilter.class)
        	.csrf().disable()
        	.httpBasic()
        	.realmName("/")
        	.and()
        		.authorizeRequests()
        		.anyRequest().fullyAuthenticated();
    }
}