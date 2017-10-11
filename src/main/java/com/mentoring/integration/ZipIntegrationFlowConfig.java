package com.mentoring.integration;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.annotation.Transformer;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.file.FileReadingMessageSource;
import org.springframework.integration.file.filters.SimplePatternFileListFilter;
import org.springframework.integration.handler.LoggingHandler;
import org.springframework.integration.zip.transformer.UnZipTransformer;
import org.springframework.integration.zip.transformer.ZipResultType;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;

/**
 * @author ivanovaolyaa
 * @version 10/11/2017
 */
@Configuration
public class ZipIntegrationFlowConfig {

    @Autowired
    public File outboundFileDirectory;

    @Bean
    public MessageChannel inboundZipChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel outboundZipChannel() {
        return new DirectChannel();
    }

    /**
     * @InboundChannelAdapter connects a single sender or receiver to a Message Channel
     */
    @Bean
    @InboundChannelAdapter(channel = "inboundZipChannel",
            poller = @Poller(fixedDelay = "1000", maxMessagesPerPoll = "1"))
    public MessageSource<File> zipMessageSource(@Value("${inbound.file.path}") File directory) {
        FileReadingMessageSource fileReadingMessageSource = new FileReadingMessageSource();
        fileReadingMessageSource.setDirectory(directory);
        fileReadingMessageSource.setFilter(new SimplePatternFileListFilter("*.zip"));

        return fileReadingMessageSource;
    }

    @Bean
    @Transformer(inputChannel = "inboundZipChannel", outputChannel = "outboundZipChannel")
    public UnZipTransformer unZipTransformer() {
        UnZipTransformer unZipTransformer = new UnZipTransformer();
        unZipTransformer.setExpectSingleResult(false); // if set to true and more than 1 zip entry were detected, MessagingException will be raised
        unZipTransformer.setZipResultType(ZipResultType.FILE);
        unZipTransformer.setWorkDirectory(outboundFileDirectory);
        unZipTransformer.setDeleteFiles(true);

        return unZipTransformer;
    }

    /**
     * @ServiceActivator is the endpoint type for connecting any Spring-managed Object to an input channel
     * so that it may play the role of a service. If the service produces output, it may also be connected to an output channel.
     */
    @Bean
    @ServiceActivator(inputChannel = "outboundZipChannel")
    public LoggingHandler logging() {
        return new LoggingHandler(LoggingHandler.Level.INFO);
    }

}
