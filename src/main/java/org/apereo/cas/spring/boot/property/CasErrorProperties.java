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

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;


/**
 * Configuration properties for the CAS {@code ErrorRedirectFilter}, bound to the
 * {@code cas.error} prefix.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@ConfigurationProperties(CasErrorProperties.PREFIX)
public class CasErrorProperties {

	public static final String PREFIX = "cas.error";

	/** 
	 * Whether Enable ErrorRedirectFilter. 
	 */
	private boolean enabled = false;
	/** 
	 * The Url Patterns of ErrorRedirectFilter. 
	 */
	private String[] pathPatterns = new String[] { "/*" };
	/** 
	 * Default url to redirect to, in case no erorr matches are found.
	  */
	private String errorRedirectUrl; 
	/** 
	 * The Url to redirect to, find the path by Fully qualified exception name , i.e. java.lang.Exception .
	  */
	private Map<String /* Class Name */, String /* Redirect Page Path */> errorRedirectMappings = new LinkedHashMap<String, String>();


	public boolean isEnabled() { return enabled; }
	public void setEnabled(boolean enabled) { this.enabled = enabled; }
	public String[] getPathPatterns() { return pathPatterns; }
	public void setPathPatterns(String[] pathPatterns) { this.pathPatterns = pathPatterns; }
	public String getErrorRedirectUrl() { return errorRedirectUrl; }
	public void setErrorRedirectUrl(String errorRedirectUrl) { this.errorRedirectUrl = errorRedirectUrl; }
	public Map<String /* Class Name */, String /* Redirect Page Path */> getErrorRedirectMappings() { return errorRedirectMappings; }
	public void setErrorRedirectMappings(Map<String /* Class Name */, String /* Redirect Page Path */> errorRedirectMappings) { this.errorRedirectMappings = errorRedirectMappings; }
}
