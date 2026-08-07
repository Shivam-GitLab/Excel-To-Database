package com.trangile.sftptodb.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.sshd.sftp.client.SftpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.file.filters.AcceptOnceFileListFilter;
import org.springframework.integration.file.remote.session.SessionFactory;
import org.springframework.integration.sftp.filters.SftpSimplePatternFileListFilter;
import org.springframework.integration.sftp.inbound.SftpInboundFileSynchronizer;
import org.springframework.integration.sftp.inbound.SftpInboundFileSynchronizingMessageSource;
import org.springframework.messaging.MessageChannel;

import java.nio.file.Files;
import java.nio.file.Path;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class SftpInboundConfig {

    private final SessionFactory<SftpClient.DirEntry> sessionFactory;

    @Value("${sftp.remote-input-directory}")
    private String remoteInputDirectory;

    @Value("${sftp.local-download-directory}")
    private String localDownloadDirectory;

    @Value("${sftp.file-pattern}")
    private String filePattern;

    @Bean
    public SftpInboundFileSynchronizer sftpInboundFileSynchronizer() {

        SftpInboundFileSynchronizer synchronizer =
                new SftpInboundFileSynchronizer(sessionFactory);

        synchronizer.setRemoteDirectory(remoteInputDirectory);
        synchronizer.setDeleteRemoteFiles(false);
        synchronizer.setFilter(new SftpSimplePatternFileListFilter(filePattern));

        log.info("Remote Input Directory : {}", remoteInputDirectory);
        log.info("File Pattern : {}", filePattern);

        return synchronizer;
    }

    @Bean
    @InboundChannelAdapter(
            value = "sftpChannel",
            poller = @Poller(
                    fixedDelay = "${sftp.poll-delay}",
                    maxMessagesPerPoll = "${sftp.max-messages-per-poll}"
            )
    )
    public MessageSource<?> sftpMessageSource() {

        Path downloadDirectory = Path.of(localDownloadDirectory);

        try {
            Files.createDirectories(downloadDirectory);
        } catch (Exception ex) {
            throw new IllegalStateException(
                    "Unable to create local download directory : " + downloadDirectory,
                    ex
            );
        }

        SftpInboundFileSynchronizingMessageSource source =
                new SftpInboundFileSynchronizingMessageSource(
                        sftpInboundFileSynchronizer());

        source.setLocalDirectory(downloadDirectory.toFile());
        source.setAutoCreateLocalDirectory(true);
        source.setLocalFilter(new AcceptOnceFileListFilter<>());

        return source;
    }



    @Bean
    public MessageChannel sftpChannel() {
        return new DirectChannel();
    }
}