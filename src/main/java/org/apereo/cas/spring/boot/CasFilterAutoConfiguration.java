package org.apereo.cas.spring.boot;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apereo.cas.spring.boot.CasProperties.CasProtocol;
import org.apereo.cas.spring.boot.property.CasAssertionProperties;
import org.apereo.cas.spring.boot.property.CasAuthcProperties;
import org.apereo.cas.spring.boot.property.CasErrorProperties;
import org.apereo.cas.spring.boot.property.CasSsoProperties;
import org.apereo.cas.spring.boot.property.CasTicketProperties;
import org.apereo.cas.spring.boot.property.CasWrapperProperties;
import org.jasig.cas.client.authentication.AuthenticationFilter;
import org.jasig.cas.client.authentication.GatewayResolver;
import org.jasig.cas.client.authentication.Saml11AuthenticationFilter;
import org.jasig.cas.client.authentication.UrlPatternMatcherStrategy;
import org.jasig.cas.client.configuration.ConfigurationKeys;
import org.jasig.cas.client.session.SingleSignOutFilter;
import org.jasig.cas.client.session.SingleSignOutHttpSessionListener;
import org.jasig.cas.client.util.AssertionThreadLocalFilter;
import org.jasig.cas.client.util.ErrorRedirectFilter;
import org.jasig.cas.client.util.HttpServletRequestWrapperFilter;
import org.jasig.cas.client.validation.AbstractTicketValidationFilter;
import org.jasig.cas.client.validation.Cas10TicketValidationFilter;
import org.jasig.cas.client.validation.Cas20ProxyReceivingTicketValidationFilter;
import org.jasig.cas.client.validation.Cas30ProxyReceivingTicketValidationFilter;
import org.jasig.cas.client.validation.Saml11TicketValidationFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.properties.PropertyMapper;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

/**
 * Spring Boot auto-configuration that registers the Apereo CAS client servlet filters
 * (single sign-out, error redirect, ticket validation, authentication, request wrapper
 * and assertion thread-local) and the single sign-out HTTP session listener.
 * <p>
 * Each filter is registered as a {@link FilterRegistrationBean} with an explicit order so
 * the CAS filter chain executes in the correct sequence. The configuration is activated
 * only when the CAS {@code AuthenticationFilter} is on the classpath and CAS integration
 * is explicitly enabled.</p>
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 * @since 1.0.0
 */
@Configuration
@ConditionalOnClass(AuthenticationFilter.class)
@ConditionalOnProperty(prefix = CasProperties.PREFIX, value = "enabled", havingValue = "true")
@EnableConfigurationProperties({ CasProperties.class })
public class CasFilterAutoConfiguration {

	/**
	 * Registers the single sign-out HTTP session listener.
	 * @return a {@link ServletListenerRegistrationBean} wrapping a
	 *         {@link SingleSignOutHttpSessionListener}, ordered first
	 */
	@Bean
	public ServletListenerRegistrationBean<SingleSignOutHttpSessionListener> singleSignOutHttpSessionListener() {
		ServletListenerRegistrationBean<SingleSignOutHttpSessionListener> registration = new ServletListenerRegistrationBean<SingleSignOutHttpSessionListener>(
				new SingleSignOutHttpSessionListener());
		registration.setOrder(1);
		return registration;
	}

	/**
	 * Registers the CAS {@link ErrorRedirectFilter} that redirects exceptions thrown
	 * during request processing to configured error pages.
	 * @param casErrorProperties the CAS error-handling configuration properties
	 * @return a {@link FilterRegistrationBean} for the {@link ErrorRedirectFilter}
	 */
	@Bean
	public FilterRegistrationBean<ErrorRedirectFilter> errorRedirectFilter(CasErrorProperties casErrorProperties) {
		
		FilterRegistrationBean<ErrorRedirectFilter> filterRegistration = new FilterRegistrationBean<ErrorRedirectFilter>();
		filterRegistration.setFilter(new ErrorRedirectFilter());
		
		filterRegistration.addInitParameter("defaultErrorRedirectPage", casErrorProperties.getErrorRedirectUrl());
		Map<String /* Class Name */, String /* Redirect Page Path */> errorRedirectMappings = casErrorProperties.getErrorRedirectMappings();
		if(errorRedirectMappings != null) {
			Iterator<Entry<String, String>> ite = errorRedirectMappings.entrySet().iterator();
			while (ite.hasNext()) {
				Entry<String, String> entry = ite.next();
				filterRegistration.addInitParameter(entry.getKey(), entry.getValue());
			}
		}
		
		filterRegistration.setEnabled(casErrorProperties.isEnabled());
		filterRegistration.addUrlPatterns(casErrorProperties.getPathPatterns());
		filterRegistration.setOrder(1);
		return filterRegistration;
	}
	
