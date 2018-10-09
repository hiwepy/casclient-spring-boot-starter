package org.apereo.cas.spring.boot;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.jasig.cas.client.Protocol;
import org.jasig.cas.client.authentication.AuthenticationFilter;
import org.jasig.cas.client.authentication.Saml11AuthenticationFilter;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

@Configuration
@ConditionalOnProperty(prefix = CasclientProperties.PREFIX, value = "enabled", havingValue = "true")
@EnableConfigurationProperties({ CasclientProperties.class })
public class CasclientAutoConfiguration {
	
	@Autowired
	private CasclientProperties casProperties;

	/*
	1、首先定义CasConfiguration(loginUrl,prefixUrl),loginUrl为完整的cas登录地址,比如client项目的https://passport.sqzryang.com/login?service=https://client.sqzryang.com,prefixUrl则为cas路径前缀,根据cas的版本号拼接请求地址,用于验证sts是否正确并且返回登录成功后的信息。
	2、定义CasClient,property configuration(CasConfiguration) and callbackUrl(String),在pac4j中,每一个client相当于一种认证协议,比如我们需要weibo登录则应该配置一个WeiboClient,具体回掉的时候应该采用哪个client进行验证授权则需要下面配置的Clients
	3、定义Clients,在这里面可以定义你所有的Client以及默认的client还有关于如何区分回掉的哪个client应该取某个参数的配置,具体详细看源码
	4、定义Config,在config里面还有关于权限方面的配置以及session存储的一些配置,由于这部分我交给shiro去管理,所以只传入了clients即可
	5、以上四个都是pac4j的配置,接下来配置一个由buji-pac4j提供用于和shiro结合的filter:CallbackFilter,直接传入config即可
	6、定义好CallbackFilter以后,在ShiroFilterFactoryBean中注册好filters,并且配置好filterChainDefinitions
    */
	/*
	 * 1、首先定义CasConfiguration(loginUrl,prefixUrl),loginUrl为完整的cas登录地址,
	 * 比如client项目的https://passport.sqzryang.com/login?service=https://client.sqzryang.com,
	 * prefixUrl则为cas路径前缀,根据cas的版本号拼接请求地址,用于验证sts是否正确并且返回登录成功后的信息。
	 
	CasConfiguration casConfiguration(String loginUrl,String prefixUrl) {
		return casConfiguration(loginUrl, prefixUrl);
	}*/
	
	/*
	 * 2、定义CasClient,property configuration(CasConfiguration) and callbackUrl(String),
	 * 在pac4j中,每一个client相当于一种认证协议,比如我们需要weibo登录则应该配置一个WeiboClient,
	 * 具体回掉的时候应该采用哪个client进行验证授权则需要下面配置的Clients
	 */
	
	
	/*
	 * 单点登录Session监听器
	 */
	@Bean
	public ServletListenerRegistrationBean<SingleSignOutHttpSessionListener> singleSignOutHttpSessionListener() {
		ServletListenerRegistrationBean<SingleSignOutHttpSessionListener> registration = new ServletListenerRegistrationBean<SingleSignOutHttpSessionListener>(
				new SingleSignOutHttpSessionListener());
		registration.setOrder(1);
		return registration;
	}

	/*
	 * CAS Error Redirect Filter </br>
	 */
	@Bean
	public FilterRegistrationBean<ErrorRedirectFilter> errorRedirectFilter() {
		
		FilterRegistrationBean<ErrorRedirectFilter> filterRegistration = new FilterRegistrationBean<ErrorRedirectFilter>();
		filterRegistration.setFilter(new ErrorRedirectFilter());
		filterRegistration.setEnabled(casProperties.isErrorRedirect());
		
		filterRegistration.addInitParameter("defaultErrorRedirectPage", casProperties.getDefaultErrorRedirectPage());
		Map<String /* Class Name */, String /* Redirect Page Path */> errorRedirectMappings = casProperties.getErrorRedirectMappings();
		if(errorRedirectMappings != null) {
			Iterator<Entry<String, String>> ite = errorRedirectMappings.entrySet().iterator();
			while (ite.hasNext()) {
				Entry<String, String> entry = ite.next();
				filterRegistration.addInitParameter(entry.getKey(), entry.getValue());
			}
		}
		
		filterRegistration.addUrlPatterns(casProperties.getErrorRedirectFilterUrlPatterns());
		filterRegistration.setOrder(1);
		return filterRegistration;
	}
	
