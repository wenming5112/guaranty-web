package com.example.guaranty.service.business;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.guaranty.common.exception.BusinessException;
import com.example.guaranty.common.handle.ApiResult;
import com.example.guaranty.dto.business.MortgageMaterialDTO;
import com.example.guaranty.entity.business.UserMortgageMaterial;
import org.springframework.web.multipart.MultipartFile;

/**
 * 用户抵押资料 Service
 *
 * @author m
 * @version 1.0.0
 * @date 2020-11-30 17:48:20
 **/
public interface UserMortgageMaterialService extends IService<UserMortgageMaterial> {

    /**
     * 上传抵押资料
     *
     * @param file 资料文件
     * @return res
     * @throws BusinessException e
     */
    ApiResult upload(MultipartFile file) throws BusinessException;

    /**
     * 添加抵押资料
     *
     * @param mortgageMaterialDTO 抵押资料
     * @return res
     * @throws BusinessException e
     */
    ApiResult addMortgageMaterial(MortgageMaterialDTO mortgageMaterialDTO) throws Exception;

    /**
     * 查询抵押资料
     *
     * @return res
     * @throws BusinessException e
     */
    ApiResult getMortgageMaterial() throws BusinessException;

    //ApiResult checkMortgageMaterialForBank() throws BusinessException;
}
