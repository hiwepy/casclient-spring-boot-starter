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

@ConfigurationProperties(CasIgnoreProperties.PREFIX)
@Getter
@Setter
@ToString
public class CasIgnoreProperties {

	public static final String PREFIX = "cas.ignore";
	 
	/** Whether role checking should ignore case. Defaults to false. */
	private boolean ignoreCase = false;
	/** Defines the url pattern to ignore, when intercepting authentication requests. */
	private String ignorePattern;
	/** Defines the type of the pattern specified. Defaults to REGEX. Other types are CONTAINS, EXACT. */
	private String ignoreUrlPatternType = "REGEX";

}