	/**
	 * Registers the CAS {@link SingleSignOutFilter} that implements single sign-out.
	 * <p>
	 * This filter must run before the other CAS filters so that logout requests from the
	 * CAS server invalidate the local session.</p>
	 * @param casProperties the CAS configuration properties
	 * @param casSsoProperties the CAS single sign-out configuration properties
	 * @return a {@link FilterRegistrationBean} for the {@link SingleSignOutFilter}
	 */
	@Bean
	public FilterRegistrationBean<SingleSignOutFilter> singleSignOutFilter(CasProperties casProperties,
			CasSsoProperties casSsoProperties) {

		FilterRegistrationBean<SingleSignOutFilter> filterRegistration = new FilterRegistrationBean<SingleSignOutFilter>();
		filterRegistration.setFilter(new SingleSignOutFilter());
		filterRegistration.setEnabled(casProperties.isEnabled());
		
		if(StringUtils.hasText(casProperties.getArtifactParameterName())) {	
			filterRegistration.addInitParameter(ConfigurationKeys.ARTIFACT_PARAMETER_NAME.getName(), casProperties.getArtifactParameterName());
		}
		if(StringUtils.hasText(casProperties.getLogoutParameterName())) {	
			filterRegistration.addInitParameter(ConfigurationKeys.LOGOUT_PARAMETER_NAME.getName(), casProperties.getLogoutParameterName());
		}
		if(StringUtils.hasText(casProperties.getRelayStateParameterName())) {	
			filterRegistration.addInitParameter(ConfigurationKeys.RELAY_STATE_PARAMETER_NAME.getName(), casProperties.getRelayStateParameterName());
		}
		filterRegistration.addInitParameter(ConfigurationKeys.CAS_SERVER_URL_PREFIX.getName(), casProperties.getPrefixUrl());
		filterRegistration.addInitParameter(ConfigurationKeys.ARTIFACT_PARAMETER_OVER_POST.getName(), String.valueOf(casProperties.isArtifactParameterOverPost()));
		filterRegistration.addInitParameter(ConfigurationKeys.EAGERLY_CREATE_SESSIONS.getName(), String.valueOf(casProperties.isEagerlyCreateSessions()));
		
		filterRegistration.setEnabled(casProperties.isEnabled());
		filterRegistration.addUrlPatterns(casSsoProperties.getPathPatterns());
		filterRegistration.setOrder(2);
		return filterRegistration;
	}

