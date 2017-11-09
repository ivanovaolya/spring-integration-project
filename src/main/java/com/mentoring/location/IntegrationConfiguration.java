package com.mentoring.location;

import java.util.UUID;

import javax.jms.ConnectionFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.Transformer;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.jms.Jms;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.web.client.RestTemplate;

/**
 * @author ivanovaolyaa
 * @version 10/12/2017
 */
@Configuration
@IntegrationComponentScan
@EnableJms
public class IntegrationConfiguration {

    @Autowired
    private ConnectionFactory jmsConnectionFactory;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String IP_ADDRESS_QUEUE = "ip.queue";

    private static final String LOCATION_QUEUE = "location.queue";

    private Logger logger = LoggerFactory.getLogger(IntegrationConfiguration.class);

    @Bean
    public MessageChannel ipChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel locationChannel() {
        return new DirectChannel();
    }

    /**
     * Consumes messages from IP_ADDRESS_QUEUE and populates ipChannel
     *
     * @return IntegrationFlow - the central component of the Spring Integration Java DSL.
     * The IntegrationFlow is a Consumer functional interface,
     * so we can minimize our code and concentrate just only on the integration scenario requirements.
     */
    @Bean
    public IntegrationFlow jmsInboundIntegrationFlow() {
        return IntegrationFlows.from(Jms.messageDrivenChannelAdapter(jmsConnectionFactory)
                .destination(IP_ADDRESS_QUEUE))
                .channel(ipChannel())
                .get();
    }

    /**
     * Consumes messages from ipChannel, transforms the payload using defined @Transformer,
     * and populates locationChannel
     */
    @Bean
    public IntegrationFlow geolocationFlow() {
        return IntegrationFlows.from(ipChannel())
                .transform(this)
                .channel(locationChannel())
                .get();
    }

    /**
     * Consumes messages from locationChannel and sends them to LOCATION_QUEUE
     */
    @Bean
    public IntegrationFlow jmsOutboundIntegrationFlow() {
        return IntegrationFlows.from("locationChannel")
                .handle(Jms.outboundAdapter(jmsConnectionFactory).destination(LOCATION_QUEUE))
                .get();
    }

    @Transformer
    public String transform(String payload, @Header("id") UUID id) {
        logger.info(String.format("Handled message: id = %s, payload = %s", id, payload));

        RestTemplate restTemplate = new RestTemplate();
        LocationJson json = restTemplate.getForObject("http://ip-api.com/json/" + payload, LocationJson.class);
        String location = null;

        try {
            location = objectMapper.writeValueAsString(json);
            logger.info(String.format("Transformed message: %s", location));
        } catch (Exception ex) {
            logger.error("Caught: " + ex);
        }

        return location;
    }

}
