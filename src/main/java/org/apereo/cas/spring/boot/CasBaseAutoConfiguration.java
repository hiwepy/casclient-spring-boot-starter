package org.apereo.cas.spring.boot;

import org.jasig.cas.client.authentication.AuthenticationFilter;
import org.jasig.cas.client.authentication.AuthenticationRedirectStrategy;
import org.jasig.cas.client.authentication.DefaultAuthenticationRedirectStrategy;
import org.jasig.cas.client.authentication.DefaultGatewayResolverImpl;
import org.jasig.cas.client.authentication.GatewayResolver;
import org.jasig.cas.client.authentication.UrlPatternMatcherStrategy;
import org.jasig.cas.client.proxy.ProxyGrantingTicketStorage;
import org.jasig.cas.client.proxy.ProxyGrantingTicketStorageImpl;
import org.jasig.cas.client.session.HashMapBackedSessionMappingStorage;
import org.jasig.cas.client.session.SessionMappingStorage;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(AuthenticationFilter.class)
@ConditionalOnProperty(prefix = CasProperties.PREFIX, value = "enabled", havingValue = "true")
@EnableConfigurationProperties({ CasProperties.class })
public class CasBaseAutoConfiguration {
	 	
	@Bean
	@ConditionalOnMissingBean
	public ProxyGrantingTicketStorage proxyGrantingTicketStorage(CasProperties casProperties) {
		return new ProxyGrantingTicketStorageImpl(casProperties.getTimeout());
	}
 
	@Bean
	@ConditionalOnMissingBean
	public GatewayResolver gatewayStorage() {
		GatewayResolver gatewayStorage = new DefaultGatewayResolverImpl();
		return gatewayStorage;
	}

	@Bean
	@ConditionalOnMissingBean
	public AuthenticationRedirectStrategy authenticationRedirectStrategy() {
		AuthenticationRedirectStrategy authenticationRedirectStrategy = new DefaultAuthenticationRedirectStrategy();
		return authenticationRedirectStrategy;
	}
	
	@Bean
	@ConditionalOnMissingBean
	public UrlPatternMatcherStrategy ignoreUrlPatternMatcherStrategy() {
		UrlPatternMatcherStrategy ignoreUrlPatternMatcherStrategy = new AntUrlPatternMatcherStrategy();
		return ignoreUrlPatternMatcherStrategy;
	}
	
	@Bean
	@ConditionalOnMissingBean
	public SessionMappingStorage sessionMappingStorage() {
		return new HashMapBackedSessionMappingStorage();
	}
}
