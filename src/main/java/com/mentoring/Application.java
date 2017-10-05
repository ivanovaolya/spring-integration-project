package com.mentoring;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * @author ivanovaolyaa
 * @version 10/5/2017
 */
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        new SpringApplicationBuilder()
                .web(false)
                .sources(Application.class)
                .run(args);
    }

}