	/*
	 * CAS SignOut Filter </br>
	 * 该过滤器用于实现单点登出功能，单点退出配置，一定要放在其他filter之前
	 */
	@Bean
	public FilterRegistrationBean<SingleSignOutFilter> singleSignOutFilter() {
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
		filterRegistration.addInitParameter(ConfigurationKeys.CAS_SERVER_URL_PREFIX.getName(), casProperties.getCasServerUrlPrefix());
		filterRegistration.addInitParameter(ConfigurationKeys.ARTIFACT_PARAMETER_OVER_POST.getName(), String.valueOf(casProperties.isArtifactParameterOverPost()));
		filterRegistration.addInitParameter(ConfigurationKeys.EAGERLY_CREATE_SESSIONS.getName(), String.valueOf(casProperties.isEagerlyCreateSessions()));
		
		filterRegistration.addUrlPatterns(casProperties.getSignOutFilterUrlPatterns());
		filterRegistration.setOrder(2);
		return filterRegistration;
	}

	/*
	 * CAS Ticket Validation Filter </br>
	 * 该过滤器负责对Ticket的校验工作
	 */
	@Bean
	public FilterRegistrationBean<AbstractTicketValidationFilter> ticketValidationFilter() {
		FilterRegistrationBean<AbstractTicketValidationFilter> filterRegistration = new FilterRegistrationBean<AbstractTicketValidationFilter>();
		filterRegistration.setEnabled(casProperties.isEnabled()); 
		if(Protocol.CAS1.equals(casProperties.getProtocol())) {
			filterRegistration.setFilter(new Cas10TicketValidationFilter());
		}
		else if(Protocol.CAS2.equals(casProperties.getProtocol())) {
			
			filterRegistration.setFilter(new Cas20ProxyReceivingTicketValidationFilter());
			
			// Cas20ProxyReceivingTicketValidationFilter
			filterRegistration.addInitParameter(ConfigurationKeys.ACCEPT_ANY_PROXY.getName(), Boolean.toString(casProperties.isAcceptAnyProxy()));
			if(StringUtils.hasText(casProperties.getAllowedProxyChains())) {	
				filterRegistration.addInitParameter(ConfigurationKeys.ALLOWED_PROXY_CHAINS.getName(), casProperties.getAllowedProxyChains());
			}
			filterRegistration.addInitParameter(ConfigurationKeys.ARTIFACT_PARAMETER_OVER_POST.getName(), Boolean.toString(casProperties.isArtifactParameterOverPost()));
			if(StringUtils.hasText(casProperties.getArtifactParameterName())) {	
				filterRegistration.addInitParameter(ConfigurationKeys.ARTIFACT_PARAMETER_NAME.getName(), casProperties.getArtifactParameterName());
			}
			if(StringUtils.hasText(casProperties.getAuthenticationRedirectStrategyClass())) {
				filterRegistration.addInitParameter(ConfigurationKeys.AUTHENTICATION_REDIRECT_STRATEGY_CLASS.getName(), casProperties.getAuthenticationRedirectStrategyClass());
			}
			if(StringUtils.hasText(casProperties.getCipherAlgorithm())) {
				filterRegistration.addInitParameter(ConfigurationKeys.CIPHER_ALGORITHM.getName(), casProperties.getCipherAlgorithm());
			}
			filterRegistration.addInitParameter(ConfigurationKeys.EAGERLY_CREATE_SESSIONS.getName(), Boolean.toString(casProperties.isEagerlyCreateSessions()));
			filterRegistration.addInitParameter(ConfigurationKeys.GATEWAY.getName(), Boolean.toString(casProperties.isGateway()));
			if(StringUtils.hasText(casProperties.getGatewayStorageClass())) {
				filterRegistration.addInitParameter(ConfigurationKeys.GATEWAY_STORAGE_CLASS.getName(), casProperties.getGatewayStorageClass());
			}
			filterRegistration.addInitParameter(ConfigurationKeys.IGNORE_CASE.getName(), Boolean.toString(casProperties.isIgnoreCase()));
			if(StringUtils.hasText(casProperties.getIgnorePattern())) {
				filterRegistration.addInitParameter(ConfigurationKeys.IGNORE_PATTERN.getName(), casProperties.getIgnorePattern());
			}
			filterRegistration.addInitParameter(ConfigurationKeys.IGNORE_URL_PATTERN_TYPE.getName(), casProperties.getIgnoreUrlPatternType().toString());
			if(StringUtils.hasText(casProperties.getLogoutParameterName())) {
				filterRegistration.addInitParameter(ConfigurationKeys.LOGOUT_PARAMETER_NAME.getName(), casProperties.getLogoutParameterName());
			}
			filterRegistration.addInitParameter(ConfigurationKeys.MILLIS_BETWEEN_CLEAN_UPS.getName(), Long.toString(casProperties.getMillisBetweenCleanUps()));
			if(StringUtils.hasText(casProperties.getProxyReceptorUrl())) {
				filterRegistration.addInitParameter(ConfigurationKeys.PROXY_RECEPTOR_URL.getName(), casProperties.getProxyReceptorUrl());
			}
			if(StringUtils.hasText(casProperties.getProxyCallbackUrl())) {
				filterRegistration.addInitParameter(ConfigurationKeys.PROXY_CALLBACK_URL.getName(), casProperties.getProxyCallbackUrl());
			}
			if(StringUtils.hasText(casProperties.getProxyGrantingTicketStorageClass())) {
				filterRegistration.addInitParameter(ConfigurationKeys.PROXY_GRANTING_TICKET_STORAGE_CLASS.getName(), casProperties.getProxyGrantingTicketStorageClass());
			}
			if(StringUtils.hasText(casProperties.getRelayStateParameterName())) {
				filterRegistration.addInitParameter(ConfigurationKeys.RELAY_STATE_PARAMETER_NAME.getName(), casProperties.getRelayStateParameterName());
			}
			if(StringUtils.hasText(casProperties.getRoleAttribute())) {
				filterRegistration.addInitParameter(ConfigurationKeys.ROLE_ATTRIBUTE.getName(), casProperties.getRoleAttribute());
			}
			if(StringUtils.hasText(casProperties.getSecretKey())) {
				filterRegistration.addInitParameter(ConfigurationKeys.SECRET_KEY.getName(), casProperties.getSecretKey());
			}
			if(StringUtils.hasText(casProperties.getTicketValidatorClass())) {
				filterRegistration.addInitParameter(ConfigurationKeys.TICKET_VALIDATOR_CLASS.getName(), casProperties.getTicketValidatorClass());
			}
			filterRegistration.addInitParameter(ConfigurationKeys.TOLERANCE.getName(), Long.toString(casProperties.getTolerance()));
			
		}
		else if(Protocol.CAS3.equals(casProperties.getProtocol())) {
			filterRegistration.setFilter(new Cas30ProxyReceivingTicketValidationFilter());
		}
		else if(Protocol.SAML11.equals(casProperties.getProtocol())) {
			filterRegistration.setFilter(new Saml11TicketValidationFilter());
			// Saml11TicketValidationFilter
			filterRegistration.addInitParameter(ConfigurationKeys.TOLERANCE.getName(), Long.toString(casProperties.getTolerance()));
		}
		
		// Cas10TicketValidationFilter、Cas20ProxyReceivingTicketValidationFilter、Cas30ProxyReceivingTicketValidationFilter、Saml11TicketValidationFilter
		filterRegistration.addInitParameter(ConfigurationKeys.ENCODE_SERVICE_URL.getName(), Boolean.toString(casProperties.isEncodeServiceUrl()));
		if(StringUtils.hasText(casProperties.getEncoding())) {	
			filterRegistration.addInitParameter(ConfigurationKeys.ENCODING.getName(), casProperties.getEncoding());
		}
		filterRegistration.addInitParameter(ConfigurationKeys.EXCEPTION_ON_VALIDATION_FAILURE.getName(), Boolean.toString(casProperties.isExceptionOnValidationFailure()));
		filterRegistration.addInitParameter(ConfigurationKeys.CAS_SERVER_LOGIN_URL.getName(), casProperties.getCasServerLoginUrl());
		filterRegistration.addInitParameter(ConfigurationKeys.CAS_SERVER_URL_PREFIX.getName(), casProperties.getCasServerUrlPrefix());
		if(StringUtils.hasText(casProperties.getHostnameVerifier())) {	
			filterRegistration.addInitParameter(ConfigurationKeys.HOSTNAME_VERIFIER.getName(), casProperties.getHostnameVerifier());
		}
		if(StringUtils.hasText(casProperties.getHostnameVerifierConfig())) {	
			filterRegistration.addInitParameter(ConfigurationKeys.HOSTNAME_VERIFIER_CONFIG.getName(), casProperties.getHostnameVerifierConfig());
		}
		filterRegistration.addInitParameter(ConfigurationKeys.REDIRECT_AFTER_VALIDATION.getName(), Boolean.toString(casProperties.isRedirectAfterValidation()));
		//filterRegistration.addInitParameter(ConfigurationKeys.RENEW.getName(), Boolean.toString(properties.isRenew()));
		if(StringUtils.hasText(casProperties.getServerName())) {	
			filterRegistration.addInitParameter(ConfigurationKeys.SERVER_NAME.getName(), casProperties.getServerName());
		} else if(StringUtils.hasText(casProperties.getService())) {	
			filterRegistration.addInitParameter(ConfigurationKeys.SERVICE.getName(), casProperties.getService());
		}
		if(StringUtils.hasText(casProperties.getSslConfigFile())) {
			filterRegistration.addInitParameter(ConfigurationKeys.SSL_CONFIG_FILE.getName(), casProperties.getSslConfigFile());
		}
		filterRegistration.addInitParameter(ConfigurationKeys.USE_SESSION.getName(), Boolean.toString(casProperties.isUseSession()));
		
		
		filterRegistration.addUrlPatterns(casProperties.getTicketValidationFilterUrlPatterns());
		filterRegistration.setOrder(3);
	    return filterRegistration;
	}
	
