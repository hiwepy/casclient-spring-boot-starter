/*
 * Copyright (c) 2017, hiwepy (https://github.com/hiwepy).
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.apereo.cas.spring.boot;

import org.jasig.cas.client.proxy.ProxyGrantingTicketStorageImpl;
import org.springframework.boot.context.properties.ConfigurationProperties;


/**
 * Configuration properties for the Apereo CAS client integration, bound to the
 * {@code cas} prefix.
 * <p>
 * These properties drive the core CAS client behavior shared across the authentication,
 * ticket-validation and single sign-out filters.</p>
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@ConfigurationProperties(CasProperties.PREFIX)
public class CasProperties {

	/**
	 * Default name of the CAS attribute for remember me authentication (CAS 3.4.10+)
	 */
	public static final String DEFAULT_REMEMBER_ME_ATTRIBUTE_NAME = "longTermAuthenticationRequestTokenUsed";
	/**
	 * Default timeout in milliseconds.
	 */
	public static final long DEFAULT_TIMEOUT = 60000;
	
	public static final String PREFIX = "cas";

	/**
	 * Supported CAS protocols, used to select the appropriate ticket validation and
	 * authentication filters.
	 */
	public static enum CasProtocol {
		CAS10, CAS20, CAS20_PROXY, CAS30, CAS30_PROXY, SAML
	}

	/** 
	 * Whether Enable Cas. 
	 */
	private boolean enabled = false;
	/**
	 * Defines the location of the CAS server login URL, i.e. https://localhost:8443/cas/login
	 */
	private String loginUrl;
	/**
	 * Defines the location of the CAS server logout URL, i.e. https://localhost:8443/cas/logout
	 */
	private String logoutUrl;
	/**
	 * Defines the location of the CAS server rest URL, i.e. https://localhost:8443/cas/v1/tickets
	 */
	private String restUrl;
	/** 
	 * The prefix url of the CAS server. i.e.https://localhost:8443/cas 
	 */
	private String prefixUrl;
	/** 
	 * The url where the application is redirected if the CAS service ticket validation failed (example : /mycontextpatch/cas_error.jsp) 
	 */
	private String failureUrl;
	/**
	 * Specifies the name of the request parameter on where to find the artifact (i.e. ticket).
	 */
	private String artifactParameterName = "ticket";
	/**
	 * If true, then any non-null artifact (ticket) should be authenticated.
	 * Additionally, the service will be determined dynamically in order to ensure
	 * the service matches the expected value for this artifact.
	 */
	private boolean authenticateAllArtifacts = false;

	private boolean artifactParameterOverPost = false;
	/** 
     * Specifies whether any proxy is OK. Defaults to false. 
     */
	private boolean acceptAnyProxy = false;
	/**
	 * Specifies the proxy chain. 
	 * Each acceptable proxy chain should include a space-separated list of URLs (for exact match) or regular expressions of URLs (starting by the ^ character). 
	 * Each acceptable proxy chain should appear on its own line.
	 */
	private String allowedProxyChains;
	/** 
	 * Specifies the encoding charset the client should use 
	 */
	private String encoding = "UTF-8";
	/** 
	 * Whether the client should auto encode the service url. Defaults to true 
	 */
	private boolean encodeServiceUrl = true;
    /** 
     * Defaults to true 
     */
	private boolean eagerlyCreateSessions = true;
	/** 
	 * The protocol of the CAS Client. 
	 */
	private CasProtocol protocol = CasProtocol.CAS20;
	/** 
	 * Specifies whether gateway=true should be sent to the CAS server. Valid values are either true/false (or no value at all) 
	 */
	private boolean gateway = false;
	/** 
	 * Defaults to logoutRequest 
	 */
	private String logoutParameterName;
	
	/**
	 * Specifies whether renew=true should be sent to the CAS server. 
	 * Valid values are either true/false (or no value at all). 
	 * Note that renew cannot be specified as local init-param setting..
	 */
	private boolean renew = false;
	/** 
	 * Name of parameter containing the state of the CAS server webflow. 
	 */
	private String relayStateParameterName;
	/** 
	 * Defines the location of the application cas callback URL, i.e. /callback 
	 */
	private String serverCallbackUrl;
	/**
	 * The name of the server this application is hosted on. 
	 * Service URL will be dynamically constructed using this, 
	 * i.e. https://localhost:8443 (you must include the protocol, but port is optional if it's a standard port).
	 */
	private String serverName;
	/** 
	 * The service URL to send to the CAS server, i.e. https://localhost:8443/yourwebapp/index.html 
	 */
	private String service;
	/** 
	 * Specifies the name of the request parameter on where to find the service (i.e. service). 
	 */
	private String serviceParameterName = "service";
	
	/**
	 * time, in milliseconds, before a {@link ProxyGrantingTicketHolder} is
	 * considered expired and ready for removal.
	 * 
	 * @see ProxyGrantingTicketStorageImpl#DEFAULT_TIMEOUT
	 */
	private long timeout = DEFAULT_TIMEOUT;
	


	public boolean isEnabled() { return enabled; }
	public void setEnabled(boolean enabled) { this.enabled = enabled; }
	public String getLoginUrl() { return loginUrl; }
	public void setLoginUrl(String loginUrl) { this.loginUrl = loginUrl; }
	public String getLogoutUrl() { return logoutUrl; }
	public void setLogoutUrl(String logoutUrl) { this.logoutUrl = logoutUrl; }
	public String getRestUrl() { return restUrl; }
	public void setRestUrl(String restUrl) { this.restUrl = restUrl; }
	public String getPrefixUrl() { return prefixUrl; }
	public void setPrefixUrl(String prefixUrl) { this.prefixUrl = prefixUrl; }
	public String getFailureUrl() { return failureUrl; }
	public void setFailureUrl(String failureUrl) { this.failureUrl = failureUrl; }
	public String getArtifactParameterName() { return artifactParameterName; }
	public void setArtifactParameterName(String artifactParameterName) { this.artifactParameterName = artifactParameterName; }
	public boolean isAuthenticateAllArtifacts() { return authenticateAllArtifacts; }
	public void setAuthenticateAllArtifacts(boolean authenticateAllArtifacts) { this.authenticateAllArtifacts = authenticateAllArtifacts; }
	public boolean isArtifactParameterOverPost() { return artifactParameterOverPost; }
	public void setArtifactParameterOverPost(boolean artifactParameterOverPost) { this.artifactParameterOverPost = artifactParameterOverPost; }
	public boolean isAcceptAnyProxy() { return acceptAnyProxy; }
	public void setAcceptAnyProxy(boolean acceptAnyProxy) { this.acceptAnyProxy = acceptAnyProxy; }
	public String getAllowedProxyChains() { return allowedProxyChains; }
	public void setAllowedProxyChains(String allowedProxyChains) { this.allowedProxyChains = allowedProxyChains; }
	public String getEncoding() { return encoding; }
	public void setEncoding(String encoding) { this.encoding = encoding; }
	public boolean isEncodeServiceUrl() { return encodeServiceUrl; }
	public void setEncodeServiceUrl(boolean encodeServiceUrl) { this.encodeServiceUrl = encodeServiceUrl; }
	public boolean isEagerlyCreateSessions() { return eagerlyCreateSessions; }
	public void setEagerlyCreateSessions(boolean eagerlyCreateSessions) { this.eagerlyCreateSessions = eagerlyCreateSessions; }
	public CasProtocol getProtocol() { return protocol; }
	public void setProtocol(CasProtocol protocol) { this.protocol = protocol; }
	public boolean isGateway() { return gateway; }
	public void setGateway(boolean gateway) { this.gateway = gateway; }
	public String getLogoutParameterName() { return logoutParameterName; }
	public void setLogoutParameterName(String logoutParameterName) { this.logoutParameterName = logoutParameterName; }
	public boolean isRenew() { return renew; }
	public void setRenew(boolean renew) { this.renew = renew; }
	public String getRelayStateParameterName() { return relayStateParameterName; }
	public void setRelayStateParameterName(String relayStateParameterName) { this.relayStateParameterName = relayStateParameterName; }
	public String getServerCallbackUrl() { return serverCallbackUrl; }
	public void setServerCallbackUrl(String serverCallbackUrl) { this.serverCallbackUrl = serverCallbackUrl; }
	public String getServerName() { return serverName; }
	public void setServerName(String serverName) { this.serverName = serverName; }
	public String getService() { return service; }
	public void setService(String service) { this.service = service; }
	public String getServiceParameterName() { return serviceParameterName; }
	public void setServiceParameterName(String serviceParameterName) { this.serviceParameterName = serviceParameterName; }
	public long getTimeout() { return timeout; }
	public void setTimeout(long timeout) { this.timeout = timeout; }
}
