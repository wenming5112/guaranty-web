package com.example.guaranty.common.constant;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Web 常量
 *
 * @author ming
 * @version 1.0.0
 * @date 2020/4/18 15:24
 **/
@Data
@Component
public class WebConstant {
    // ============================ SMS Config ============================


    // ============================ File Upload ============================
    /**
     * 文件后缀
     */
    public static final String FILE_POINT = ".";


    // ============================ Default Page setting ============================
    /**
     * 默认页码
     */
    public static final String PAGE_NO = "1";

    /**
     * 默认显示多少个
     */
    public static final String PAGE_SIZE = "10";

    // ============================ token 过期时间 ============================
    /**
     * token expireTime
     */
    @Value("${jwt.token.expire-time}")
    private long expireTime;

    public static final String TOKEN_HEADER = "Authorization";

    // ============================ 其他配置 ============================

    /**
     * Maximum number of retries
     */
    public static final Integer MAX_NUM_OF_RETRY = 3;

    public static final String MSG_STR = "msg";
    public static final String CODE_STR = "code";
    public static final String DATA_STR = "data";

    public static final String FABRIC_ROOT_DIR = "D:/fabric/";

    public static final String DEV_ENV = "dev";
    public static final String PRD_ENV = "prd";
    public static final String UAT_ENV = "uat";

    @Value("${spring.profiles.active}")
    private String activeProfile;

}
