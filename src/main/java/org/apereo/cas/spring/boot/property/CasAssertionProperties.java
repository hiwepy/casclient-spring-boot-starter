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
 * Configuration properties for the CAS {@code AssertionThreadLocalFilter}, bound to the
 * {@code cas.assertion} prefix.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@ConfigurationProperties(CasAssertionProperties.PREFIX)
public class CasAssertionProperties {

	public static final String PREFIX = "cas.assertion";

	/** 
	 * Whether Enable AuthenticationFilter. 
	 */
	private boolean enabled = true;
	/** 
	 * The Url Patterns of AuthenticationFilter. 
	 */
	private String[] pathPatterns = new String[] { "/*" };
	

	public boolean isEnabled() { return enabled; }
	public void setEnabled(boolean enabled) { this.enabled = enabled; }
	public String[] getPathPatterns() { return pathPatterns; }
	public void setPathPatterns(String[] pathPatterns) { this.pathPatterns = pathPatterns; }
}
