package com.example.guaranty.service.business.impl;

import cn.hutool.core.date.DatePattern;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.guaranty.common.exception.BusinessException;
import com.example.guaranty.common.handle.ApiResult;
import com.example.guaranty.dao.business.LoanProductMapper;
import com.example.guaranty.dto.business.LoanProductDTO;
import com.example.guaranty.entity.business.LoanProduct;
import com.example.guaranty.service.business.BankService;
import com.example.guaranty.service.business.LoanProductService;
import com.example.guaranty.shiro.JwtUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


/**
 * 贷款产品表 Impl
 *
 * @author m
 * @version 1.0.0
 * @date 2020-11-29 12:11:50
 **/
@Service
public class LoanProductServiceImpl extends ServiceImpl<LoanProductMapper, LoanProduct> implements LoanProductService {

    @Resource
    private LoanProductMapper loanProductMapper;

    @Resource
    private BankService bankService;

    /**
     * 新增贷款产品
     *
     * @param loanProductDTO 贷款产品
     * @return res
     * @throws BusinessException e
     */
    @Override
    public ApiResult insertLoanProduct(LoanProductDTO loanProductDTO) throws BusinessException {
        Integer bankId = JwtUtil.getUserFromRedis().getBankId();
        if (ObjectUtils.isEmpty(bankId)) {
            throw new BusinessException("获取银行信息失败");
        }
        LoanProduct loanProduct = dto2Entity(loanProductDTO);
        loanProduct.setBankId(bankId);
        loanProduct.setCreateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN)));
        loanProduct.setCreator(JwtUtil.getUserNameFromRedis());
        if (loanProductMapper.insert(loanProduct) > 0) {
            return ApiResult.successOf("添加贷款产品成功");
        }

        return ApiResult.successOf("添加贷款产品失败");
    }

    /**
     * 修改银行产品
     *
     * @param productId      productId
     * @param loanProductDTO product
     * @return res
     * @throws BusinessException e
     */
    @Override
    public ApiResult updateLoanProduct(Integer productId, LoanProductDTO loanProductDTO) throws BusinessException {
        LoanProduct loanProduct = loanProductMapper.selectById(productId);
        if (ObjectUtils.isEmpty(loanProduct)) {
            throw new BusinessException("您要修改的银行产品不存在");
        }
        //loanProduct = dto2Entity(loanProductDTO);
        loanProduct.setTypeName(loanProductDTO.getTypeName());
        loanProduct.setAmount(loanProductDTO.getAmount());
        loanProduct.setDescription(loanProductDTO.getDescription());

        if (loanProductMapper.updateById(loanProduct) > 0) {
            return ApiResult.successOf("修改贷款产品成功");
        }

        return ApiResult.failOf("修改贷款产品失败");
    }

    /**
     * 查询贷款产品（银行业务员用）
     *
     * @param current 当前页码
     * @param size    页大小
     * @return res
     * @throws BusinessException e
     */
    @Override
    public ApiResult getLoanProduct(Long current, Long size) throws BusinessException {
        Integer bankId = JwtUtil.getUserFromRedis().getBankId();
        Page<LoanProduct> loanProducts = loanProductMapper.selectPage(new Page<>(current, size), new QueryWrapper<LoanProduct>().lambda()
                .eq(LoanProduct::getBankId, bankId)
                .and(wrapper -> wrapper.eq(LoanProduct::getValid, Boolean.TRUE)));
        if (!CollectionUtils.isEmpty(loanProducts.getRecords())) {
            for (LoanProduct product : loanProducts.getRecords()) {
                product.setBankName(bankService.getById(product.getBankId()).getBankName());
            }
        }
        return ApiResult.successOf(loanProducts);
    }

    /**
     * 查询所有贷款产品
     *
     * @param current 当前页码
     * @param size    页大小
     * @return res
     * @throws BusinessException e
     */
    @Override
    public ApiResult getAllLoanProduct(Long current, Long size) throws BusinessException {
        Page<LoanProduct> loanProducts = loanProductMapper.selectPage(new Page<>(current, size), new QueryWrapper<LoanProduct>().lambda()
                .and(wrapper -> wrapper.eq(LoanProduct::getValid, Boolean.TRUE)));
        if (!CollectionUtils.isEmpty(loanProducts.getRecords())) {
            for (LoanProduct product : loanProducts.getRecords()) {
                product.setBankName(bankService.getById(product.getBankId()).getBankName());
            }
        }
        return ApiResult.successOf(loanProducts);
    }

    private <T> LoanProduct dto2Entity(T dto) {
        LoanProduct loanProduct = new LoanProduct();
        if (dto instanceof LoanProductDTO) {
            LoanProductDTO loanProductDTO = (LoanProductDTO) dto;
            loanProduct.setAmount(loanProductDTO.getAmount())
                    .setDescription(loanProductDTO.getDescription())
                    .setTypeName(loanProductDTO.getTypeName());
        }
        return loanProduct;
    }
}
