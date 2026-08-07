package com.trangile.sftptodb.config;

import com.trangile.sftptodb.service.ExcelProcessingService;
import com.trangile.sftptodb.service.sftp.SftpArchivalService;
import com.trangile.sftptodb.service.staging.StagingProcessingService;
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
            StagingProcessingService stagingProcessingService,
            SftpArchivalService sftpArchivalService) {

        return message -> {

            File file = (File) message.getPayload();

            try {

                log.info("Started processing file : {}", file.getName());

                // Phase-1 : Excel -> Staging
                excelProcessingService.processExcelFile(file);

                // Phase-2 : Staging -> Orders -> OrderDetails
                stagingProcessingService.processPendingRecords();

                // Archive on Success
                sftpArchivalService.handleSuccess(file);

                log.info("File processed successfully : {}", file.getName());

            } catch (Exception ex) {

                log.error("Failed to process file : {}", file.getName(), ex);

                sftpArchivalService.handleFailure(file);

            } finally {

                try {

                    Files.deleteIfExists(file.toPath());

                    log.info("Local file deleted : {}", file.getName());

                } catch (Exception ex) {

                    log.error("Unable to delete local file : {}", file.getName(), ex);
                }
            }
        };
    }
}