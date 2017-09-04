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
package org.apache.rocketmq.spring.boot.event;

import java.io.UnsupportedEncodingException;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.context.ApplicationEvent;

public class RocketmqEvent extends ApplicationEvent {  
    private static final long serialVersionUID = -4468405250074063206L;  
      
    private DefaultMQPushConsumer consumer;  
    private MessageExt messageExt;  
    private String topic;  
    private String tag;  
    private byte[] body;  
      
    public RocketmqEvent(MessageExt msg,DefaultMQPushConsumer consumer) throws Exception {  
        super(msg);  
        this.topic = msg.getTopic();  
        this.tag = msg.getTags();  
        this.body = msg.getBody();  
        this.consumer = consumer;  
        this.messageExt = msg;  
    }  
  
    public String getMsg() {  
        try {  
            return new String(this.body,"UTF-8");  
        } catch (UnsupportedEncodingException e) {  
            return null;  
        }  
    }
      
    public String getMsg(String code) {  
        try {  
            return new String(this.body,code);  
        } catch (UnsupportedEncodingException e) {  
            return null;  
        }  
    }

	public DefaultMQPushConsumer getConsumer() {
		return consumer;
	}

	public MessageExt getMessageExt() {
		return messageExt;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public byte[] getBody() {
		return body;
	}

	public void setBody(byte[] body) {
		this.body = body;
	}
    
    
      
}  