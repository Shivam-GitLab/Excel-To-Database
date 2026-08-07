package com.trangile.sftptodb.config;

import org.apache.sshd.sftp.client.SftpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.integration.file.remote.session.CachingSessionFactory;
import org.springframework.integration.file.remote.session.Session;
import org.springframework.integration.file.remote.session.SessionFactory;
import org.springframework.integration.sftp.session.DefaultSftpSessionFactory;

@Configuration
public class SftpSessionConfig {

    @Value("${sftp.host}")
    private String host;

    @Value("${sftp.port}")
    private int port;

    @Value("${sftp.user}")
    private String user;

    @Value("${sftp.password}")
    private String password;

    @Value("${sftp.known-hosts}")
    private String knownHosts;

    @Bean
    public SessionFactory<SftpClient.DirEntry> sftpSessionFactory() {

        DefaultSftpSessionFactory factory =
                new DefaultSftpSessionFactory(true);

        factory.setHost(host);
        factory.setPort(port);
        factory.setUser(user);
        factory.setPassword(password);

        factory.setKnownHostsResource(
                new FileSystemResource(knownHosts)
        );

        factory.setAllowUnknownKeys(false);

        return new CachingSessionFactory<>(factory);
    }

    @Bean
    public CommandLineRunner testSftp(SessionFactory<SftpClient.DirEntry> sessionFactory) {

        return args -> {

            try (Session<SftpClient.DirEntry> session = sessionFactory.getSession()) {

                System.out.println("======================================");
                System.out.println("SFTP CONNECTION SUCCESS");
                System.out.println("======================================");

                System.out.println("Listing files from /INPUT");

                var files = session.list("/INPUT");

                if (files == null) {
                    System.out.println("No files found.");
                } else {

                    for (Object file : files) {
                        System.out.println(file);
                    }
                }

                System.out.println("======================================");

            } catch (Exception ex) {

                System.err.println("SFTP CONNECTION FAILED");
                ex.printStackTrace();
            }
        };
    }
}