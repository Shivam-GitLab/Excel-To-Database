spring:
  application:
    name: SftpToDbApp

  datasource:
    url: jdbc:postgresql://localhost:5432/excel_to_db
    username: postgres
    password: ShivamJha91
    driver-class-name: org.postgresql.Driver

  jpa:
    hibernate:
      ddl-auto: none
    show-sql: true
    open-in-view: false
    properties:
      hibernate:
        format_sql: true

server:
  port: 8080

sftp:
  host: localhost
  port: 22
  user: tester
  password: password
  poll-delay: 30000

  remote:
    directory: incoming
    archive-directory: archive
    error-directory: error

  local:
    directory: ./local_temp

excel:
  max-file-size: 10485760

logging:
  level:
    root: INFO
    com.trangile.sftptodb: INFO
    org.springframework.integration: INFO
    org.springframework.integration.sftp: INFO
    org.apache.sshd: WARN
    org.hibernate.SQL: DEBUG