package com.trangile.sftptodb.service.sftp;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.integration.sftp.session.DefaultSftpSessionFactory;
import org.springframework.integration.sftp.session.SftpSession;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class SftpArchivalService {

    private final DefaultSftpSessionFactory sessionFactory;

    @Value("${sftp.remote.directory}")
    private String incomingDirectory;

    @Value("${sftp.remote.archive-directory}")
    private String archiveDirectory;

    @Value("${sftp.remote.error-directory}")
    private String errorDirectory;

    public void handleSuccess(File file) {
        moveRemoteFile(file, archiveDirectory);
    }

    public void handleFailure(File file) {
        moveRemoteFile(file, errorDirectory);
    }

    private void moveRemoteFile(File file, String destinationDirectory) {

        Objects.requireNonNull(file, "File cannot be null");

        String source = buildPath(incomingDirectory, file.getName());
        String target = buildPath(destinationDirectory, file.getName());

        try (SftpSession session = sessionFactory.getSession()) {

            log.info("========================================");
            log.info("Moving Remote File");
            log.info("Source      : {}", source);
            log.info("Destination : {}", target);

            session.rename(source, target);

            log.info("Remote file moved successfully.");
            log.info("========================================");

        } catch (Exception ex) {

            log.error("Failed to move remote file [{}]", file.getName(), ex);

            throw new RuntimeException(
                    "Failed to move remote file : " + file.getName(),
                    ex
            );
        }
    }

    private String buildPath(String directory, String fileName) {

        directory = directory
                .replace("\\", "/")
                .replaceAll("/+$", "");

        return directory + "/" + fileName;
    }
}