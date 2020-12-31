package com.example.guaranty.common.utils.security;

import cn.hutool.core.codec.Base64;
import com.alibaba.fastjson.JSONObject;
import org.junit.Test;

import java.io.File;
import java.net.URL;

public class RsaUtilsTest {

    @Test
    void genKeyPair() throws Exception {
        RsaUtils.RsaKeyPair rsaKeyPair = RsaUtils.genKeyPair();
        System.out.println("私钥：" + rsaKeyPair.getPrivateKey());
        System.out.println("公钥：" + rsaKeyPair.getPublicKey());
    }

    @Test
    void createKeys() {
        RsaUtils.RsaKeyPair keyPair = RsaUtils.createRsaKeys();
        System.out.println("私钥：" + keyPair.getPrivateKey());
        System.out.println("公钥：" + keyPair.getPublicKey());
    }

    @Test
    void RsaDecodeTest() {
        String privateKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAOHXgvnI+gJAqeomAarJQFTM9Tl5bRDevyPZwvS/nZKQ8ZgtFRNhn/L0SDanaGiAdP3uaacX/srPEKTOFjSYxis7gyanPmLDC7GVVWVxm7+QmkY9g27g8Yv+ROIcP8jM2XXXLnjNCGGE0wO6CmbP30tMMXT1wKXs/+xDtJ3nWncnAgMBAAECgYAFjhN+qV7c51JGnCC2XrTiYRg3DbGNFK2G6DhPZNcvX7lRkOpPy7rvcurDzWJYD7DQ/ihDH+f4fRdftmH9cTSOl9K0JvK3I/SdIUwIXwpZcen+YV8ZHvg5kJRo3q2uSAugfX5gONk0lukqq95LYOMUXDudMD0UP8+OmaytMhpOoQJBAP8Yer2LbDQRP946pqqqBrKDaGYdWH0sAHhAthxjrYzfIIo/CTpQBJ/0CE/4Cu2FOxQCRI/HLBYVhw1GgthZD7MCQQDipHtfZcSWAUJng+3uiDIn3aYKnjpFEQ2eehhzs+8k/rHd8vmK3uJGax4wHKRuWTrjs7pElQf9shyZpEe4DqC9AkBye6X6EuxIocUsEt5hkVTYIKllyp2/71N2pHQWYZWttyV/ZbLafLbWokpouUUOO9C7thjW/egHMToe9xoZxj6JAkA8iVSOwZ8FtiZngdyupuBGt7RfB65mvkxV9STM2tXYmtMlhn3S8v+bcYcpsKzW8KyDH4F0Sh5NTCLJgxDI/9c9AkEA8BLZ9sjXD4ikY9lT9C2AKOGK3GTVQdM5S9Ob9d/Mna64NY+6/NngDTNdmZ0UDkgnHaWSjHXg1jpe8lAOo+yR+g==";

        String encryptStr = "";
        // 解密密文
        String decodeStr = RsaUtils.rsaDecrypt(encryptStr, privateKey);
        System.out.println(decodeStr);
    }

