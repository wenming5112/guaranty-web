package com.example.guaranty.config.fabric;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author ming
 * @version 1.0.0
 * @date 2020/12/8 15:46
 **/
@ConfigurationProperties(prefix = "fabric-sdk")
@Component
@Data
public class SdkProperties {
    private Boolean tls;
    private String caName;
    private String caAdminName;
    private String caAdminPass;
    private String caIp;
    private String caPort;
    private String caDep;
    private String orgName;
    private String orgMspId;
    private String orgDomainName;
    private String channelName;
    private String chainCodeName;
    private String chainCodeSource;
    private String chainCodeVersion;
}
