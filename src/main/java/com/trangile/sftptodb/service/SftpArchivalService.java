package com.trangile.sftptodb.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.integration.sftp.session.DefaultSftpSessionFactory;
import org.springframework.integration.sftp.session.SftpSession;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
@Slf4j
@RequiredArgsConstructor
public class SftpArchivalService {


    private final DefaultSftpSessionFactory sessionFactory;

    @Value("${sftp.remote.directory}")
    private String incomingDir;

    @Value("${sftp.remote.archive-directory}")
    private String archiveDir;

    @Value("${sftp.remote.error-directory}")
    private String errorDir;


    public void handleSuccess(File file) {
        if (file == null) {
            throw new IllegalArgumentException("File cannot be null");
        }
        moveRemoteFile(file.getName(), archiveDir);
    }

    public void handleFailure(File file) {
        if (file == null) {
            throw new IllegalArgumentException("File cannot be null");
        }
        moveRemoteFile(file.getName(), errorDir);
    }

    private void moveRemoteFile(String fileName, String destinationDir) {

        String source = buildPath(incomingDir, fileName);
        String target = buildPath(destinationDir, fileName);

        try (SftpSession session = sessionFactory.getSession()) {
            log.info("========================================");
            log.info("Moving remote file");
            log.info("Source      : {}", source);
            log.info("Destination : {}", target);

            session.rename(source, target);

            log.info("Remote file moved successfully.");
            log.info("========================================");

        } catch (Exception e) {

            log.error("Failed to move remote file [{}] from {} to {}",
                    fileName, source, target, e);

            throw new RuntimeException(
                    "Failed to move remote file from " + source + " to " + target,
                    e
            );
        }
    }


    private String buildPath(String dir, String fileName) {

        dir = dir.replace("\\", "/").replaceAll("/+$", "");

        return dir + "/" + fileName;
    }
}