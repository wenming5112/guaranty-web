package com.example.guaranty.common.utils.security;


import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * base64
 *
 * @author ming
 * @version 1.0.0
 * @date 2020/9/27 10:22
 **/
public class Base64Utils {

    public static String encode(String data) {
        Base64.Encoder encoder = Base64.getEncoder();
        return encoder.encodeToString(data.getBytes(StandardCharsets.UTF_8));
    }

    public static String decode(String base64Str) {
        Base64.Decoder decoder = Base64.getDecoder();
        return new String(decoder.decode(base64Str.getBytes(StandardCharsets.UTF_8)));
    }

    public static void main(String[] args) {
        String base64Str = Base64Utils.encode("wzm");
        System.out.println(base64Str);
        System.out.println(Base64Utils.decode(base64Str));
    }
}
