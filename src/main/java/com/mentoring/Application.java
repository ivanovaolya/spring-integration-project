package com.mentoring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.config.EnableIntegration;

/**
 * @author ivanovaolyaa
 * @version 10/5/2017
 */
@SpringBootApplication
@IntegrationComponentScan
@EnableIntegration
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
