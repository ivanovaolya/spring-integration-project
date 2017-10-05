package com.mentoring.integration;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.file.FileReadingMessageSource;
import org.springframework.integration.file.FileWritingMessageHandler;
import org.springframework.integration.file.filters.SimplePatternFileListFilter;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

/**
 * @author ivanovaolyaa
 * @version 10/5/2017
 */
@Configuration
public class IntegrationFlowConfig {

    @Autowired
    public File outboundFileDirectory;

    @Bean
    public MessageChannel inboundFileChannel() {
        final DirectChannel channel = new DirectChannel();
        channel.subscribe(fileWritingMessageHandler());

        return channel;
    }

    @Bean
    @InboundChannelAdapter(channel = "inboundFileChannel",
            poller = @Poller(fixedDelay = "1000", maxMessagesPerPoll = "1"))
    public MessageSource<File> fileMessageSource(@Value("${inbound.file.path}") File directory) {
        FileReadingMessageSource fileReadingMessageSource = new FileReadingMessageSource();
        fileReadingMessageSource.setDirectory(directory);
        fileReadingMessageSource.setFilter(new SimplePatternFileListFilter("*.txt"));

        return fileReadingMessageSource;
    }

    @Bean
    public MessageHandler fileWritingMessageHandler() {
        final FileWritingMessageHandler handler = new FileWritingMessageHandler(outboundFileDirectory);
        handler.setExpectReply(false);
        handler.setDeleteSourceFiles(true);

        return handler;
    }

}
