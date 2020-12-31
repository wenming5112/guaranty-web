package com.example.guaranty.config.upload;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 文件上传配置
 *
 * @author ming
 * @version 1.0.0
 * @date 2020/11/24 9:50
 **/
@ConfigurationProperties(prefix = "file-upload")
@Component
@Data
public class FileUploadProperties {
    private String path;
    private String baseUrl;
}
