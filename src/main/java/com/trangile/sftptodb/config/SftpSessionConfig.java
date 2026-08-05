/*
package com.trangile.sftptodb.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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

    @Bean
    public DefaultSftpSessionFactory sftpSessionFactory() {

        DefaultSftpSessionFactory factory = new DefaultSftpSessionFactory();

        factory.setHost(host);
        factory.setPort(port);
        factory.setUser(user);
        factory.setPassword(password);

        // Development
        factory.setAllowUnknownKeys(true);

        return factory;
    }
}*/

package com.trangile.sftptodb.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
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

    @Bean
    public DefaultSftpSessionFactory sftpSessionFactory() {

        DefaultSftpSessionFactory factory = new DefaultSftpSessionFactory(true);

        factory.setHost(host);
        factory.setPort(port);
        factory.setUser(user);
        factory.setPassword(password);

        factory.setKnownHostsResource(
                new FileSystemResource(System.getProperty("user.home") + "/.ssh/known_hosts")
        );

        factory.setAllowUnknownKeys(false);

        return factory;
    }
}