	/**
	 * Registers the CAS ticket validation filter responsible for validating the service
	 * ticket returned by the CAS server.
	 * <p>
	 * The concrete filter implementation (CAS 1.0, CAS 2.0 proxy, CAS 3.0 proxy or SAML
	 * 1.1) is selected based on the configured {@link CasProperties.CasProtocol}.</p>
	 * @param casProperties the CAS configuration properties
	 * @param casTicketProperties the CAS ticket-validation configuration properties
	 * @return a {@link FilterRegistrationBean} for the {@link AbstractTicketValidationFilter}
	 */
	@Bean
	public FilterRegistrationBean<AbstractTicketValidationFilter> ticketValidationFilter(CasProperties casProperties,
			CasTicketProperties casTicketProperties) {
		FilterRegistrationBean<AbstractTicketValidationFilter> filterRegistration = new FilterRegistrationBean<AbstractTicketValidationFilter>();
		filterRegistration.setEnabled(casProperties.isEnabled()); 
		if(CasProtocol.CAS10.equals(casProperties.getProtocol())) {
			filterRegistration.setFilter(new Cas10TicketValidationFilter());
		}
		else if(CasProtocol.CAS20_PROXY.equals(casProperties.getProtocol())) {
			
			filterRegistration.setFilter(new Cas20ProxyReceivingTicketValidationFilter());

			// CAS 2.0 proxy-receiving ticket validation parameters
			filterRegistration.addInitParameter(ConfigurationKeys.ACCEPT_ANY_PROXY.getName(), Boolean.toString(casProperties.isAcceptAnyProxy()));
			if(StringUtils.hasText(casProperties.getAllowedProxyChains())) {	
				filterRegistration.addInitParameter(ConfigurationKeys.ALLOWED_PROXY_CHAINS.getName(), casProperties.getAllowedProxyChains());
			}
			filterRegistration.addInitParameter(ConfigurationKeys.ARTIFACT_PARAMETER_OVER_POST.getName(), Boolean.toString(casProperties.isArtifactParameterOverPost()));
			if(StringUtils.hasText(casProperties.getArtifactParameterName())) {	
				filterRegistration.addInitParameter(ConfigurationKeys.ARTIFACT_PARAMETER_NAME.getName(), casProperties.getArtifactParameterName());
			}
			if(StringUtils.hasText(casTicketProperties.getAuthenticationRedirectStrategyClass())) {
				filterRegistration.addInitParameter(ConfigurationKeys.AUTHENTICATION_REDIRECT_STRATEGY_CLASS.getName(), casTicketProperties.getAuthenticationRedirectStrategyClass());
			}
			if(StringUtils.hasText(casTicketProperties.getCipherAlgorithm())) {
				filterRegistration.addInitParameter(ConfigurationKeys.CIPHER_ALGORITHM.getName(), casTicketProperties.getCipherAlgorithm());
			}
			filterRegistration.addInitParameter(ConfigurationKeys.EAGERLY_CREATE_SESSIONS.getName(), Boolean.toString(casProperties.isEagerlyCreateSessions()));
			filterRegistration.addInitParameter(ConfigurationKeys.GATEWAY.getName(), Boolean.toString(casProperties.isGateway()));
			if(StringUtils.hasText(casTicketProperties.getGatewayStorageClass())) {
				filterRegistration.addInitParameter(ConfigurationKeys.GATEWAY_STORAGE_CLASS.getName(), casTicketProperties.getGatewayStorageClass());
			}
			filterRegistration.addInitParameter(ConfigurationKeys.IGNORE_CASE.getName(), Boolean.toString(casTicketProperties.isIgnoreCase()));
			if(StringUtils.hasText(casTicketProperties.getIgnorePattern())) {
				filterRegistration.addInitParameter(ConfigurationKeys.IGNORE_PATTERN.getName(), casTicketProperties.getIgnorePattern());
			}
			filterRegistration.addInitParameter(ConfigurationKeys.IGNORE_URL_PATTERN_TYPE.getName(), casTicketProperties.getIgnoreUrlPatternType().toString());
			if(StringUtils.hasText(casProperties.getLogoutParameterName())) {
				filterRegistration.addInitParameter(ConfigurationKeys.LOGOUT_PARAMETER_NAME.getName(), casProperties.getLogoutParameterName());
			}
			filterRegistration.addInitParameter(ConfigurationKeys.MILLIS_BETWEEN_CLEAN_UPS.getName(), Long.toString(casTicketProperties.getMillisBetweenCleanUps()));
			if(StringUtils.hasText(casTicketProperties.getProxyReceptorUrl())) {
				filterRegistration.addInitParameter(ConfigurationKeys.PROXY_RECEPTOR_URL.getName(), casTicketProperties.getProxyReceptorUrl());
			}
			if(StringUtils.hasText(casTicketProperties.getProxyCallbackUrl())) {
				filterRegistration.addInitParameter(ConfigurationKeys.PROXY_CALLBACK_URL.getName(), casTicketProperties.getProxyCallbackUrl());
			}
			if(StringUtils.hasText(casTicketProperties.getProxyGrantingTicketStorageClass())) {
				filterRegistration.addInitParameter(ConfigurationKeys.PROXY_GRANTING_TICKET_STORAGE_CLASS.getName(), casTicketProperties.getProxyGrantingTicketStorageClass());
			}
			if(StringUtils.hasText(casProperties.getRelayStateParameterName())) {
				filterRegistration.addInitParameter(ConfigurationKeys.RELAY_STATE_PARAMETER_NAME.getName(), casProperties.getRelayStateParameterName());
			}
			if(StringUtils.hasText(casTicketProperties.getRoleAttribute())) {
				filterRegistration.addInitParameter(ConfigurationKeys.ROLE_ATTRIBUTE.getName(), casTicketProperties.getRoleAttribute());
			}
			if(StringUtils.hasText(casTicketProperties.getSecretKey())) {
				filterRegistration.addInitParameter(ConfigurationKeys.SECRET_KEY.getName(), casTicketProperties.getSecretKey());
			}
			if(StringUtils.hasText(casTicketProperties.getTicketValidatorClass())) {
				filterRegistration.addInitParameter(ConfigurationKeys.TICKET_VALIDATOR_CLASS.getName(), casTicketProperties.getTicketValidatorClass());
			}
			filterRegistration.addInitParameter(ConfigurationKeys.TOLERANCE.getName(), Long.toString(casTicketProperties.getTolerance()));
			
		}
		else if(CasProtocol.CAS30_PROXY.equals(casProperties.getProtocol())) {
			filterRegistration.setFilter(new Cas30ProxyReceivingTicketValidationFilter());
		}
		else if(CasProtocol.SAML.equals(casProperties.getProtocol())) {
			filterRegistration.setFilter(new Saml11TicketValidationFilter());
			// SAML 1.1 ticket validation parameters
			filterRegistration.addInitParameter(ConfigurationKeys.TOLERANCE.getName(), Long.toString(casTicketProperties.getTolerance()));
		}

		// Common parameters applied to all ticket validation filters
		filterRegistration.addInitParameter(ConfigurationKeys.ENCODE_SERVICE_URL.getName(), Boolean.toString(casProperties.isEncodeServiceUrl()));
		if(StringUtils.hasText(casProperties.getEncoding())) {	
			filterRegistration.addInitParameter(ConfigurationKeys.ENCODING.getName(), casProperties.getEncoding());
		}
		filterRegistration.addInitParameter(ConfigurationKeys.EXCEPTION_ON_VALIDATION_FAILURE.getName(), Boolean.toString(casTicketProperties.isExceptionOnValidationFailure()));
		filterRegistration.addInitParameter(ConfigurationKeys.CAS_SERVER_LOGIN_URL.getName(), casProperties.getLoginUrl());
		filterRegistration.addInitParameter(ConfigurationKeys.CAS_SERVER_URL_PREFIX.getName(), casProperties.getPrefixUrl());
		if(StringUtils.hasText(casTicketProperties.getHostnameVerifier())) {	
			filterRegistration.addInitParameter(ConfigurationKeys.HOSTNAME_VERIFIER.getName(), casTicketProperties.getHostnameVerifier());
		}
		if(StringUtils.hasText(casTicketProperties.getHostnameVerifierConfig())) {	
			filterRegistration.addInitParameter(ConfigurationKeys.HOSTNAME_VERIFIER_CONFIG.getName(), casTicketProperties.getHostnameVerifierConfig());
		}
		filterRegistration.addInitParameter(ConfigurationKeys.REDIRECT_AFTER_VALIDATION.getName(), Boolean.toString(casTicketProperties.isRedirectAfterValidation()));
		//filterRegistration.addInitParameter(ConfigurationKeys.RENEW.getName(), Boolean.toString(properties.isRenew()));
		if(StringUtils.hasText(casProperties.getServerName())) {	
			filterRegistration.addInitParameter(ConfigurationKeys.SERVER_NAME.getName(), casProperties.getServerName());
		} else if(StringUtils.hasText(casProperties.getService())) {	
			filterRegistration.addInitParameter(ConfigurationKeys.SERVICE.getName(), casProperties.getService());
		}
		if(StringUtils.hasText(casTicketProperties.getSslConfigFile())) {
			filterRegistration.addInitParameter(ConfigurationKeys.SSL_CONFIG_FILE.getName(), casTicketProperties.getSslConfigFile());
		}
		filterRegistration.addInitParameter(ConfigurationKeys.USE_SESSION.getName(), Boolean.toString(casTicketProperties.isUseSession()));
		
		filterRegistration.addUrlPatterns(casTicketProperties.getPathPatterns());
		filterRegistration.setOrder(3);
	    return filterRegistration;
	}
	
