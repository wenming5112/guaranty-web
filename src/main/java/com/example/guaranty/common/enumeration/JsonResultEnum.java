package com.example.guaranty.common.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 数据返回的常量
 *
 * @author ming
 * @since 2020-09-09 11:11
 */
@Getter
@AllArgsConstructor
public enum JsonResultEnum {
    /**
     * xxx
     */
    SUCCESS("操作成功"),
    TIPS("提示框"),
    FAILURE("操作失败"),
    FAIL("信息错误，请核对信息"),
    EXCEPTION("未知异常"),
    NULL("空值异常"),
    UPDATE_FAIL("修改失败");
    private String message;

}
