package com.example.guaranty.common.utils;


import org.apache.commons.lang3.StringUtils;

/**
 * 数据脱敏
 *
 * @author ming
 * @version 1.0.0
 * @date 2019/06/20 10:26
 */
public class DesensitizeUtil {

    /**
     * @param fullName 脱敏字符串
     * @param index    左边留下显示字符串的长度
     * @return String
     */
    public static String left(String fullName, int index) {
        if (StringUtils.isBlank(fullName)) {
            return "";
        }
        String name = StringUtils.left(fullName, index);
        return StringUtils.rightPad(name, StringUtils.length(fullName), "*");
    }

    /**
     * @param name  脱敏字符串
     * @param index 前面保留长度
     * @param end   结尾保留长度
     * @return String
     */
    public static String around(String name, int index, int end) {
        if (StringUtils.isBlank(name)) {
            return "";
        }
        return StringUtils.left(name, index).concat(StringUtils.removeStart(StringUtils.leftPad(StringUtils.right(name, end), StringUtils.length(name), "*"), "***"));
    }

    /**
     * @param num 脱敏字符串
     * @param end 结尾保留长度
     * @return String
     */
    public static String right(String num, int end) {
        if (StringUtils.isBlank(num)) {
            return "";
        }
        return StringUtils.leftPad(StringUtils.right(num, end), StringUtils.length(num), "*");
    }


    /**
     * [中文姓名] 只显示第一个汉字，其他隐藏为2个星号<例子：李**>
     *
     * @param fullName 中文名
     * @return String
     */
    public static String formatChineseName(String fullName) {
        if (StringUtils.isBlank(fullName)) {
            return "";
        }
        String name = StringUtils.left(fullName, 1);
        return StringUtils.rightPad(name, StringUtils.length(fullName), "*");
    }

    /**
     * [身份证号] 110****58，前面保留3位明文，后面保留2位明文
     *
     * @param name 身份证号
     * @return String
     */
    public static String formatIdentificationNum(String name) {
        if (StringUtils.isBlank(name)) {
            return "";
        }
        return StringUtils.left(name, 3).
                concat(StringUtils.removeStart(StringUtils.
                        leftPad(StringUtils.right(name, 2), StringUtils.length(name), "*"), "***"));
    }

    /**
     * [固定电话] 后四位，其他隐藏<例子：****1234>
     *
     * @param num 电话号码
     * @return String
     */
    public static String formatFixedPhone(String num) {
        if (StringUtils.isBlank(num)) {
            return "";
        }
        return StringUtils.leftPad(StringUtils.right(num, 4), StringUtils.length(num), "*");
    }


    /**
     * [手机号码] 前四位，后四位，其他隐藏<例子:138****1234>
     *
     * @param num 电话号码
     * @return String
     */
    public static String formatMobilePhone(String num) {
        if (StringUtils.isBlank(num)) {
            return "";
        }
        return StringUtils.left(num, 3).concat(StringUtils.removeStart(StringUtils.leftPad(StringUtils.right(num, 4), StringUtils.length(num), "*"), "***"));
    }

    /**
     * [地址] 只显示到地区，不显示详细地址；我们要对个人信息增强保护<例子：北京市海淀区****>
     *
     * @param address       地址
     * @param sensitiveSize 敏感信息长度
     * @return String
     */
    public static String formatAddress(String address, int sensitiveSize) {
        if (StringUtils.isBlank(address)) {
            return "";
        }
        int length = StringUtils.length(address);
        return StringUtils.rightPad(StringUtils.left(address, length - sensitiveSize), length, "*");
    }

    /**
     * [电子邮箱] 邮箱前缀仅显示第一个字母，前缀其他隐藏，用星号代替，@及后面的地址显示<例子:g**@163.com>
     *
     * @param email 邮箱
     * @return String
     */
    public static String formatEmail(String email) {
        if (StringUtils.isBlank(email)) {
            return "";
        }
        int index = StringUtils.indexOf(email, "@");
        if (index <= 1) {
            return email;
        } else {
            return StringUtils.rightPad(StringUtils.left(email, 3), index, "*").concat(StringUtils.mid(email, index, StringUtils.length(email)));
        }
    }


    /**
     * [银行卡号] 前四位，后四位，其他用星号隐藏每位1个星号<例子:6222**********1234>
     *
     * @param cardNum 银行卡
     * @return String
     */
    public static String formatBankCard(String cardNum) {
        if (StringUtils.isBlank(cardNum)) {
            return "";
        }
        return StringUtils.left(cardNum, 4).concat(StringUtils.removeStart(StringUtils.leftPad(StringUtils.right(cardNum, 4), StringUtils.length(cardNum), "*"), "******"));
    }

    /**
     * [公司开户银行联号] 公司开户银行联行号,显示前两位，其他用星号隐藏，每位1个星号<例子:12********>
     *
     * @param code 银行卡
     * @return String
     */
    public static String formatCnapsCode(String code) {
        if (StringUtils.isBlank(code)) {
            return "";
        }
        return StringUtils.rightPad(StringUtils.left(code, 2), StringUtils.length(code), "*");
    }

}
