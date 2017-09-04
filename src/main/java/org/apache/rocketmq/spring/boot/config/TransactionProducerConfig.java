/*
 * Copyright (c) 2010-2020, vindell (https://github.com/vindell).
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
package org.apache.rocketmq.spring.boot.config;

import org.apache.commons.lang3.StringUtils;

public class TransactionProducerConfig extends ProducerConfig {

	/** 事务回查最小并发数 **/
	protected int checkThreadPoolMinSize = 1;
	/** 事务回查最大并发数 **/
	protected int checkThreadPoolMaxSize = 1;
	/** 队列数 **/
	protected int checkRequestHoldMax = 2000;

	public String getProducerGroup() {
		return StringUtils.isEmpty(producerGroup) ? "TransactionProducerGroup" : producerGroup;
	}

	public int getCheckThreadPoolMinSize() {
		return checkThreadPoolMinSize;
	}

	public void setCheckThreadPoolMinSize(int checkThreadPoolMinSize) {
		this.checkThreadPoolMinSize = checkThreadPoolMinSize;
	}

	public int getCheckThreadPoolMaxSize() {
		return checkThreadPoolMaxSize;
	}

	public void setCheckThreadPoolMaxSize(int checkThreadPoolMaxSize) {
		this.checkThreadPoolMaxSize = checkThreadPoolMaxSize;
	}

	public int getCheckRequestHoldMax() {
		return checkRequestHoldMax;
	}

	public void setCheckRequestHoldMax(int checkRequestHoldMax) {
		this.checkRequestHoldMax = checkRequestHoldMax;
	}

}
