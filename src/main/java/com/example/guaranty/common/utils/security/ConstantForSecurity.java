package com.example.guaranty.common.utils.security;

/**
 * 算法常量
 *
 * @author ming
 * @version 1.0.0
 * @date 2019/8/22 17:17
 **/
public class ConstantForSecurity {

    /**
     * 加密算法AES
     */
    protected static final String AES_KEY_ALGORITHM = "AES";

    /**
     * key的长度，Wrong key size: must be equal to 128, 192 or 256
     * 传入时需要16、24、36
     */
    protected static final Integer AES_KEY_LENGTH = 16 * 8;

    /**
     * 算法名称/加密模式/数据填充方式
     * 默认：AES/ECB/PKCS5Padding
     */
    protected static final String AES_ALGORITHMS = "AES/ECB/PKCS5Padding";

    /**
     * 教秘算法RSA
     */
    protected static String RSA_KEY_TYPE = "RSA";

    /**
     * JDK方式RSA加密最大只有1024位
     */
    protected static int RSA_KEY_SIZE = 1024;

    protected static int RSA_ENCODE_PART_SIZE = RSA_KEY_SIZE / 8;

    protected static final String RSA_PUBLIC_KEY_NAME = "public";

    protected static final String RSA_PRIVATE_KEY_NAME = "private";

    /**
     * 默认的密码字符串组合，用来将字节转换成 16 进制表示的字符,apache校验下载的文件的正确性用的就是默认的这个组合
     */
    public static char[] HEX_DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    public static final String HEX_NUMS_STR = "0123456789ABCDEF";

    public static final Integer SALT_LEN = 12;

}
