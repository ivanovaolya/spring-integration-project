package com.mentoring.location;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.json.Json;

import com.mentoring.Application;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.MessageChannel;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author ivanovaolyaa
 * @version 11/13/2017
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class IntegrationLocationTest {

    @Autowired
    public MessageChannel ipChannel;

    @Autowired
    public MessageChannel locationChannel;

    @Autowired
    public ConnectionFactory connectionFactory;

    private final String TEST_IP = "208.80.152.201";

    @Test
    public void testSendMessageToIpQueueAndGetLocationJson() throws Exception {
        final Connection connection = connectionFactory.createConnection();
        connection.start();

        final Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        final Queue ipQueue = session.createQueue(IntegrationConfiguration.IP_ADDRESS_QUEUE);
        final MessageProducer producer = session.createProducer(ipQueue);
        final TextMessage message = session.createTextMessage(TEST_IP);
        producer.send(message);

        final Queue locationQueue = session.createQueue(IntegrationConfiguration.LOCATION_QUEUE);
        final MessageConsumer consumer = session.createConsumer(locationQueue);

        final javax.jms.TextMessage receivedMessage = (TextMessage) consumer.receive(5000);
        assertNotNull(receivedMessage);
        System.out.println(receivedMessage.toString());
        assertEquals(buildLocationJsonResponse(), receivedMessage.getText());
        connection.close();
    }

    private String buildLocationJsonResponse() {
        return Json.createObjectBuilder()
                .add("country", "United States")
                .add("countryCode", "US")
                .add("regionName", "Ohio")
                .add("timezone", "America/New_York")
                .build().toString();
    }
}
