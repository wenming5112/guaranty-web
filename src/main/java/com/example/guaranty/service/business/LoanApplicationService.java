package com.example.guaranty.service.business;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.guaranty.common.exception.BusinessException;
import com.example.guaranty.common.handle.ApiResult;
import com.example.guaranty.dto.business.LoanApplicationDTO;
import com.example.guaranty.entity.business.LoanApplication;
import org.springframework.web.multipart.MultipartFile;

/**
 * 贷款申请表 Service
 *
 * @author m
 * @version 1.0.0
 * @date 2020-11-26 14:13:11
 **/
public interface LoanApplicationService extends IService<LoanApplication> {

    ApiResult createLoanApplication(LoanApplicationDTO loanApplicationDTO) throws BusinessException;

    ApiResult uploadMaterialFile(Integer loanApplicationId, MultipartFile file) throws BusinessException;

    ApiResult loanApplicationApproval(Integer loanApplicationId, Integer operationCode, String reason) throws Exception;

    ApiResult getLoanApplication(String username, String realName, String idNo, Integer status, Long current, Long size) throws BusinessException;

    ApiResult getLoanMyApplication(String bankName, Integer status, Long current, Long size) throws BusinessException;

    ApiResult getUserMortgageMaterial(Integer loanApplicationId) throws BusinessException;
}
