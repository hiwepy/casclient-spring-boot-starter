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
import org.apache.rocketmq.common.MixAll;

public class ProducerConfig {

	/** 客户端回调线程池容量大小，默认 JVM可用的CPU个数 **/
	protected int clientCallbackExecutorThreads = Runtime.getRuntime().availableProcessors();

	/** 当前客户端IP地址 **/
	protected String clientIP;

	/**
	 * Compress message body threshold, namely, message body larger than 4k will
	 * be compressed on default.
	 */
	protected int compressMsgBodyOverHowmuch = 1024 * 4;

	/**
	 * Just for testing or demo program
	 */
	protected String createTopicKey = MixAll.DEFAULT_TOPIC;

	/**
	 * Number of queues to create per default topic.
	 */
	protected int defaultTopicQueueNums = 4;

	/**
	 * Heartbeat interval in microseconds with message broker
	 */
	protected int heartbeatBrokerInterval = 1000 * 30;

	/**
	 * 当前客户端实例名称，默认 ：DEFAULT
	 */
	protected String instanceName = System.getProperty("rocketmq.client.name", "DEFAULT");

	protected long[] latencyMax;

	/**
	 * Maximum allowed message size in bytes.
	 */
	protected int maxMessageSize = 1024 * 1024 * 4; // 4M

	protected String namesrvAddr;

	protected long[] notAvailableDuration;

	/**
	 * Offset persistent interval for consumer
	 */
	protected int persistConsumerOffsetInterval = 1000 * 5;

	/**
	 * Pulling topic information interval from the named server
	 */
	protected int pollNameServerInteval = 1000 * 30;

	/**
	 * Producer group conceptually aggregates all producer instances of exactly
	 * same role, which is particularly important when transactional messages
	 * are involved.
	 * </p>
	 *
	 * For non-transactional messages, it does not matter as long as it's unique
	 * per process.
	 * </p>
	 *
	 * See {@linktourl http://rocketmq.incubator.apache.org/docs/core-concept/}
	 * for more discussion.
	 */
	protected String producerGroup;

	/**
	 * Indicate whether to retry another broker on sending failure internally.
	 */
	protected boolean retryAnotherBrokerWhenNotStoreOK = false;

	/**
	 * Maximum number of retry to perform internally before claiming sending
	 * failure in asynchronous mode.
	 * </p>
	 *
	 * This may potentially cause message duplication which is up to application
	 * developers to resolve.
	 */
	protected int retryTimesWhenSendAsyncFailed = 2;

	/**
	 * Maximum number of retry to perform internally before claiming sending
	 * failure in synchronous mode.
	 * </p>
	 *
	 * This may potentially cause message duplication which is up to application
	 * developers to resolve.
	 */
	protected int retryTimesWhenSendFailed = 2;

	protected boolean sendLatencyFaultEnable = true;

	protected boolean sendMessageWithVIPChannel = true;

	/**
	 * Timeout for sending messages.
	 */
	private int sendMsgTimeout = 3000;

	protected boolean unitMode = false;

	protected String unitName;

	protected boolean vipChannelEnabled = false;

	public String getProducerGroup() {
		return StringUtils.isEmpty(producerGroup) ? "ProducerGroup" : producerGroup;
	}

	public void setProducerGroup(String producerGroup) {
		this.producerGroup = producerGroup;
	}

	public String getInstanceName() {
		return instanceName;
	}

	public void setInstanceName(String instanceName) {
		this.instanceName = instanceName;
	}

	public int getClientCallbackExecutorThreads() {
		return clientCallbackExecutorThreads;
	}

	public void setClientCallbackExecutorThreads(int clientCallbackExecutorThreads) {
		this.clientCallbackExecutorThreads = clientCallbackExecutorThreads;
	}

	public String getClientIP() {
		return clientIP;
	}

	public void setClientIP(String clientIP) {
		this.clientIP = clientIP;
	}

	public int getCompressMsgBodyOverHowmuch() {
		return compressMsgBodyOverHowmuch;
	}

	public void setCompressMsgBodyOverHowmuch(int compressMsgBodyOverHowmuch) {
		this.compressMsgBodyOverHowmuch = compressMsgBodyOverHowmuch;
	}

