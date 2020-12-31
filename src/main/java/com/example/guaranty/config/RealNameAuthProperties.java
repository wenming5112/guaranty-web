package com.example.guaranty.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author ming
 * @version 1.0.0
 * @date 2020/11/27 11:29
 **/
@ConfigurationProperties(prefix = "real-name-auth")
@Component
@Data
public class RealNameAuthProperties {
    private String appCode;
    private String host;
    private String path;
    private String method;
}
