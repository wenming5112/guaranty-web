package com.example.guaranty.service.business;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.guaranty.common.exception.BusinessException;
import com.example.guaranty.common.handle.ApiResult;
import com.example.guaranty.dto.business.LoanProductDTO;
import com.example.guaranty.entity.business.LoanProduct;
import io.swagger.models.auth.In;

/**
 * 贷款产品表 Service
 *
 * @author m
 * @version 1.0.0
 * @date 2020-11-29 12:11:50
 **/
public interface LoanProductService extends IService<LoanProduct> {

    /**
     * 新增贷款产品
     *
     * @param loanProductDTO 贷款产品
     * @return res
     * @throws BusinessException e
     */
    ApiResult insertLoanProduct(LoanProductDTO loanProductDTO) throws BusinessException;

    /**
     * 修改银行产品
     *
     * @param productId      productId
     * @param loanProductDTO product
     * @return res
     * @throws BusinessException e
     */
    ApiResult updateLoanProduct(Integer productId, LoanProductDTO loanProductDTO) throws BusinessException;

    /**
     * 查询贷款产品（银行业务员用）
     *
     * @param current 当前页码
     * @param size    页大小
     * @return res
     * @throws BusinessException e
     */
    ApiResult getLoanProduct(Long current, Long size) throws BusinessException;

    /**
     * 查询所有贷款产品
     *
     * @param current 当前页码
     * @param size    页大小
     * @return res
     * @throws BusinessException e
     */
    ApiResult getAllLoanProduct(Long current, Long size) throws BusinessException;


}
