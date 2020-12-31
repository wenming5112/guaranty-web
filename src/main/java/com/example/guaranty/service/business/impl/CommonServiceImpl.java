package com.example.guaranty.service.business.impl;

import cn.hutool.core.date.DatePattern;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.guaranty.common.constant.WebConstant;
import com.example.guaranty.common.exception.BusinessException;
import com.example.guaranty.common.handle.ApiResult;
import com.example.guaranty.common.utils.CommonUtil;
import com.example.guaranty.common.utils.FileHashUtils;
import com.example.guaranty.config.EmailVerifyProperties;
import com.example.guaranty.config.redis.RedisCache;
import com.example.guaranty.config.upload.FileUploadProperties;
import com.example.guaranty.dao.business.FileCacheMapper;
import com.example.guaranty.entity.business.FileCache;
import com.example.guaranty.service.business.CommonService;
import com.example.guaranty.shiro.JwtUtil;
import com.example.guaranty.vo.business.MaterialUploadVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * 公共模块 实现类
 *
 * @author ming
 * @version 1.0.0
 * @date 2020/04/18
 */

@Service
@Slf4j
public class CommonServiceImpl implements CommonService {

    @Resource
    private FileCacheMapper fileCacheMapper;

    @Resource
    private FileUploadProperties fileUploadProperties;

    @Resource
    private PlatformTransactionManager platformTransactionManager;

    @Resource
    private EmailVerifyProperties emailVerifyProperties;

    @Resource
    private JavaMailSenderImpl mailSender;