	/*
	 * CAS Authentication Filter </br>
	 * 该过滤器负责用户的认证工作
	 */
	@Bean
	public FilterRegistrationBean<AuthenticationFilter> authenticationFilter() {
		FilterRegistrationBean<AuthenticationFilter> filterRegistration = new FilterRegistrationBean<AuthenticationFilter>();
		if (Protocol.SAML11.equals(casProperties.getProtocol())) {
			filterRegistration.setFilter(new Saml11AuthenticationFilter());
		} else {
			filterRegistration.setFilter(new AuthenticationFilter());
		}
		
		if(StringUtils.hasText(casProperties.getAuthenticationRedirectStrategyClass())) {	
			filterRegistration.addInitParameter(ConfigurationKeys.AUTHENTICATION_REDIRECT_STRATEGY_CLASS.getName(), casProperties.getAuthenticationRedirectStrategyClass());
		}
		filterRegistration.addInitParameter(ConfigurationKeys.CAS_SERVER_LOGIN_URL.getName(), casProperties.getCasServerLoginUrl());
		filterRegistration.addInitParameter(ConfigurationKeys.ENCODE_SERVICE_URL.getName(), Boolean.toString(casProperties.isEncodeServiceUrl()));
		filterRegistration.addInitParameter(ConfigurationKeys.GATEWAY.getName(), Boolean.toString(casProperties.isGateway()));
		if(StringUtils.hasText(casProperties.getGatewayStorageClass())) {	
			filterRegistration.addInitParameter(ConfigurationKeys.GATEWAY_STORAGE_CLASS.getName(), casProperties.getGatewayStorageClass());
		}
		if(StringUtils.hasText(casProperties.getIgnorePattern())) {
			filterRegistration.addInitParameter(ConfigurationKeys.IGNORE_PATTERN.getName(), casProperties.getIgnorePattern());
		}
		filterRegistration.addInitParameter(ConfigurationKeys.IGNORE_URL_PATTERN_TYPE.getName(), casProperties.getIgnoreUrlPatternType().toString());
		//filterRegistration.addInitParameter(ConfigurationKeys.RENEW.getName(), Boolean.toString(properties.isRenew()));
		if(StringUtils.hasText(casProperties.getServerName())) {	
			filterRegistration.addInitParameter(ConfigurationKeys.SERVER_NAME.getName(), casProperties.getServerName());
		} else if(StringUtils.hasText(casProperties.getService())) {	
			filterRegistration.addInitParameter(ConfigurationKeys.SERVICE.getName(), casProperties.getService());
		}
		
		filterRegistration.addUrlPatterns(casProperties.getAuthenticationFilterUrlPatterns());
		filterRegistration.setOrder(4);
		return filterRegistration;
	}

