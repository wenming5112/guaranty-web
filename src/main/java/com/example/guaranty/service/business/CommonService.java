package com.example.guaranty.service.business;

import com.example.guaranty.common.exception.BusinessException;
import com.example.guaranty.common.handle.ApiResult;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * @author ming
 * @version 1.0.0
 * @date 2019 19:35
 */
public interface CommonService {


    /**
     * 文件上传(local)
     *
     * @param file 文件
     * @return json
     * @throws BusinessException e
     */
    ApiResult uploadFile(MultipartFile file) throws BusinessException;

    /**
     * 获取邮箱验证码
     *
     * @param email 邮箱地址
     * @return res
     * @throws BusinessException e
     */
    ApiResult emailVerifyCode(String email) throws BusinessException;

    /**
     * 用邮箱发送密码
     *
     * @param email    邮件
     * @param password 密码
     * @return res
     * @throws BusinessException e
     */
    Boolean sendEmailSecret(String email, String password) throws BusinessException;

    /**
     * 邮箱验证
     *
     * @param email 邮箱地址
     * @param code  验证码
     * @return res
     * @throws BusinessException e
     */
    Boolean emailVerify(String email, String code) throws BusinessException;

    void deleteFile(File... files);

//    /**
//     * 文件上传(oss)
//     *
//     * @param file 文件
//     * @return json
//     * @throws BusinessException e
//     */
//    ApiResult ossUploadFile(MultipartFile file) throws BusinessException;
//
//    /**
//     * 文件删除(oss)
//     *
//     * @param fileName 文件名
//     * @return json
//     * @throws BusinessException e
//     */
//    ApiResult ossDeleteFile(String fileName) throws BusinessException;

}