    @Resource
    private RedisCache redisCache;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult uploadFile(MultipartFile file) throws BusinessException {
        if (ObjectUtils.isEmpty(file)) {
            throw new BusinessException("不能上传空文件");
        }
        MaterialUploadVO materialUploadVO = new MaterialUploadVO();
        File newFile;
        try {
            String fileHash = FileHashUtils.getSha256ByStream(file.getInputStream());
            String path = fileUploadProperties.getPath();
            File folderPath = new File(path);
            if (!folderPath.exists() && !folderPath.isDirectory()) {
                if (folderPath.mkdirs()) {
                    log.warn(String.format("Path %s is not exists, it will create new directory!!", path));
                }
            }
            //获取文件名，带后缀
            String fileName = file.getOriginalFilename();
            String fileSuffix = "";
            if (Objects.requireNonNull(fileName).contains(WebConstant.FILE_POINT)) {
                fileSuffix = fileName.substring(fileName.lastIndexOf("."));
            }
            fileName = fileHash;
            newFile = new File(path + fileName + fileSuffix);
            String url = fileUploadProperties.getBaseUrl() + fileName + fileSuffix;

            //将上传的文件写到服务器上指定的文件路径
            file.transferTo(newFile);

            FileCache fileCache = fileCacheMapper.selectOne(new QueryWrapper<FileCache>().lambda()
                    .eq(FileCache::getTitle, fileName)
                    .and(wrapper -> wrapper.eq(FileCache::getValid, Boolean.TRUE)));
            if (ObjectUtils.isEmpty(fileCache)) {
                fileCache = new FileCache();
                fileCache.setFileSize(file.getSize());
                fileCache.setTitle(fileName);
                fileCache.setTitleSuffix(fileSuffix);
                fileCache.setUrl(url);
                fileCache.setCreator(JwtUtil.getUserNameFromRedis());
                fileCache.setCreateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN)));
                if (fileCacheMapper.insert(fileCache) < 1) {
                    throw new BusinessException("文件上传失败");
                }
            }

            materialUploadVO.setMaterialUrl(url);
            materialUploadVO.setMaterialHash(fileHash);

        } catch (Exception e) {
            throw new BusinessException("文件上传失败");
        }
        return ApiResult.successOf(materialUploadVO);
    }

    /**
     * 邮箱验证
     *
     * @param email 邮箱地址
     * @return res
     * @throws BusinessException e
     */
    @Override
    public ApiResult emailVerifyCode(String email) throws BusinessException {
        String verifyCode = CommonUtil.getRandomNumCode(6);
        if (redisCache.hasKey(email)) {
            throw new BusinessException("请勿重复发送!!");
        }
        // 有效时间5分钟
        if (redisCache.set(email, verifyCode, emailVerifyProperties.getExpireTime())) {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setSubject(emailVerifyProperties.getSubject());
            message.setText(emailVerifyProperties.getTemplate().replace(emailVerifyProperties.getReplaceStr(), verifyCode));
            message.setFrom(emailVerifyProperties.getFrom());
            message.setTo(email);
            try {
                mailSender.send(message);
            } catch (Exception e) {
                redisCache.delete(email);
                return ApiResult.failOf("Send failed!!");
            }
            return ApiResult.successOf("Send success!!");
        }
        return ApiResult.failOf("Send failed!!");
    }

    /**
     * 用邮箱发送密码
     *
     * @param email    邮件
     * @param password 密码
     * @return res
     * @throws BusinessException e
     */
    @Override
    public Boolean sendEmailSecret(String email, String password) throws BusinessException {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject("初始密码");
        message.setText(String.format("您的初始密码为 %s ，首次登陆后，请及时修改!!", password));
        message.setFrom(emailVerifyProperties.getFrom());
        message.setTo(email);
        try {
            mailSender.send(message);
        } catch (Exception e) {
            redisCache.delete(email);
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    /**
     * 邮箱验证(内部验证使用)
     *
     * @param email 邮箱地址
     * @param code  验证码
     * @return res
     * @throws BusinessException e
     */
    @Override
    public Boolean emailVerify(String email, String code) throws BusinessException {
        if (!redisCache.hasKey(email)) {
            throw new BusinessException("Your email verification code is timeout!! Please send again!!");
        }
        String verifyCode = String.valueOf(redisCache.get(email));
        if (verifyCode.equals(code)) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    /**
     * 删除
     *
     * @param files f
     */
    @Override
    public void deleteFile(File... files) {
        for (File file : files) {
            if (file.exists()) {
                String path = file.getAbsolutePath();
                if (file.delete()) {
                    log.info(String.format("File: %s delete success!!", path));
                }
            }
        }
    }

//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public ApiResult ossUploadFile(MultipartFile file) throws BusinessException {
//        try {
//            // 处理文件
//            String fileName = file.getOriginalFilename();
//            Assert.isTrue(!ObjectUtils.isEmpty(fileName), "请选择要上传的文件");
//            String fileSuffix = "";
//            if (Objects.requireNonNull(fileName).contains(WebConstant.FILE_POINT)) {
//                fileSuffix = fileName.substring(fileName.lastIndexOf("."));
//            }
//            // 新文件名
//            String newFileName = UUID.randomUUID().toString().replaceAll("-", "") + fileSuffix;
//            // 阿里云ossSDK客户端
//            OSSClient ossClient = new OSSClient(WebConstant.END_POINT, webConstant.getAccessKey(), webConstant.getAccessSecret());
//            InputStream is = file.getInputStream();
//            // 把文件流上传
//            ossClient.putObject(WebConstant.OSS_BUCKET_NAME, newFileName, is);
//            ossClient.shutdown();
//            // 数据库存储上传记录
//            FileCache fileCache = new FileCache();
//            fileCache.setFileSize(file.getSize());
//            fileCache.setTitle(newFileName);
//            fileCache.setUrl(WebConstant.OSS_URL + "/" + newFileName);
//            fileCache.setTitleSuffix(fileSuffix);
//            fileCache.setCreateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN)));
//            fileCache.setCreator("临时测试用户");
//            if (fileCacheMapper.insert(fileCache) > 0) {
//                return ApiResult.successOf(HttpStatus.CREATED.value(), "文件上传成功", fileCache.getUrl());
//            }
//        } catch (Exception e) {
//            log.error("oss文件上传异常信息 ： " + e.getMessage());
//            throw new BusinessException(e.getMessage());
//        }
//        return ApiResult.failOf("文件上传失败");
//    }

//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public ApiResult ossDeleteFile(String fileName) throws BusinessException {
//        try {
//            // 阿里云ossSDK客户端
//            OSSClient ossClient = new OSSClient(WebConstant.END_POINT, webConstant.getAccessKey(), webConstant.getAccessSecret());
//            // 把文件流上传
//            ossClient.deleteObject(WebConstant.OSS_BUCKET_NAME, fileName);
//            // 数据库操作
//            return ApiResult.successOf("oss文件删除成功");
//        } catch (Exception e) {
//            log.error("oss文件删除异常信息 ： " + e.getMessage());
//            throw new BusinessException(e.getMessage());
//        }
//    }
}
