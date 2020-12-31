package com.example.guaranty.service.business;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.guaranty.common.exception.BusinessException;
import com.example.guaranty.common.handle.ApiResult;
import com.example.guaranty.entity.business.Bank;

/**
 * 银行信息表 Service
 *
 * @author m
 * @version 1.0.0
 * @date 2020-11-26 14:13:11
 **/
public interface BankService extends IService<Bank> {

    /**
     * 查询银行列表
     *
     * @param current 当前页码
     * @param size    页大小
     * @return res
     * @throws BusinessException e
     */
    ApiResult<Page<Bank>> getList(Long current, Long size) throws BusinessException;

    /**
     * 查询所有银行
     *
     * @return res
     * @throws BusinessException e
     */
    ApiResult getAll() throws BusinessException;

    /**
     * 根据名称查询
     *
     * @param name 名称
     * @return res
     * @throws BusinessException e
     */
    Bank getByName(String name) throws BusinessException;
}
