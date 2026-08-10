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

/**
 * Base Spring Boot auto-configuration for the Apereo CAS client, registering the core
 * infrastructural beans (proxy granting ticket storage, gateway resolver, authentication
 * redirect strategy, URL pattern matcher and session mapping storage) used by the CAS
 * filters.
 * <p>
 * The configuration is activated only when the CAS {@code AuthenticationFilter} is on the
 * classpath and CAS integration is explicitly enabled.</p>
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@Configuration
@ConditionalOnClass(AuthenticationFilter.class)
@ConditionalOnProperty(prefix = CasProperties.PREFIX, value = "enabled", havingValue = "true")
@EnableConfigurationProperties({ CasProperties.class })
public class CasBaseAutoConfiguration {

	/**
	 * Creates the {@link ProxyGrantingTicketStorage} bean used to hold proxy granting
	 * tickets, honoring the configured timeout.
	 * @param casProperties the CAS configuration properties
	 * @return a default {@link ProxyGrantingTicketStorageImpl} instance
	 */
	@Bean
	@ConditionalOnMissingBean
	public ProxyGrantingTicketStorage proxyGrantingTicketStorage(CasProperties casProperties) {
		return new ProxyGrantingTicketStorageImpl(casProperties.getTimeout());
	}

	/**
	 * Creates the {@link GatewayResolver} bean responsible for tracking gateway
	 * authentication requests.
	 * @return a default {@link DefaultGatewayResolverImpl} instance
	 */
	@Bean
	@ConditionalOnMissingBean
	public GatewayResolver gatewayStorage() {
		GatewayResolver gatewayStorage = new DefaultGatewayResolverImpl();
		return gatewayStorage;
	}

	/**
	 * Creates the {@link AuthenticationRedirectStrategy} bean that decides how the
	 * client redirects to the CAS server for authentication.
	 * @return a default {@link DefaultAuthenticationRedirectStrategy} instance
	 */
	@Bean
	@ConditionalOnMissingBean
	public AuthenticationRedirectStrategy authenticationRedirectStrategy() {
		AuthenticationRedirectStrategy authenticationRedirectStrategy = new DefaultAuthenticationRedirectStrategy();
		return authenticationRedirectStrategy;
	}

	/**
	 * Creates the {@link UrlPatternMatcherStrategy} bean used to determine which URLs the
	 * CAS filters should ignore.
	 * @return an {@link AntUrlPatternMatcherStrategy} instance
	 */
	@Bean
	@ConditionalOnMissingBean
	public UrlPatternMatcherStrategy ignoreUrlPatternMatcherStrategy() {
		UrlPatternMatcherStrategy ignoreUrlPatternMatcherStrategy = new AntUrlPatternMatcherStrategy();
		return ignoreUrlPatternMatcherStrategy;
	}

	/**
	 * Creates the {@link SessionMappingStorage} bean used by the single sign-out filter
	 * to map HTTP sessions to CAS tickets.
	 * @return a {@link HashMapBackedSessionMappingStorage} instance
	 */
	@Bean
	@ConditionalOnMissingBean
	public SessionMappingStorage sessionMappingStorage() {
		return new HashMapBackedSessionMappingStorage();
	}
}
