package com.mentoring.topic;

import javax.jms.ConnectionFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

/**
 * @author ivanovaolyaa
 * @version 10/19/2017
 */
@Configuration
public class Config {

    @Autowired
    private ConnectionFactory jmsConnectionFactory;

}
