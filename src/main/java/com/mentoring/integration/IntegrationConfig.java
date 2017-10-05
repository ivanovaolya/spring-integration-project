package com.mentoring.integration;

import java.io.File;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.config.EnableIntegration;

/**
 * @author ivanovaolyaa
 * @version 10/5/2017
 */
@Configuration
@EnableIntegration
public class IntegrationConfig {

    @Bean
    public File inboundFileDirectory(@Value("${inbound.file.path}") String path) {
        return new File(path);
    }

    @Bean
    public File outboundFileDirectory(@Value("${outbound.file.path}") String path) {
        return new File(path);
    }

}