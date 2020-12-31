package com.example.guaranty.service.business.impl;

import cn.hutool.core.date.DatePattern;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.guaranty.common.constant.WebConstant;
import com.example.guaranty.common.exception.BusinessException;
import com.example.guaranty.common.handle.ApiResult;
import com.example.guaranty.common.utils.FileHashUtils;
import com.example.guaranty.component.FabricClientManager;
import com.example.guaranty.config.upload.FileUploadProperties;
import com.example.guaranty.dao.business.FileCacheMapper;
import com.example.guaranty.dao.business.UserMortgageMaterialMapper;
import com.example.guaranty.dto.business.MortgageMaterialDTO;
import com.example.guaranty.entity.business.BackstageUser;
import com.example.guaranty.entity.business.FileCache;
import com.example.guaranty.entity.business.UserMortgageMaterial;
import com.example.guaranty.service.business.BackstageUserService;
import com.example.guaranty.service.business.UserMortgageMaterialService;
import com.example.guaranty.shiro.JwtUtil;
import com.example.guaranty.vo.business.MortgageMaterialUploadVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Objects;


/**
 * 用户抵押资料 Impl
 *
 * @author m
 * @version 1.0.0
 * @date 2020-11-30 17:48:20
 **/
@Service
public class UserMortgageMaterialServiceImpl extends ServiceImpl<UserMortgageMaterialMapper, UserMortgageMaterial> implements UserMortgageMaterialService {

    @Resource
    private UserMortgageMaterialMapper userMortgageMaterialMapper;

    @Resource
    private FileUploadProperties fileUploadProperties;

    @Resource
    private FileCacheMapper fileCacheMapper;

    @Resource
    private FabricClientManager fabricClientManager;