    @Test
    void RsaEncodeTest() {
        String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDh14L5yPoCQKnqJgGqyUBUzPU5eW0Q3r8j2cL0v52SkPGYLRUTYZ/y9Eg2p2hogHT97mmnF/7KzxCkzhY0mMYrO4Mmpz5iwwuxlVVlcZu/kJpGPYNu4PGL/kTiHD/IzNl11y54zQhhhNMDugpmz99LTDF09cCl7P/sQ7Sd51p3JwIDAQAB";
        String privateKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAOHXgvnI+gJAqeomAarJQFTM9Tl5bRDevyPZwvS/nZKQ8ZgtFRNhn/L0SDanaGiAdP3uaacX/srPEKTOFjSYxis7gyanPmLDC7GVVWVxm7+QmkY9g27g8Yv+ROIcP8jM2XXXLnjNCGGE0wO6CmbP30tMMXT1wKXs/+xDtJ3nWncnAgMBAAECgYAFjhN+qV7c51JGnCC2XrTiYRg3DbGNFK2G6DhPZNcvX7lRkOpPy7rvcurDzWJYD7DQ/ihDH+f4fRdftmH9cTSOl9K0JvK3I/SdIUwIXwpZcen+YV8ZHvg5kJRo3q2uSAugfX5gONk0lukqq95LYOMUXDudMD0UP8+OmaytMhpOoQJBAP8Yer2LbDQRP946pqqqBrKDaGYdWH0sAHhAthxjrYzfIIo/CTpQBJ/0CE/4Cu2FOxQCRI/HLBYVhw1GgthZD7MCQQDipHtfZcSWAUJng+3uiDIn3aYKnjpFEQ2eehhzs+8k/rHd8vmK3uJGax4wHKRuWTrjs7pElQf9shyZpEe4DqC9AkBye6X6EuxIocUsEt5hkVTYIKllyp2/71N2pHQWYZWttyV/ZbLafLbWokpouUUOO9C7thjW/egHMToe9xoZxj6JAkA8iVSOwZ8FtiZngdyupuBGt7RfB65mvkxV9STM2tXYmtMlhn3S8v+bcYcpsKzW8KyDH4F0Sh5NTCLJgxDI/9c9AkEA8BLZ9sjXD4ikY9lT9C2AKOGK3GTVQdM5S9Ob9d/Mna64NY+6/NngDTNdmZ0UDkgnHaWSjHXg1jpe8lAOo+yR+g==";

        JSONObject data = new JSONObject();
        data.put("tel", "18692345390");
        data.put("a", 123);
        data.put("b", 321);

        // 加密之后的内容
        String encryptStr = RsaUtils.rsaEncrypt(data.toJSONString(), publicKey);
        System.out.println(encryptStr);

        // 解密密文
        String decodeStr = RsaUtils.rsaDecrypt(encryptStr, privateKey);
        System.out.println(decodeStr);
    }

    @Test
    void integrationTest() throws Exception {
        URL url = RsaUtils.class.getClassLoader().getResource("other-config.yml");
        if (null != url) {
            String path = url.getPath();
            System.out.println(path);
            File file = new File(path);
            System.out.println(file.getAbsolutePath());
        }

        String filepath = "E:/tmp/";

        //生成公钥和私钥文件
        RsaUtils.genKeyPair(filepath);

        System.out.println("--------------公钥加密私钥解密过程-------------------");
        String plainText = "公钥加密私钥解密";
        //公钥加密过程
        byte[] cipherData = RsaUtils.encrypt(RsaUtils.loadPublicKeyByStr(RsaUtils.loadPublicKeyByFile(filepath + RsaUtils.PUBLIC_KEY_FILE_NAME)), plainText.getBytes());
        String cipher = Base64.encode(cipherData);
        //私钥解密过程
        byte[] res = RsaUtils.decrypt(RsaUtils.loadPrivateKeyByStr(RsaUtils.loadPrivateKeyByFile(filepath + RsaUtils.PRIVATE_KEY_FILE_NAME)), Base64.decode(cipher));
        String reStr = new String(res);
        System.out.println("原文：" + plainText);
        System.out.println("加密：" + cipher);
        System.out.println("解密：" + reStr);
        System.out.println();

        System.out.println("--------------私钥加密公钥解密过程-------------------");
        plainText = "私钥加密公钥解密";
        //私钥加密过程
        cipherData = RsaUtils.encrypt(RsaUtils.loadPrivateKeyByStr(RsaUtils.loadPrivateKeyByFile(filepath + RsaUtils.PRIVATE_KEY_FILE_NAME)), plainText.getBytes());
        cipher = Base64.encode(cipherData);
        //公钥解密过程
        res = RsaUtils.decrypt(RsaUtils.loadPublicKeyByStr(RsaUtils.loadPublicKeyByFile(filepath + RsaUtils.PUBLIC_KEY_FILE_NAME)), Base64.decode(cipher));
        reStr = new String(res);
        System.out.println("原文：" + plainText);
        System.out.println("加密：" + cipher);
        System.out.println("解密：" + reStr);
        System.out.println();

        System.out.println("---------------sha1签名验签------------------");
        System.out.println("---------------私钥签名过程------------------");
        String content = "这是用于签名的原始数据";
        String signStr = RsaUtils.sign(content, RsaUtils.loadPrivateKeyByFile(filepath + RsaUtils.PRIVATE_KEY_FILE_NAME));
        System.out.println("签名原串：" + content);
        System.out.println("签名串：" + signStr);
        System.out.println();

        System.out.println("---------------公钥校验签名------------------");
        System.out.println("签名原串：" + content);
        System.out.println("签名串：" + signStr);

        System.out.println("验签结果：" + RsaUtils.doCheck(content, signStr, RsaUtils.loadPublicKeyByFile(filepath + RsaUtils.PUBLIC_KEY_FILE_NAME)));
        System.out.println();
    }

