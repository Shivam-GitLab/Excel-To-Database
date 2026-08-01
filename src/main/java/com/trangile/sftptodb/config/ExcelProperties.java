package com.trangile.sftptodb.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "excel")
public class ExcelProperties {

    private long maxFileSize;

    private List<String> expectedHeaders;
}