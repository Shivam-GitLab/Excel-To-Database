package com.trangile.sftptodb.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.MessageChannel;
import org.springframework.integration.channel.DirectChannel;
import com.trangile.sftptodb.entity.ProcessingLog;
import com.trangile.sftptodb.repository.ProcessingLogRepository;
import com.trangile.sftptodb.service.ExcelProcessingService;
import com.trangile.sftptodb.service.SftpArchivalService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.file.filters.AcceptOnceFileListFilter;
import org.springframework.integration.sftp.filters.SftpSimplePatternFileListFilter;
import org.springframework.integration.sftp.inbound.SftpInboundFileSynchronizer;
import org.springframework.integration.sftp.inbound.SftpInboundFileSynchronizingMessageSource;
import org.springframework.integration.sftp.session.DefaultSftpSessionFactory;
import org.springframework.messaging.MessageHandler;
import com.trangile.sftptodb.exception.FileValidationException;

import java.io.File;
import java.nio.file.Files;
import java.time.LocalDateTime;


@Configuration
@Slf4j
public class SftpIntegrationConfig {

    @Value("${sftp.host}")
    private String host;

    @Value("${sftp.port}")
    private int port;

    @Value("${sftp.user}")
    private String user;

    @Value("${sftp.password}")
    private String password;

    @Value("${sftp.poll-delay}")
    private String pollDelay;

    @Value("${sftp.remote.directory}")
    private String remoteDirectory;

    @Value("${sftp.local.directory}")
    private String localDirectory;

    @Bean
    public DefaultSftpSessionFactory sftpSessionFactory() {

        DefaultSftpSessionFactory factory = new DefaultSftpSessionFactory();

        factory.setHost(host);
        factory.setPort(port);
        factory.setUser(user);
        factory.setPassword(password);
        factory.setAllowUnknownKeys(true);

        return factory;
    }

    @Bean
    public SftpInboundFileSynchronizer sftpInboundFileSynchronizer() {

        SftpInboundFileSynchronizer synchronizer =
                new SftpInboundFileSynchronizer(sftpSessionFactory());

        synchronizer.setDeleteRemoteFiles(false);
        synchronizer.setRemoteDirectory(remoteDirectory);
        synchronizer.setFilter(new SftpSimplePatternFileListFilter("*.xlsx"));

        return synchronizer;
    }

    @Bean
    @InboundChannelAdapter(
            value = "sftpChannel",
            poller = @Poller(fixedDelay = "${sftp.poll-delay}")
    )
    public MessageSource<File> sftpMessageSource() {

        SftpInboundFileSynchronizingMessageSource source =
                new SftpInboundFileSynchronizingMessageSource(
                        sftpInboundFileSynchronizer()
                );

        source.setLocalDirectory(new File(localDirectory));
        source.setAutoCreateLocalDirectory(true);
        source.setLocalFilter(new AcceptOnceFileListFilter<>());

        return source;
    }

    @Bean
    @ServiceActivator(inputChannel = "sftpChannel")
    public MessageHandler handleDownloadedFile(
            ExcelProcessingService excelProcessingService,
            SftpArchivalService sftpArchivalService,
            ProcessingLogRepository processingLogRepository) {

        return message -> {

            File downloadedFile = (File) message.getPayload();

            ProcessingLog processingLog = new ProcessingLog();
            processingLog.setFileName(downloadedFile.getName());
            processingLog.setProcessedAt(LocalDateTime.now());

            try {

                log.info("==============================================");
                log.info("Started processing file: {}", downloadedFile.getName());
                log.info("==============================================");

                // Read Excel and save into PostgreSQL
                excelProcessingService.processExcelFile(downloadedFile);

                // Move remote file to archive
                sftpArchivalService.handleSuccess(downloadedFile);

                // Delete local temp file
                Files.deleteIfExists(downloadedFile.toPath());

                processingLog.setStatus("SUCCESS");
                processingLog.setErrorMessage(null);

                log.info("Successfully processed file: {}", downloadedFile.getName());

            } catch (FileValidationException e) {

                log.warn("Validation failed for file: {}. Reason: {}",
                        downloadedFile.getName(),
                        e.getMessage());

                handleFailure(downloadedFile, processingLog, e, sftpArchivalService);

            } catch (Exception e) {

                log.error("Unexpected error while processing file: {}",
                        downloadedFile.getName(), e);

                handleFailure(downloadedFile, processingLog, e, sftpArchivalService);

            } finally {

                processingLogRepository.save(processingLog);
            }
        };
    }

    private void handleFailure(File downloadedFile,
                               ProcessingLog processingLog,
                               Exception e,
                               SftpArchivalService sftpArchivalService) {

        try {
            // Move remote file to error folder
            sftpArchivalService.handleFailure(downloadedFile);

        } catch (Exception ex) {

            log.error("Failed to move remote file to error folder: {}",
                    downloadedFile.getName(), ex);

        } finally {

            try {
                Files.deleteIfExists(downloadedFile.toPath());
            } catch (Exception ex) {
                log.error("Failed to delete local temp file: {}",
                        downloadedFile.getName(), ex);
            }
        }

        processingLog.setStatus("FAILED");
        processingLog.setErrorMessage(e.getMessage());
    }
    @Bean
    public MessageChannel sftpChannel() {
        return new DirectChannel();
    }
}