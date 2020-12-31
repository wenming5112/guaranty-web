package com.example.guaranty.common.utils.security;

import cn.hutool.core.codec.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.LinkedList;
import java.util.List;

/**
 * rsa
 *
 * @author ming
 * @version 1.0.0
 * @date 2020/9/16 17:07
 **/
public class RsaUtils {

    /**
     * 字节数据转字符串专用集合
     */
    private static final char[] HEX_CHAR = {'0', '1', '2', '3', '4', '5', '6',
            '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    /**
     * 签名算法
     */
    private static final String SIGN_ALGORITHMS = "SHA1WithRSA";
    /**
     * Encryption algorithm name
     */
    private static final String ENCRYPTION_ALGORITHM_NAME_RSA = "RSA";

    public static final String PRIVATE_KEY_FILE_NAME = "privateKey.keystore";
    public static final String PUBLIC_KEY_FILE_NAME = "publicKey.keystore";

    static class RsaKeyPair {
        private String publicKey;
        private String privateKey;

        RsaKeyPair(String publicKey, String privateKey) {
            this.publicKey = publicKey;
            this.privateKey = privateKey;
        }

        public String getPublicKey() {
            return publicKey;
        }

        public String getPrivateKey() {
            return privateKey;
        }

        @Override
        public String toString() {
            return "RsaKeyPair{" +
                    "publicKey='" + publicKey + '\'' +
                    ", privateKey='" + privateKey + '\'' +
                    '}';
        }
    }

    public static RsaKeyPair genKeyPair() throws Exception {
        try {
            // KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象
            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(ENCRYPTION_ALGORITHM_NAME_RSA);
            // 初始化密钥对生成器，密钥大小为96-1024位
            keyPairGen.initialize(1024, new SecureRandom());
            // 生成一个密钥对，保存在keyPair中
            KeyPair keyPair = keyPairGen.generateKeyPair();
            // 得到私钥
            RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
            // 得到公钥
            RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
            // 得到公钥字符串
            String publicKeyString = Base64.encode(publicKey.getEncoded());
            // 得到私钥字符串
            String privateKeyString = Base64.encode(privateKey.getEncoded());
            return new RsaKeyPair(publicKeyString, privateKeyString);
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此算法");
        }
    }


    /**
     * 随机生成密钥对
     */
    public static void genKeyPair(String path) throws Exception {
        try {
            // KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象
            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
            // 初始化密钥对生成器，密钥大小为96-1024位
            keyPairGen.initialize(1024, new SecureRandom());
            // 生成一个密钥对，保存在keyPair中
            KeyPair keyPair = keyPairGen.generateKeyPair();
            // 得到私钥
            RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
            // 得到公钥
            RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
            try (FileWriter fileWriterPub = new FileWriter(path + File.separator + PUBLIC_KEY_FILE_NAME);
                 FileWriter fileWriterPri = new FileWriter(path + File.separator + PRIVATE_KEY_FILE_NAME);
                 BufferedWriter bufferedWriterPub = new BufferedWriter(fileWriterPub);
                 BufferedWriter bufferedWriterPri = new BufferedWriter(fileWriterPri)
            ) {
                // 得到公钥字符串
                String publicKeyString = Base64.encode(publicKey.getEncoded());
                // 得到私钥字符串
                String privateKeyString = Base64.encode(privateKey.getEncoded());
                // 将密钥对写入到文件
                bufferedWriterPub.write(publicKeyString);
                bufferedWriterPri.write(privateKeyString);
                bufferedWriterPub.flush();
                bufferedWriterPri.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此算法");
        }
    }

    /**
     * 从文件中输入流中加载公钥
     *
     * @param path 公钥路径
     * @throws Exception 加载公钥时产生的异常
     */
    public static String loadPublicKeyByFile(String path) throws Exception {
        try {
            return getString(path);
        } catch (IOException e) {
            throw new Exception("公钥数据流读取错误");
        } catch (NullPointerException e) {
            throw new Exception("公钥输入流为空");
        }
    }

    /**
     * 从字符串中加载公钥
     *
     * @param publicKeyStr 公钥数据字符串
     * @throws Exception 加载公钥时产生的异常
     */
    public static RSAPublicKey loadPublicKeyByStr(String publicKeyStr)
            throws Exception {
        try {
            byte[] buffer = Base64.decode(publicKeyStr);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
            return (RSAPublicKey) keyFactory.generatePublic(keySpec);
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此算法");
        } catch (InvalidKeySpecException e) {
            throw new Exception("公钥非法");
        } catch (NullPointerException e) {
            throw new Exception("公钥数据为空");
        }
    }

    /**
     * 从文件中加载私钥
     *
     * @param path 私钥路径
     * @return 是否成功
     * @throws Exception e
     */
    public static String loadPrivateKeyByFile(String path) throws Exception {
        try {
            return getString(path);
        } catch (IOException e) {
            throw new Exception("私钥数据读取错误");
        } catch (NullPointerException e) {
            throw new Exception("私钥输入流为空");
        }
    }

    private static String getString(String path) throws IOException {
        String readLine;
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            while ((readLine = br.readLine()) != null) {
                sb.append(readLine);
            }
            br.close();
            return sb.toString();
        } catch (IOException e) {
            throw new IOException(e);
        }
    }

    public static RSAPrivateKey loadPrivateKeyByStr(String privateKeyStr) throws Exception {
        try {
            // 默认utf-8
            byte[] buffer = Base64.decode(privateKeyStr);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(buffer);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此算法");
        } catch (InvalidKeySpecException e) {
            throw new Exception("私钥非法");
        } catch (NullPointerException e) {
            throw new Exception("私钥数据为空");
        }
    }

    /**
     * 私钥解密 RSA
     *
     * @param sourceBase64Str     源消息
     * @param privateKeyBase64Str 私钥
     */
    public static String rsaDecrypt(String sourceBase64Str, String privateKeyBase64Str) {
        byte[] privateBytes = org.apache.commons.codec.binary.Base64.decodeBase64(privateKeyBase64Str);
        byte[] encodeSource = org.apache.commons.codec.binary.Base64.decodeBase64(sourceBase64Str);
        int encodePartLen = encodeSource.length / ConstantForSecurity.RSA_ENCODE_PART_SIZE;
        // 所有解密数据
        List<byte[]> decodeListData = new LinkedList<>();
        String decodeStrResult;
        // 私钥解密
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(privateBytes);
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(ConstantForSecurity.RSA_KEY_TYPE);
            PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
            Cipher cipher = Cipher.getInstance(ConstantForSecurity.RSA_KEY_TYPE);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            // 初始化所有被解密数据长度
            int allDecodeByteLen = 0;
            for (int i = 0; i < encodePartLen; i++) {
                byte[] tempEncodedData = new byte[ConstantForSecurity.RSA_ENCODE_PART_SIZE];
                System.arraycopy(encodeSource, i * ConstantForSecurity.RSA_ENCODE_PART_SIZE, tempEncodedData, 0, ConstantForSecurity.RSA_ENCODE_PART_SIZE);
                byte[] decodePartData = cipher.doFinal(tempEncodedData);
                decodeListData.add(decodePartData);
                allDecodeByteLen += decodePartData.length;
            }
            byte[] decodeResultBytes = new byte[allDecodeByteLen];
            for (int i = 0, curPosition = 0; i < encodePartLen; i++) {
                byte[] tempSorceBytes = decodeListData.get(i);
                int tempSourceBytesLen = tempSorceBytes.length;
                System.arraycopy(tempSorceBytes, 0, decodeResultBytes, curPosition, tempSourceBytesLen);
                curPosition += tempSourceBytesLen;
            }
            decodeStrResult = new String(decodeResultBytes, "UTF-8");
            return decodeStrResult;
        } catch (NoSuchAlgorithmException | InvalidKeyException | NoSuchPaddingException | BadPaddingException | UnsupportedEncodingException | InvalidKeySpecException | IllegalBlockSizeException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * RSA 签名
     *
     * @param resource            加密数据
     * @param privateKeyBase64Str 私钥
     * @return Base64Str
     */
    public static String signatureByRsa(String resource, String privateKeyBase64Str) {
        try {
            byte[] privateBytes = Base64.decode(privateKeyBase64Str);
            PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(privateBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(ConstantForSecurity.RSA_KEY_TYPE);
            PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
            // 签名 SHA256withRSA
            Signature sign = Signature.getInstance("SHA256withRSA");
            sign.initSign(privateKey);
            byte[] bysData = resource.getBytes("UTF-8");
            sign.update(bysData);
            byte[] signByte = sign.sign();
            return Base64.encode(signByte);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("签名验证失败");
        }
    }

    /**
     * 验证签名
     *
     * @param signStr            签名后的数据
     * @param verStr             原始数据
     * @param publicKeyBase64Str 公钥
     * @return Boolean
     */
    public static Boolean verifyByRsa(String signStr, String verStr, String publicKeyBase64Str) {
        try {
            byte[] publicBytes = Base64.decode(publicKeyBase64Str);
            //公钥加密
            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(publicBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(ConstantForSecurity.RSA_KEY_TYPE);
            PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
            byte[] signed = Base64.decode(signStr);
            Signature sign = Signature.getInstance("SHA256withRSA");
            sign.initVerify(publicKey);
            sign.update(verStr.getBytes("UTF-8"));
            return sign.verify(signed);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("签名验证失败");
        }
    }

    /**
     * 创建公钥秘钥
     *
     * @return map
     */
    public static RsaKeyPair createRsaKeys() {
        //里面存放公私秘钥的Base64位加密
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ConstantForSecurity.RSA_KEY_TYPE);
            keyPairGenerator.initialize(ConstantForSecurity.RSA_KEY_SIZE, new SecureRandom());
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            //获取公钥秘钥
            String publicKeyValue = org.apache.commons.codec.binary.Base64.encodeBase64String(keyPair.getPublic().getEncoded());
            String privateKeyValue = org.apache.commons.codec.binary.Base64.encodeBase64String(keyPair.getPrivate().getEncoded());
            return new RsaKeyPair(publicKeyValue, privateKeyValue);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * 公钥加密过程
     *
     * @param publicKey     公钥
     * @param plainTextData 明文数据
     * @return byte
     * @throws Exception 加密过程中的异常信息
     */
    public static byte[] encrypt(RSAPublicKey publicKey, byte[] plainTextData) throws Exception {
        if (publicKey == null) {
            throw new Exception("加密公钥为空, 请设置");
        }
        Cipher cipher;
        try {
            // 使用默认RSA
            cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            return cipher.doFinal(plainTextData);
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此加密算法");
        } catch (NoSuchPaddingException e) {
            throw new Exception("NoSuchPaddingException");
        } catch (InvalidKeyException e) {
            throw new Exception("加密公钥非法,请检查");
        } catch (IllegalBlockSizeException e) {
            throw new Exception("明文长度非法");
        } catch (BadPaddingException e) {
            throw new Exception("明文数据已损坏");
        }
    }

    /**
     * 私钥加密过程
     *
     * @param privateKey    私钥
     * @param plainTextData 明文数据
     * @return byte
     * @throws Exception 加密过程中的异常信息
     */
    public static byte[] encrypt(RSAPrivateKey privateKey, byte[] plainTextData)
            throws Exception {
        if (privateKey == null) {
            throw new Exception("加密私钥为空, 请设置");
        }
        Cipher cipher;
        try {
            // 使用默认RSA
            cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);
            return cipher.doFinal(plainTextData);
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此加密算法");
        } catch (NoSuchPaddingException e) {
            throw new Exception("NoSuchPaddingException");
        } catch (InvalidKeyException e) {
            throw new Exception("加密私钥非法,请检查");
        } catch (IllegalBlockSizeException e) {
            throw new Exception("明文长度非法");
        } catch (BadPaddingException e) {
            throw new Exception("明文数据已损坏");
        }
    }

    /**
     * 私钥解密过程
     *
     * @param privateKey 私钥
     * @param cipherData 密文数据
     * @return 明文
     * @throws Exception 解密过程中的异常信息
     */
    public static byte[] decrypt(RSAPrivateKey privateKey, byte[] cipherData) throws Exception {
        if (privateKey == null) {
            throw new Exception("解密私钥为空, 请设置");
        }
        Cipher cipher;
        try {
            // 使用默认RSA
            cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            return cipher.doFinal(cipherData);
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此解密算法");
        } catch (NoSuchPaddingException e) {
            throw new Exception("NoSuchPaddingException");
        } catch (InvalidKeyException e) {
            throw new Exception("解密私钥非法,请检查");
        } catch (IllegalBlockSizeException e) {
            throw new Exception("密文长度非法");
        } catch (BadPaddingException e) {
            throw new Exception("密文数据已损坏");
        }
    }

    /**
     * 公钥解密过程
     *
     * @param publicKey  公钥
     * @param cipherData 密文数据
     * @return 明文
     * @throws Exception 解密过程中的异常信息
     */
    public static byte[] decrypt(RSAPublicKey publicKey, byte[] cipherData) throws Exception {
        if (publicKey == null) {
            throw new Exception("解密公钥为空, 请设置");
        }
        Cipher cipher;
        try {
            // 使用默认RSA
            cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, publicKey);
            return cipher.doFinal(cipherData);
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此解密算法");
        } catch (NoSuchPaddingException e) {
            throw new Exception("NoSuchPaddingException");
        } catch (InvalidKeyException e) {
            throw new Exception("解密公钥非法,请检查");
        } catch (IllegalBlockSizeException e) {
            throw new Exception("密文长度非法");
        } catch (BadPaddingException e) {
            throw new Exception("密文数据已损坏");
        }
    }

    /**
     * 字节数据转十六进制字符串
     *
     * @param data 输入数据
     * @return 十六进制内容
     */
    public static String byteArrayToString(byte[] data) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < data.length; i++) {
            // 取出字节的高四位 作为索引得到相应的十六进制标识符 注意无符号右移
            stringBuilder.append(HEX_CHAR[(data[i] & 0xf0) >>> 4]);
            // 取出字节的低四位 作为索引得到相应的十六进制标识符
            stringBuilder.append(HEX_CHAR[(data[i] & 0x0f)]);
            if (i < data.length - 1) {
                stringBuilder.append(' ');
            }
        }
        return stringBuilder.toString();
    }


