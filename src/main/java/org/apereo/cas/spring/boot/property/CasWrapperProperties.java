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


/**
 * Configuration properties for the CAS {@code HttpServletRequestWrapperFilter}, bound to
 * the {@code cas.wrapper} prefix.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@ConfigurationProperties(CasWrapperProperties.PREFIX)
public class CasWrapperProperties {

	public static final String PREFIX = "cas.wrapper";

	/** 
	 * Whether Enable HttpServletRequestWrapperFilter. 
	 */
	private boolean enabled = true;
	/** 
	 * The Url Patterns of ErrorRedirectFilter. 
	 */
	private String[] pathPatterns = new String[] { "/*" };
	/** 
	 * Used to determine the principal role. 
	 */
	private String roleAttribute;
	/** 
	 * Whether role checking should ignore case. Defaults to false. 
	 */
	private boolean ignoreCase = false;


	public boolean isEnabled() { return enabled; }
	public void setEnabled(boolean enabled) { this.enabled = enabled; }
	public String[] getPathPatterns() { return pathPatterns; }
	public void setPathPatterns(String[] pathPatterns) { this.pathPatterns = pathPatterns; }
	public String getRoleAttribute() { return roleAttribute; }
	public void setRoleAttribute(String roleAttribute) { this.roleAttribute = roleAttribute; }
	public boolean isIgnoreCase() { return ignoreCase; }
	public void setIgnoreCase(boolean ignoreCase) { this.ignoreCase = ignoreCase; }
}
