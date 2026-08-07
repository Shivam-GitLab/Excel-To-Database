package com.trangile.sftptodb.service.sftp;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.sshd.sftp.client.SftpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.integration.file.remote.session.Session;
import org.springframework.integration.file.remote.session.SessionFactory;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
@Slf4j
@RequiredArgsConstructor
public class SftpArchivalService {

    private final SessionFactory<SftpClient.DirEntry> sessionFactory;

    @Value("${sftp.remote-input-directory}")
    private String inputDirectory;

    @Value("${sftp.remote-archive-directory}")
    private String archiveDirectory;

    @Value("${sftp.remote-error-directory}")
    private String errorDirectory;

    public void handleSuccess(File file) {
        moveRemoteFile(file, archiveDirectory);
    }

    public void handleFailure(File file) {
        moveRemoteFile(file, errorDirectory);
    }

    private void moveRemoteFile(File file, String destinationDirectory) {

        if (file == null) {
            throw new IllegalArgumentException("File cannot be null.");
        }

        String sourcePath = buildRemotePath(inputDirectory, file.getName());
        String destinationPath = buildRemotePath(destinationDirectory, file.getName());

        try (Session<SftpClient.DirEntry> session = sessionFactory.getSession()) {

            log.info("Moving remote file [{}] -> [{}]", sourcePath, destinationPath);

            session.rename(sourcePath, destinationPath);

            log.info("Remote file moved successfully : {}", file.getName());

        } catch (Exception ex) {

            log.error("Failed to move remote file : {}", file.getName(), ex);

            throw new RuntimeException(
                    "Unable to move remote file : " + file.getName(),
                    ex
            );
        }
    }

    private String buildRemotePath(String directory, String fileName) {

        String normalizedDirectory = directory
                .replace("\\", "/")
                .replaceAll("/+$", "");

        return normalizedDirectory + "/" + fileName;
    }
}