	/*
	 * CAS HttpServletRequest Wrapper Filter </br>
	 * 该过滤器对HttpServletRequest请求包装， 可通过HttpServletRequest的getRemoteUser()方法获得登录用户的登录名
	 */
	@Bean
	public FilterRegistrationBean<HttpServletRequestWrapperFilter> requestWrapperFilter() {
		FilterRegistrationBean<HttpServletRequestWrapperFilter> filterRegistration = new FilterRegistrationBean<HttpServletRequestWrapperFilter>();
		filterRegistration.setFilter(new HttpServletRequestWrapperFilter());
		filterRegistration.setEnabled(casProperties.isEnabled()); 
		filterRegistration.addInitParameter(ConfigurationKeys.IGNORE_CASE.getName(), String.valueOf(casProperties.isIgnoreCase()));
		if(StringUtils.hasText(casProperties.getRoleAttribute())) {	
			filterRegistration.addInitParameter(ConfigurationKeys.ROLE_ATTRIBUTE.getName(), casProperties.getRoleAttribute());
		}
		filterRegistration.addUrlPatterns(casProperties.getRequestWrapperFilterUrlPatterns());
		filterRegistration.setOrder(5);
	    return filterRegistration;
	}

	/*
	 * CAS Assertion Thread Local Filter </br>
	 * 该过滤器使得可以通过org.jasig.cas.client.util.AssertionHolder来获取用户的登录名。
	 * 比如AssertionHolder.getAssertion().getPrincipal().getName()。
	 * 这个类把Assertion信息放在ThreadLocal变量中，这样应用程序不在web层也能够获取到当前登录信息
	 */
	@Bean
	public FilterRegistrationBean<AssertionThreadLocalFilter> assertionThreadLocalFilter() {
		FilterRegistrationBean<AssertionThreadLocalFilter> filterRegistration = new FilterRegistrationBean<AssertionThreadLocalFilter>();
		filterRegistration.setFilter(new AssertionThreadLocalFilter());
		filterRegistration.setEnabled(casProperties.isEnabled());
		filterRegistration.addUrlPatterns(casProperties.getAssertionThreadLocalFilterUrlPatterns());
		filterRegistration.setOrder(6);
		return filterRegistration;
	}
 
}