	public String getCreateTopicKey() {
		return createTopicKey;
	}

	public void setCreateTopicKey(String createTopicKey) {
		this.createTopicKey = createTopicKey;
	}

	public int getDefaultTopicQueueNums() {
		return defaultTopicQueueNums;
	}

	public void setDefaultTopicQueueNums(int defaultTopicQueueNums) {
		this.defaultTopicQueueNums = defaultTopicQueueNums;
	}

	public int getHeartbeatBrokerInterval() {
		return heartbeatBrokerInterval;
	}

	public void setHeartbeatBrokerInterval(int heartbeatBrokerInterval) {
		this.heartbeatBrokerInterval = heartbeatBrokerInterval;
	}

	public long[] getLatencyMax() {
		return latencyMax;
	}

	public void setLatencyMax(long[] latencyMax) {
		this.latencyMax = latencyMax;
	}

	public int getMaxMessageSize() {
		return maxMessageSize;
	}

	public void setMaxMessageSize(int maxMessageSize) {
		this.maxMessageSize = maxMessageSize;
	}

	public String getNamesrvAddr() {
		return namesrvAddr;
	}

	public void setNamesrvAddr(String namesrvAddr) {
		this.namesrvAddr = namesrvAddr;
	}

	public long[] getNotAvailableDuration() {
		return notAvailableDuration;
	}

	public void setNotAvailableDuration(long[] notAvailableDuration) {
		this.notAvailableDuration = notAvailableDuration;
	}

	public int getPersistConsumerOffsetInterval() {
		return persistConsumerOffsetInterval;
	}

	public void setPersistConsumerOffsetInterval(int persistConsumerOffsetInterval) {
		this.persistConsumerOffsetInterval = persistConsumerOffsetInterval;
	}

	public int getPollNameServerInteval() {
		return pollNameServerInteval;
	}

	public void setPollNameServerInteval(int pollNameServerInteval) {
		this.pollNameServerInteval = pollNameServerInteval;
	}

	public boolean isRetryAnotherBrokerWhenNotStoreOK() {
		return retryAnotherBrokerWhenNotStoreOK;
	}

	public void setRetryAnotherBrokerWhenNotStoreOK(boolean retryAnotherBrokerWhenNotStoreOK) {
		this.retryAnotherBrokerWhenNotStoreOK = retryAnotherBrokerWhenNotStoreOK;
	}

	public int getRetryTimesWhenSendAsyncFailed() {
		return retryTimesWhenSendAsyncFailed;
	}

	public void setRetryTimesWhenSendAsyncFailed(int retryTimesWhenSendAsyncFailed) {
		this.retryTimesWhenSendAsyncFailed = retryTimesWhenSendAsyncFailed;
	}

	public int getRetryTimesWhenSendFailed() {
		return retryTimesWhenSendFailed;
	}

	public void setRetryTimesWhenSendFailed(int retryTimesWhenSendFailed) {
		this.retryTimesWhenSendFailed = retryTimesWhenSendFailed;
	}

	public boolean isSendLatencyFaultEnable() {
		return sendLatencyFaultEnable;
	}

	public void setSendLatencyFaultEnable(boolean sendLatencyFaultEnable) {
		this.sendLatencyFaultEnable = sendLatencyFaultEnable;
	}

	public boolean isSendMessageWithVIPChannel() {
		return sendMessageWithVIPChannel;
	}

	public void setSendMessageWithVIPChannel(boolean sendMessageWithVIPChannel) {
		this.sendMessageWithVIPChannel = sendMessageWithVIPChannel;
	}

	public int getSendMsgTimeout() {
		return sendMsgTimeout;
	}

	public void setSendMsgTimeout(int sendMsgTimeout) {
		this.sendMsgTimeout = sendMsgTimeout;
	}

	public boolean isUnitMode() {
		return unitMode;
	}

	public void setUnitMode(boolean unitMode) {
		this.unitMode = unitMode;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public boolean isVipChannelEnabled() {
		return vipChannelEnabled;
	}

	public void setVipChannelEnabled(boolean vipChannelEnabled) {
		this.vipChannelEnabled = vipChannelEnabled;
	}
	
}
