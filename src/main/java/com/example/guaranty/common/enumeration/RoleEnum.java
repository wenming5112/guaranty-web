package com.example.guaranty.common.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author ming
 * @version 1.0.0
 * @date 2020/11/26 23:35
 **/
@Getter
@AllArgsConstructor
public enum RoleEnum {
    // 角色枚举
    ADMIN("admin", 1),
    USER("user", 2),
    BANK_STAFF("bank-staff", 8),
    BANK_ADMIN("bank-admin", 9);

    private String name;
    private Integer roleId;
}