	/**
	 * Registers the CAS authentication filter responsible for ensuring the user is
	 * authenticated, redirecting unauthenticated requests to the CAS login page.
	 * @param casProperties the CAS configuration properties
	 * @param casAuthcProperties the CAS authentication configuration properties
	 * @param gatewayStorage the gateway resolver used to track gateway requests
	 * @param ignoreUrlPatternMatcherStrategy the strategy used to skip URLs from
	 *        authentication
	 * @return a {@link FilterRegistrationBean} for the {@link AuthenticationFilter}
	 */
	@Bean
	public FilterRegistrationBean<AuthenticationFilter> authenticationFilter(CasProperties casProperties,
			CasAuthcProperties casAuthcProperties,
			GatewayResolver gatewayStorage, UrlPatternMatcherStrategy ignoreUrlPatternMatcherStrategy) {
		FilterRegistrationBean<AuthenticationFilter> filterRegistration = new FilterRegistrationBean<AuthenticationFilter>();
		
		AuthenticationFilter authenticationFilter = null;
		if (CasProtocol.SAML.equals(casProperties.getProtocol())) {
			authenticationFilter = new Saml11AuthenticationFilter();
		} else {
			authenticationFilter = new AuthenticationFilter();
		}

		// Map CAS properties onto the authentication filter in a single pass.
		PropertyMapper map = PropertyMapper.get().alwaysApplyingWhenNonNull();
		
		map.from(casProperties.getLoginUrl()).to(authenticationFilter::setCasServerLoginUrl);
		map.from(casProperties.getPrefixUrl()).to(authenticationFilter::setCasServerUrlPrefix);
		map.from(casProperties.isEncodeServiceUrl()).to(authenticationFilter::setEncodeServiceUrl);
		map.from(casProperties.isGateway()).to(authenticationFilter::setGateway);
		map.from(gatewayStorage).to(authenticationFilter::setGatewayStorage);
		map.from(true).to(authenticationFilter::setIgnoreInitConfiguration);
		map.from(ignoreUrlPatternMatcherStrategy).to(authenticationFilter::setIgnoreUrlPatternMatcherStrategyClass);
		map.from(casProperties.isRenew()).to(authenticationFilter::setRenew);
		map.from(casProperties.getServerName()).to(authenticationFilter::setServerName);
		map.from(casProperties.getService()).to(authenticationFilter::setService);
		
		filterRegistration.setFilter(authenticationFilter);
		filterRegistration.addUrlPatterns(casAuthcProperties.getPathPatterns());
		filterRegistration.setOrder(4);
		return filterRegistration;
	}

