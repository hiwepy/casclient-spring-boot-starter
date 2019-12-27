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
package org.apereo.cas.spring.boot.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ConfigurationProperties(CasAuthcProperties.PREFIX)
@Getter
@Setter
@ToString
public class CasAuthcProperties {

	public static final String PREFIX = "cas.authc";

	/** 
	 * The Url Patterns of AuthenticationFilter. 
	 */
	private String[] pathPatterns = new String[] { "/*" };

	/** The class name of the component to decide how to handle authn redirects to CAS */
	private String authenticationRedirectStrategyClass;

	/** 
	 * Whether to throw an exception or not on ticket validation failure. Defaults to true. 
	 */
	private boolean exceptionOnValidationFailure = true;
	/** Specifies whether gateway=true should be sent to the CAS server. Valid values are either true/false (or no value at all) */
	private boolean gateway = false;
	/** The storage class used to record gateway requests */
	private String gatewayStorageClass;
	/** Hostname verifier class name, used when making back-channel calls */
	private String hostnameVerifier;
	private String hostnameVerifierConfig;
	/** Whether role checking should ignore case. Defaults to false. */
	private boolean ignoreCase = false;
	/** Defines the url pattern to ignore, when intercepting authentication requests. */
	private String ignorePattern;
	/** Defines the type of the pattern specified. Defaults to REGEX. Other types are CONTAINS, EXACT. */
	private String ignoreUrlPatternType = "REGEX";
	/** 
	 * Startup delay for the cleanup task to remove expired tickets from the storage. Defaults to 60000 msec 
	 */
	private long millisBetweenCleanUps = 60000L;
	/** The callback URL to provide the CAS server to accept Proxy Granting Tickets. */
	private String proxyCallbackUrl;
	/**
	 * The URL to watch for PGTIOU/PGT responses from the CAS server. 
	 * Should be defined from the root of the context. 
	 * For example, if your application is deployed in /cas-client-app and 
	 * you want the proxy receptor URL to be /cas-client-app/my/receptor 
	 * you need to configure proxyReceptorUrl to be /my/receptor.
	 */
	private String proxyReceptorUrl;
	/** Specify an implementation of the ProxyGrantingTicketStorage class that has a no-arg constructor. */
	private String proxyGrantingTicketStorageClass;
	/** The Url Patterns of HttpServletRequestWrapperFilter. */
	private String[] requestWrapperFilterUrlPatterns = new String[] { "/*" };
	/** Whether to redirect to the same URL after ticket validation, but without the ticket in the parameter. Defaults to true. */
	private boolean redirectAfterValidation = true;
	/**
	 * Specifies whether renew=true should be sent to the CAS server. 
	 * Valid values are either true/false (or no value at all). 
	 * Note that renew cannot be specified as local init-param setting..
	 */
	private boolean renew = false;
	/** Name of parameter containing the state of the CAS server webflow. */
	private String relayStateParameterName;
	/** Used to determine the principal role. */
	private String roleAttribute;
	/** The secret key used by the proxyGrantingTicketStorageClass if it supports encryption. */
	private String secretKey;
	/** Defines the location of the application cas callback URL, i.e. /callback */
	private String serverCallbackUrl;
	/**
	 * The name of the server this application is hosted on. 
	 * Service URL will be dynamically constructed using this, 
	 * i.e. https://localhost:8443 (you must include the protocol, but port is optional if it's a standard port).
	 */
	private String serverName;
	/** The service URL to send to the CAS server, i.e. https://localhost:8443/yourwebapp/index.html */
	private String service;
	/** Specifies the name of the request parameter on where to find the service (i.e. service). */
	private String serviceParameterName = "service";
	/**
	 * A reference to a properties file that includes SSL settings for client-side
	 * SSL config, used during back-channel calls. The configuration includes keys
	 * for protocol which defaults to SSL,keyStoreType, keyStorePath,
	 * keyStorePass,keyManagerType which defaults to SunX509 andcertificatePassword.
	 */
	private String sslConfigFile;
	/** The Url Patterns of TicketValidationFilter. */
	private String[] ticketValidationFilterUrlPatterns = new String[] { "/*" };
	/** Ticket validator class to use/create */
	private String ticketValidatorClass;
	/**
	 * The tolerance for drifting clocks when validating SAML tickets. 
	 * Note that 10 seconds should be more than enough for most environments that have NTP time synchronization. 
	 * Defaults to 1000 msec
	 */
	private long tolerance = 5000L;
	/**
	 * Whether to store the Assertion in session or not. If sessions are not used,
	 * tickets will be required for each request. Defaults to true.
	 */
	private boolean useSession = true;
	
	/** The algorithm used by the proxyGrantingTicketStorageClass if it supports encryption. Defaults to DESede */
	private String cipherAlgorithm;
}