    /**
     * RSA签名
     *
     * @param content    待签名数据
     * @param privateKey 商户私钥
     * @param encode     字符集编码
     * @return 签名值
     */
    public static String sign(String content, String privateKey, String encode) {
        try {
            PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(Base64.decode(privateKey));

            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PrivateKey priKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);

            java.security.Signature signature = java.security.Signature.getInstance(SIGN_ALGORITHMS);

            signature.initSign(priKey);
            signature.update(content.getBytes(encode));

            byte[] signed = signature.sign();

            return Base64.encode(signed);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String sign(String content, String privateKey) {
        try {
            PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(Base64.decode(privateKey));
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PrivateKey priKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
            java.security.Signature signature = java.security.Signature.getInstance(SIGN_ALGORITHMS);
            signature.initSign(priKey);
            signature.update(content.getBytes());
            byte[] signed = signature.sign();
            return Base64.encode(signed);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * RSA验签名检查
     *
     * @param content   待签名数据
     * @param sign      签名值
     * @param publicKey 分配给开发商公钥
     * @param encode    字符集编码
     * @return 布尔值
     */
    public static boolean doCheck(String content, String sign, String publicKey, String encode) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            byte[] encodedKey = Base64.decode(publicKey);
            PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));


            java.security.Signature signature = java.security.Signature.getInstance(SIGN_ALGORITHMS);

            signature.initVerify(pubKey);
            signature.update(content.getBytes(encode));

            return signature.verify(Base64.decode(sign));

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public static boolean doCheck(String content, String sign, String publicKey) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            byte[] encodedKey = Base64.decode(publicKey);
            PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));

            java.security.Signature signature = java.security.Signature.getInstance(SIGN_ALGORITHMS);

            signature.initVerify(pubKey);
            signature.update(content.getBytes());

            return signature.verify(Base64.decode(sign));

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public static String rsaEncrypt(String sourceStr, String publicKeyBase64Str) {
        byte[] publicBytes = org.apache.commons.codec.binary.Base64.decodeBase64(publicKeyBase64Str);
        // 公钥加密
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(publicBytes);
        List<byte[]> alreadyEncodeListData = new LinkedList<>();

        int maxEncodeSize = ConstantForSecurity.RSA_ENCODE_PART_SIZE - 11;
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(ConstantForSecurity.RSA_KEY_TYPE);
            PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
            Cipher cipher = Cipher.getInstance(ConstantForSecurity.RSA_KEY_TYPE);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] sourceBytes = sourceStr.getBytes("utf-8");
            int sourceLen = sourceBytes.length;
            for (int i = 0; i < sourceLen; i += maxEncodeSize) {
                int curPosition = sourceLen - i;
                int tempLen = curPosition;
                if (curPosition > maxEncodeSize) {
                    tempLen = maxEncodeSize;
                }
                // 待加密分段数据
                byte[] tempBytes = new byte[tempLen];
                System.arraycopy(sourceBytes, i, tempBytes, 0, tempLen);
                byte[] tempAlreadyEncodeData = cipher.doFinal(tempBytes);
                alreadyEncodeListData.add(tempAlreadyEncodeData);
            }
            // 加密次数
            int partLen = alreadyEncodeListData.size();

            int allEncodeLen = partLen * ConstantForSecurity.RSA_ENCODE_PART_SIZE;
            //存放所有RSA分段加密数据
            byte[] encodeData = new byte[allEncodeLen];
            for (int i = 0; i < partLen; i++) {
                byte[] tempByteList = alreadyEncodeListData.get(i);
                System.arraycopy(tempByteList, 0, encodeData, i * ConstantForSecurity.RSA_ENCODE_PART_SIZE, ConstantForSecurity.RSA_ENCODE_PART_SIZE);
            }
            return org.apache.commons.codec.binary.Base64.encodeBase64String(encodeData);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

}
