package com.example.guaranty.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author ming
 * @version 1.0.0
 * @date 2020/11/27 15:36
 **/

@ConfigurationProperties(prefix = "email-verify")
@Component
@Data
public class EmailVerifyProperties {
    private String from;
    private String subject;
    private String template;
    private Long expireTime;
    private String replaceStr;
}