    @Resource
    private BackstageUserService userService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult upload(MultipartFile file) throws BusinessException {
        MortgageMaterialUploadVO materialUploadVO = new MortgageMaterialUploadVO();
        try {
            if (ObjectUtils.isEmpty(file)) {
                throw new BusinessException("不能上传空文件");
            }
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
            File newFile = new File(path + fileName + fileSuffix);
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
                fileCache.setCreateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN)));
                if (fileCacheMapper.insert(fileCache) < 1) {
                    throw new BusinessException("文件上传失败");
                }
            }

            materialUploadVO.setMortgageMaterialUrl(url);
            materialUploadVO.setMortgageMaterialHash(fileHash);
            return ApiResult.successOf(materialUploadVO);
        } catch (Exception e) {
            throw new BusinessException("文件上传失败");
        }
    }

    @Override
    public ApiResult addMortgageMaterial(MortgageMaterialDTO mortgageMaterialDTO) throws Exception {
        Integer userId = JwtUtil.getUserFromRedis().getUserId();
        UserMortgageMaterial material = userMortgageMaterialMapper.selectOne(new QueryWrapper<UserMortgageMaterial>().lambda()
                .eq(UserMortgageMaterial::getUserId, userId)
                .and(wrapper -> wrapper.eq(UserMortgageMaterial::getValid, Boolean.TRUE)));
        UserMortgageMaterial mortgageMaterial;
        if (ObjectUtils.isEmpty(material)) {
            mortgageMaterial = new UserMortgageMaterial();
            mortgageMaterial.setUserId(userId);
            if (judgeMaterial("房产", mortgageMaterialDTO.getHouseMaterialHash())) {
                mortgageMaterial.setHouseMaterialUrl(mortgageMaterialDTO.getHouseMaterialUrl());
                mortgageMaterial.setHouseMaterialHash(mortgageMaterialDTO.getHouseMaterialHash());
            }
            if (judgeMaterial("产权", mortgageMaterialDTO.getPropertyMaterialHash())) {
                mortgageMaterial.setPropertyMaterialUrl(mortgageMaterialDTO.getPropertyMaterialUrl());
                mortgageMaterial.setPropertyMaterialHash(mortgageMaterialDTO.getPropertyMaterialHash());
            }
            if (judgeMaterial("车辆", mortgageMaterialDTO.getPropertyMaterialHash())) {
                mortgageMaterial.setCarMaterialUrl(mortgageMaterialDTO.getCarMaterialUrl());
                mortgageMaterial.setCarMaterialHash(mortgageMaterialDTO.getCarMaterialHash());
            }
            mortgageMaterial.setCreateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN)));
            mortgageMaterial.setCreator(JwtUtil.getUserNameFromRedis());
            if (userMortgageMaterialMapper.insert(mortgageMaterial) > 0) {
                // TODO: 2020/12/9 用户抵押资料上链
                BackstageUser user = userService.getById(userId);
                String func = "USER_CREATE_MORTGAGE_MATERIAL";
                String[] args = {user.getId().toString(), mortgageMaterial.getHouseMaterialHash(), mortgageMaterial.getHouseMaterialUrl(),
                        mortgageMaterial.getPropertyMaterialHash(), mortgageMaterial.getPropertyMaterialUrl(),
                        mortgageMaterial.getCarMaterialHash(), mortgageMaterial.getCarMaterialUrl()};
                Map<String, Object> map = fabricClientManager.getChainCodeManager().invoke(fabricClientManager.caEnroll(user.getUserName(), user.getPassword()), func, args);
                if (map.get("code").equals(1)) {
                    return ApiResult.successOf("添加成功");
                }
            }

        } else {
            mortgageMaterial = material;
            mortgageMaterial.setUserId(userId);
            if (judgeMaterial("房产", mortgageMaterialDTO.getHouseMaterialHash())) {
                mortgageMaterial.setHouseMaterialUrl(mortgageMaterialDTO.getHouseMaterialUrl());
                mortgageMaterial.setHouseMaterialHash(mortgageMaterialDTO.getHouseMaterialHash());
            }
            if (judgeMaterial("产权", mortgageMaterialDTO.getPropertyMaterialHash())) {
                mortgageMaterial.setPropertyMaterialUrl(mortgageMaterialDTO.getPropertyMaterialUrl());
                mortgageMaterial.setPropertyMaterialHash(mortgageMaterialDTO.getPropertyMaterialHash());
            }
            if (judgeMaterial("车辆", mortgageMaterialDTO.getPropertyMaterialHash())) {
                mortgageMaterial.setCarMaterialUrl(mortgageMaterialDTO.getCarMaterialUrl());
                mortgageMaterial.setCarMaterialHash(mortgageMaterialDTO.getCarMaterialHash());
            }
            if (userMortgageMaterialMapper.updateById(mortgageMaterial) > 0) {
                // TODO: 2020/12/9 用户抵押资料上链
                BackstageUser user = userService.getById(userId);
                String func = "USER_UPDATE_MORTGAGE_MATERIAL";
                String[] args = {mortgageMaterial.getHouseMaterialHash(), mortgageMaterial.getHouseMaterialUrl(),
                        mortgageMaterial.getPropertyMaterialHash(), mortgageMaterial.getPropertyMaterialUrl(),
                        mortgageMaterial.getCarMaterialHash(), mortgageMaterial.getCarMaterialUrl()};
                Map<String, Object> map = fabricClientManager.getChainCodeManager().invoke(fabricClientManager.caEnroll(user.getUserName(), user.getPassword()), func, args);
                if (map.get("code").equals(1)) {
                    return ApiResult.successOf("修改成功");
                }
            }
        }
        return ApiResult.successOf("添加失败");
    }

    /**
     * 查询抵押资料
     *
     * @return res
     * @throws BusinessException e
     */
    @Override
    public ApiResult getMortgageMaterial() throws BusinessException {
        Integer userId = JwtUtil.getUserFromRedis().getUserId();
        UserMortgageMaterial userMortgageMaterial = userMortgageMaterialMapper.selectOne(new QueryWrapper<UserMortgageMaterial>().lambda()
                .eq(UserMortgageMaterial::getUserId, userId)
                .and(wrapper -> wrapper.eq(UserMortgageMaterial::getValid, Boolean.TRUE)));

        return ApiResult.successOf(userMortgageMaterial);
    }

    private Boolean judgeMaterial(String flag, String hash) throws BusinessException {
        String basePath = fileUploadProperties.getPath();
        FileCache fileCache = fileCacheMapper.selectOne(new QueryWrapper<FileCache>().lambda()
                .eq(FileCache::getTitle, hash)
                .and(wrapper -> wrapper.eq(FileCache::getValid, Boolean.TRUE)));
        if (ObjectUtils.isEmpty(fileCache)) {
            throw new BusinessException(String.format("%s 资料未找到，请重新上传", flag));
        }
        String path = basePath + hash + fileCache.getTitleSuffix();
        if (!new File(path).exists()) {
            throw new BusinessException(String.format("%s 资料未找到，请重新上传", flag));
        }
        return Boolean.TRUE;
    }


}
