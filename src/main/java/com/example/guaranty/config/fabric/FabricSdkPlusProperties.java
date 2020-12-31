package com.example.guaranty.config.fabric;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * fabric sdk config
 *
 * @author ming
 * @version 1.0.0
 * @date 2020/10/15 17:38
 **/
@ConfigurationProperties(prefix = "fabric-sdk-plus")
@Component
@Data
public class FabricSdkPlusProperties {
    private String channelName;
    private String connectionFilePath;
}