	/**
	 * Registers the CAS {@link HttpServletRequestWrapperFilter} that wraps the request so
	 * the authenticated user can be retrieved via
	 * {@code HttpServletRequest#getRemoteUser()}.
	 * @param casWrapperProperties the CAS request-wrapper configuration properties
	 * @return a {@link FilterRegistrationBean} for the {@link HttpServletRequestWrapperFilter}
	 */
	@Bean
	public FilterRegistrationBean<HttpServletRequestWrapperFilter> requestWrapperFilter(CasWrapperProperties casWrapperProperties) {
		FilterRegistrationBean<HttpServletRequestWrapperFilter> filterRegistration = new FilterRegistrationBean<HttpServletRequestWrapperFilter>();
		
		filterRegistration.setFilter(new HttpServletRequestWrapperFilter());
		filterRegistration.setEnabled(casWrapperProperties.isEnabled()); 
		filterRegistration.addInitParameter(ConfigurationKeys.IGNORE_CASE.getName(), String.valueOf(casWrapperProperties.isIgnoreCase()));
		if(StringUtils.hasText(casWrapperProperties.getRoleAttribute())) {	
			filterRegistration.addInitParameter(ConfigurationKeys.ROLE_ATTRIBUTE.getName(), casWrapperProperties.getRoleAttribute());
		}
		filterRegistration.addUrlPatterns(casWrapperProperties.getPathPatterns());
		filterRegistration.setOrder(5);
	    return filterRegistration;
	}

	/**
	 * Registers the CAS {@link AssertionThreadLocalFilter} that exposes the current
	 * authentication {@code Assertion} through {@code AssertionHolder}, so non-web layers
	 * can access the logged-in principal (for example via
	 * {@code AssertionHolder.getAssertion().getPrincipal().getName()}).
	 * @param casAssertionProperties the CAS assertion configuration properties
	 * @return a {@link FilterRegistrationBean} for the {@link AssertionThreadLocalFilter}
	 */
	@Bean
	public FilterRegistrationBean<AssertionThreadLocalFilter> assertionThreadLocalFilter(CasAssertionProperties casAssertionProperties) {
		FilterRegistrationBean<AssertionThreadLocalFilter> filterRegistration = new FilterRegistrationBean<AssertionThreadLocalFilter>();
		filterRegistration.setFilter(new AssertionThreadLocalFilter());
		filterRegistration.setEnabled(casAssertionProperties.isEnabled());
		filterRegistration.addUrlPatterns(casAssertionProperties.getPathPatterns());
		filterRegistration.setOrder(6);
		return filterRegistration;
	}
 
}
