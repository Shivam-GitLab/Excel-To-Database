package com.trangile.sftptodb.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.file.filters.AcceptOnceFileListFilter;
import org.springframework.integration.sftp.filters.SftpSimplePatternFileListFilter;
import org.springframework.integration.sftp.inbound.SftpInboundFileSynchronizer;
import org.springframework.integration.sftp.inbound.SftpInboundFileSynchronizingMessageSource;
import org.springframework.integration.sftp.session.DefaultSftpSessionFactory;
import org.springframework.messaging.MessageChannel;

import java.io.File;

@Configuration
public class SftpInboundConfig {

    @Value("${sftp.remote.directory}")
    private String remoteDirectory;

    @Value("${sftp.local.directory}")
    private String localDirectory;

    private final DefaultSftpSessionFactory sessionFactory;

    public SftpInboundConfig(DefaultSftpSessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Bean
    public SftpInboundFileSynchronizer sftpInboundFileSynchronizer() {

        SftpInboundFileSynchronizer synchronizer =
                new SftpInboundFileSynchronizer(sessionFactory);

        synchronizer.setDeleteRemoteFiles(false);
        synchronizer.setRemoteDirectory(remoteDirectory);
        synchronizer.setFilter(new SftpSimplePatternFileListFilter("*.xlsx"));

        return synchronizer;
    }

    @Bean
    @InboundChannelAdapter(
            value = "sftpChannel",
            poller = @Poller(
                    fixedDelay = "${sftp.poll-delay}",
                    maxMessagesPerPoll = "5"
            )
    )
    public MessageSource<File> sftpMessageSource() {

        SftpInboundFileSynchronizingMessageSource source =
                new SftpInboundFileSynchronizingMessageSource(
                        sftpInboundFileSynchronizer());

        source.setLocalDirectory(new File(localDirectory));
        source.setAutoCreateLocalDirectory(true);
        source.setLocalFilter(new AcceptOnceFileListFilter<>());

        return source;
    }

    @Bean
    public MessageChannel sftpChannel() {
        return new DirectChannel();
    }
}