package com.example.guaranty.common.utils;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.example.guaranty.common.exception.BusinessException;
import org.springframework.util.StringUtils;


import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;

/**
 * @author ming
 * @version 1.0.0
 * @date 2020/11/30 23:50
 **/
public class FileHashUtils {

    private static final String SHA_256_DIGEST = "SHA-256";
    private static final String MD5 = "MD5";

    public static void main(String[] args) throws Exception {
        //e10adc3949ba59abbe56e057f20f883e hello.txt
        //e10adc3949ba59abbe56e057f20f883e hello1.txt 修改文件名不会使hash改变
        //fcea920f7412b5da7be0cf42b8c93759
        System.out.println(getMd5ByFile("F:\\wzm_work\\thyc\\sql\\hello1.txt"));
        System.out.println(getMd5ByStream(new FileInputStream("F:\\wzm_work\\thyc\\sql\\hello1.txt")));
    }

    public static String getMd5ByFile(String path) throws Exception {
        return getMd5Checksum(path, null, MD5);
    }

    public static String getMd5ByStream(InputStream fis) throws Exception {
        return getMd5Checksum(null, fis, MD5);
    }

    public static String getSha256ByFile(String path) throws Exception {
        return getMd5Checksum(path, null, MD5);
    }

    public static String getSha256ByStream(InputStream fis) throws Exception {
        return getMd5Checksum(null, fis, MD5);
    }

    private static byte[] createChecksum(String path, InputStream inputStream, String digest) throws Exception {
        InputStream stream = inputStream;
        MessageDigest complete = MessageDigest.getInstance(digest);
        if (!StringUtils.isEmpty(path)) {
            stream = new FileInputStream(path);
        }
        byte[] buffer = new byte[1024];
        int numRead;
        try {
            do {
                numRead = stream.read(buffer);
                if (numRead > 0) {
                    complete.update(buffer, 0, numRead);
                }
            } while (numRead != -1);
        } catch (Exception e) {
            throw new BusinessException("生成文件Hash失败");
        } finally {
            if (ObjectUtils.isNotEmpty(stream)) {
                stream.close();
            }
        }
        return complete.digest();
    }

    private static String getMd5Checksum(String filename, InputStream fis, String digest) throws Exception {
        byte[] b = createChecksum(filename, fis, digest);
        StringBuilder result = new StringBuilder();

        for (byte aB : b) {
            result.append(Integer.toString((aB & 0xff) + 0x100, 16).substring(1));
        }
        return result.toString();
    }
}
