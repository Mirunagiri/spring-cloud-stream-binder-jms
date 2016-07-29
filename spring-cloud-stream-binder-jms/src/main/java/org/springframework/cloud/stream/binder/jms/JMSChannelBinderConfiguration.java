/*
 *  Copyright 2002-2016 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.springframework.cloud.stream.binder.jms;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.stream.binder.jms.utils.MessageRecoverer;
import org.springframework.cloud.stream.binder.jms.utils.RepublishMessageRecoverer;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.codec.Codec;
import org.springframework.jms.core.JmsTemplate;

/**
 * Configuration class containing required beans in order to set up the JMS
 * binder.
 *
 * @author Jonathan Sharpe
 * @author Joseph Taylor
 * @author José Carlos Valero
 * @since 1.1
 */
public class JMSChannelBinderConfiguration {

    @Autowired
    private Codec codec;

    @Bean
    JMSMessageChannelBinder jmsMessageChannelBinder(JmsTemplate template,
                ConnectionFactory connectionFactory,
                QueueProvisioner queueProvisioner) throws JMSException {

        JMSMessageChannelBinder jmsMessageChannelBinder = new JMSMessageChannelBinder(
                connectionFactory,
                template,
                queueProvisioner
        );
        jmsMessageChannelBinder.setCodec(codec);
        return jmsMessageChannelBinder;
    }

    @ConditionalOnMissingBean(MessageRecoverer.class)
    @Bean
    MessageRecoverer defaultMessageRecoverer(QueueProvisioner queueProvisioner, JmsTemplate jmsTemplate) {
        return new RepublishMessageRecoverer(queueProvisioner, jmsTemplate);
    }

}
