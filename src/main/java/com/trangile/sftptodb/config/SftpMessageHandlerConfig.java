package com.trangile.sftptodb.config;

import com.trangile.sftptodb.service.ExcelProcessingService;
import com.trangile.sftptodb.service.sftp.SftpArchivalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.MessageHandler;

import java.io.File;
import java.nio.file.Files;

@Configuration
@Slf4j
public class SftpMessageHandlerConfig {

    @Bean
    @ServiceActivator(inputChannel = "sftpChannel")
    public MessageHandler fileHandler(
            ExcelProcessingService excelProcessingService,
            SftpArchivalService sftpArchivalService) {

        return message -> {

            File file = (File) message.getPayload();

            try {

                log.info("Processing file : {}", file.getName());

                excelProcessingService.processExcelFile(file);

                sftpArchivalService.handleSuccess(file);

            } catch (Exception ex) {

                log.error("Failed to process file : {}", file.getName(), ex);

                sftpArchivalService.handleFailure(file);

            } finally {

                try {
                    Files.deleteIfExists(file.toPath());
                } catch (Exception e) {
                    log.error("Failed to delete local file : {}", file.getName(), e);
                }
            }
        };
    }
}