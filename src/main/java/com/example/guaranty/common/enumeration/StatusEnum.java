package com.example.guaranty.common.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 业务响应枚举
 *
 * @author ming
 * @date 2019:08:26 17:46
 */
@Getter
@AllArgsConstructor
public enum StatusEnum {

    /**
     * 业务响应枚举
     */
    NORMAL(1, 10000, "NORMAL", "正常"),
    INVALID(2, 10001, "INVALID", "无效"),
    LOCK(3, 10002, "LOCK", "已锁定"),
    NO_ACTIVE(4, 10003, "DISABLED", "未激活"),
    ACTIVATED(9, 10008, "ACTIVATED", "已激活"),
    PENDING(10, 10009, "PENDING", "待审批"),
    AGREED(15, 100014, "AGREED", "已同意"),
    ACTIVATING(18, 100017, "ACTIVATING", "激活中");

    private Integer id;
    private Integer code;
    private String enName;
    private String zhName;

}