    @Test
    void rsaWithSha256() {
        String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDh14L5yPoCQKnqJgGqyUBUzPU5eW0Q3r8j2cL0v52SkPGYLRUTYZ/y9Eg2p2hogHT97mmnF/7KzxCkzhY0mMYrO4Mmpz5iwwuxlVVlcZu/kJpGPYNu4PGL/kTiHD/IzNl11y54zQhhhNMDugpmz99LTDF09cCl7P/sQ7Sd51p3JwIDAQAB";
        String privateKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAOHXgvnI+gJAqeomAarJQFTM9Tl5bRDevyPZwvS/nZKQ8ZgtFRNhn/L0SDanaGiAdP3uaacX/srPEKTOFjSYxis7gyanPmLDC7GVVWVxm7+QmkY9g27g8Yv+ROIcP8jM2XXXLnjNCGGE0wO6CmbP30tMMXT1wKXs/+xDtJ3nWncnAgMBAAECgYAFjhN+qV7c51JGnCC2XrTiYRg3DbGNFK2G6DhPZNcvX7lRkOpPy7rvcurDzWJYD7DQ/ihDH+f4fRdftmH9cTSOl9K0JvK3I/SdIUwIXwpZcen+YV8ZHvg5kJRo3q2uSAugfX5gONk0lukqq95LYOMUXDudMD0UP8+OmaytMhpOoQJBAP8Yer2LbDQRP946pqqqBrKDaGYdWH0sAHhAthxjrYzfIIo/CTpQBJ/0CE/4Cu2FOxQCRI/HLBYVhw1GgthZD7MCQQDipHtfZcSWAUJng+3uiDIn3aYKnjpFEQ2eehhzs+8k/rHd8vmK3uJGax4wHKRuWTrjs7pElQf9shyZpEe4DqC9AkBye6X6EuxIocUsEt5hkVTYIKllyp2/71N2pHQWYZWttyV/ZbLafLbWokpouUUOO9C7thjW/egHMToe9xoZxj6JAkA8iVSOwZ8FtiZngdyupuBGt7RfB65mvkxV9STM2tXYmtMlhn3S8v+bcYcpsKzW8KyDH4F0Sh5NTCLJgxDI/9c9AkEA8BLZ9sjXD4ikY9lT9C2AKOGK3GTVQdM5S9Ob9d/Mna64NY+6/NngDTNdmZ0UDkgnHaWSjHXg1jpe8lAOo+yR+g==";

        JSONObject data = new JSONObject();
        data.put("tel", "18692345390");
        data.put("a", 123);
        data.put("b", 321);

        String sign = RsaUtils.signatureByRsa(data.toJSONString(), privateKey);
        Boolean flag = RsaUtils.verifyByRsa(sign, data.toJSONString(), publicKey);

        System.out.println("验签结果：" + flag);
    }

    @Test
    void loadPublicKeyByFile() {

    }

    @Test
    void loadPublicKeyByStr() {

    }

    @Test
    void loadPrivateKeyByFile() {

    }

    @Test
    void loadPrivateKeyByStr() {

    }

    @Test
    void encrypt() {
    }

    @Test
    void encrypt1() {
    }

    @Test
    void decrypt() {
    }

    @Test
    void decrypt1() {
    }

    @Test
    void byteArrayToString() {
    }

    @Test
    void sign() {
    }

    @Test
    void sign1() {
    }

    @Test
    void doCheck() {
    }

    @Test
    void doCheck1() {
    }
}