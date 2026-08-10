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
 * Configuration properties for the CAS single sign-out filter, bound to the
 * {@code cas.sso} prefix.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@ConfigurationProperties(CasSsoProperties.PREFIX)
public class CasSsoProperties {

	public static final String PREFIX = "cas.sso";

	/** 
	 * The Url Patterns of SingleSignOutFilter. 
	 */
	private String[] pathPatterns = new String[] { "/*" };


	public String[] getPathPatterns() { return pathPatterns; }
	public void setPathPatterns(String[] pathPatterns) { this.pathPatterns = pathPatterns; }
}
