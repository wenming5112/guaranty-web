package com.example.guaranty.common.utils.security;

import org.apache.commons.lang3.StringUtils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 * 摘要算法工具类
 * 前端采用Crypto-JS实现(MD5、SHA、SHA-256)
 *
 * @author ming
 * @version 1.0.0
 * @date 2020/9/25 9:33
 **/
public class DigestAlgorithmUtils {

    /**
     * 盐，用于混交摘要算法
     */
    private static final String SALT = "&%5124***&&%%$$#@";

    private static final String MD5_DIGEST = "MD5";
    private static final String SHA_DIGEST = "SHA";
    private static final String SHA_256_DIGEST = "SHA-256";
    private static final String SHA_512_DIGEST = "SHA-512";

    /**
     * 生成md5字符串
     *
     * @param data 原数据
     * @return String
     */
    public static String md5Sign(String data) {
        return getString(data, MD5_DIGEST);
    }

    /**
     * md5验证
     *
     * @param data   原数据
     * @param md5Str md5字符串
     * @return boolean
     */
    public static boolean md5Verify(String data, String md5Str) {
        return StringUtils.isNotBlank(md5Str) && md5Str.equals(md5Sign(data));
    }

    /**
     * 生成sha字符串
     *
     * @param data 原数据
     * @return String
     */
    public static String shaSign(String data) {
        return getString(data, SHA_DIGEST);
    }

    /**
     * sha验证
     *
     * @param data   原数据
     * @param shaStr sha字符串
     * @return boolean
     */
    public static boolean shaVerify(String data, String shaStr) {
        return StringUtils.isNotBlank(shaStr) && shaStr.equals(shaSign(data));
    }

    /**
     * 生成sha256字符串
     *
     * @param data 原数据
     * @return String
     */
    public static String sha256Sign(String data) {
        return getString(data, SHA_256_DIGEST);
    }

    /**
     * sha256验证
     *
     * @param data      原数据
     * @param sha256Str sha256字符串
     * @return boolean
     */
    public static boolean sha256Verify(String data, String sha256Str) {
        return StringUtils.isNotBlank(sha256Str) && sha256Str.equals(sha256Sign(data));
    }

    /**
     * 生成sha512字符串
     *
     * @param data 原数据
     * @return String
     */
    public static String sha512Sign(String data) {
        return getString(data, SHA_512_DIGEST);
    }

    /**
     * sha512验证
     *
     * @param data      原数据
     * @param sha512Str sha512字符串
     * @return boolean
     */
    public static boolean sha512Verify(String data, String sha512Str) {
        return StringUtils.isNotBlank(sha512Str) && sha512Str.equals(sha512Sign(data));
    }

    private static String getString(String data, String digest) {
        try {
            String d = data + SALT;
            MessageDigest sha = MessageDigest.getInstance(digest);
            sha.update(d.getBytes(StandardCharsets.UTF_8));
            byte[] s = sha.digest();
            return byteToHex(s);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 字节数组转16进制字符串
     *
     * @param bytes 字节数组
     * @return String
     */
    private static String byteToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte value : bytes) {
            result.append(Integer.toHexString((0x000000FF & value) | 0xFFFFFF00).substring(6));
        }
        return result.toString().toUpperCase();
    }

    public static void main(String[] args) {
        // TODO: 2020/9/25 md5
        String data = "123456";
        String s = md5Sign(data);
        System.out.println(s);
        System.out.println(md5Verify(data, s));
        // TODO: 2020/9/25 sha
        String s1 = shaSign(data);
        System.out.println(s1);
        System.out.println(shaVerify(data, s1));
        // TODO: 2020/9/25 sha256
        String s3 = sha256Sign(data);
        System.out.println(s3);
        System.out.println(sha256Verify(data, s3));
        // TODO: 2020/9/25 sha512
        String s4 = sha512Sign(data);
        System.out.println(s4);
        System.out.println(sha512Verify(data, s4));

    }


}
