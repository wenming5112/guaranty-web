package com.example.guaranty.common.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 响应信息枚举
 *
 * @author ming
 * @version 1.0.0
 * @date 2019/8/14 11:33
 */
@Getter
@AllArgsConstructor
public enum ResponseResultEnum {

    /**
     * 枚举
     */
    SUCCESS(200, "SUCCESS"),
    AUTH_ERROR(403, "权限不足"),
    SERVER_ERROR(500, "服务器异常"),
    PARAMS_ERROR(400, "参数错误"),
    JSON_PARSE_ERROR(155, "Json解析错误"),
    ILLEGAL_STRING(165, "非法字符串"),
    INVALID_SHIRO_AUTH(999, "无效的token"),
    USER_TOKEN_INVALID(401, "用户身份已失效"),
    /**
     * 用户不存在
     */
    USER_NOT_EXIST(1000, "User is not exists!!"),
    /**
     * 用户已存在
     */
    USER_ALREADY_EXIST(1001, "User is already exists!!"),
    /**
     * 短信验证码错误
     */
    SMS_VERIFY_FAILED(1002, "SMS verify failed!!"),
    /**
     * Token 为空
     */
    TOKEN_IS_NULL(1003, "Token is null!! Please re-login!!"),
    /**
     * 用户名或密码错误
     */
    USERNAME_OR_PASSWORD_ERROR(1004, "用户名或密码错误!!"),
    DELETE_SUCCESS(1005, "删除成功"),
    DELETE_FAILED(1006, "删除失败!!"),
    UPDATE_SUCCESS(1007, "修改成功"),
    UPDATE_FAILED(1008, "修改失败!!"),
    ADD_SUCCESS(1008, "新增成功"),
    ADD_FAILED(1008, "新增失败!!");

    private int code;
    private String msg;

}

