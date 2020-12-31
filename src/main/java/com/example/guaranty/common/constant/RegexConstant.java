package com.example.guaranty.common.constant;

/**
 * 正则常量
 *
 * @author ming
 * @version 1.0.0
 * @date 2019 9:17
 */
public class RegexConstant {

    /**
     * 手机正则
     */
    public final static String TELEPHONE_REG = "^(13[0-9]|14[579]|15[0-3,5-9]|16[6]|17[0135678]|18[0-9]|19[89])\\d{8}$";

    /**
     * 邮箱正则
     */
    public final static String EMAIL_REG = "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";

    /**
     * 用户名正则，首个必须是字母,并且只由数字和字母组成,长度4-20位
     */
    public final static String USERNAME_REG = "^[a-zA-Z][a-zA-Z0-9_]{3,19}$";

    /**
     * IP正则
     */
    public final static String IP_REG = "((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})(\\.((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})){3}";

    /**
     * 首个必须是字母,并且只由数字和字母组成,长度4-20位
     */
    public final static String NUMBER_CHAR_REG = "^[a-zA-Z][a-zA-Z0-9]{3,19}$";

    /**
     * 通道名只能包含小写字母,长度4-20位
     */
    public final static String CHANNEL_NAME_REG = "^[a-z]{4,20}$";

    /**
     * 组织名首个必须是大写字母,并且只由数字字母组成,长度4-20位
     */
    public final static String ORG_CHAR_REG = "^[A-Z][a-zA-Z0-9]{3,19}$";

    /**
     * 首个必须是字母,并且只由数字字母下划线组成,长度4-20位
     */
    public final static String CHAIN_CODE_CHAR_REG = "^[a-zA-Z][a-zA-Z0-9_]{3,19}$";

    /**
     * 首个必须是字母,并且只由数字和字母组成,长度4-20位
     */
    public final static String CA_REG = "^[a-zA-Z][a-zA-Z0-9]{2,19}$";

    /**
     * 密码正则，首个必须是字母，有数字字母下划线组成长度位6-20位
     */
    public final static String PASSWORD_REG = "^[a-zA-Z][a-zA-Z0-9_]{5,19}";

    /**
     * 强密码规则，密码强度正则，最少6位，包括至少1个大写字母，1个小写字母，1个数字，1个特殊字符
     */
    public final static String PASSWORD_HIGHT_REG = "^.*(?=.{6,})(?=.*\\d)(?=.*[A-Z])(?=.*[a-z])(?=.*[!@#$%^&*? ]).*$";
}
