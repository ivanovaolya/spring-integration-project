package com.mentoring.topic;

import javax.jms.ConnectionFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.jms.Jms;

/**
 * @author ivanovaolyaa
 * @version 10/17/2017
 */
@Configuration
public class MultipleSubscriberIntegrationFlow {

    @Autowired
    private ConnectionFactory jmsConnectionFactory;

    private final Logger logger = LoggerFactory.getLogger(MultipleSubscriberIntegrationFlow.class);

    private final String MESSAGE_TOPIC = "message.topic";

    @Bean
    public IntegrationFlow firstSubscriberFlow() {
        return IntegrationFlows.from(Jms.publishSubscribeChannel(jmsConnectionFactory)
                .destination(MESSAGE_TOPIC))
                .handle(message -> logger.info("Received in subscriber 1: " + message.getPayload()))
                .get();
    }

    @Bean
    public IntegrationFlow secondSubscriberFlow() {
        return IntegrationFlows.from(Jms.publishSubscribeChannel(jmsConnectionFactory)
                .destination(MESSAGE_TOPIC))
                .handle(message -> logger.info("Received in subscriber 2: " + message.getPayload()))
                .get();
    }

}
