package com.mentoring.location;

import java.util.Arrays;

import javax.jms.ConnectionFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ivanovaolyaa
 * @version 10/12/2017
 */
@Configuration
public class BeanConfiguration {

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public ConnectionFactory jmsConnectionFactory() {
        final ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory();
        factory.setBrokerURL("tcp://localhost:61616");
        factory.setTrustedPackages(Arrays.asList("com.mentoring", "java.util"));

        return factory;
    }
}
