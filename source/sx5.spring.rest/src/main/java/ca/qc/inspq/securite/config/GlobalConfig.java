package ca.qc.inspq.securite.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextListener;

import ca.qc.inspq.securite.session.Session;

@Configuration
public class GlobalConfig {
	@Bean
    @ConditionalOnMissingBean(RequestContextListener.class)
    public RequestContextListener requestContextListener() {
        return new RequestContextListener();
    }

	@Bean
	@Scope(value = WebApplicationContext.SCOPE_SESSION,
			proxyMode = ScopedProxyMode.TARGET_CLASS)
	public Session session() {
		return new Session();
	}
}
