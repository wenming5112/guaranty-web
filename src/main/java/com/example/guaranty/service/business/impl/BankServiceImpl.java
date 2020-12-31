package com.example.guaranty.service.business.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.guaranty.common.exception.BusinessException;
import com.example.guaranty.common.handle.ApiResult;
import com.example.guaranty.dao.business.BankMapper;
import com.example.guaranty.entity.BaseEntity;
import com.example.guaranty.entity.business.Bank;
import com.example.guaranty.service.business.BankService;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;


/**
 * 银行信息表 Impl
 *
 * @author m
 * @version 1.0.0
 * @date 2020-11-26 14:13:11
 **/
@Service
public class BankServiceImpl extends ServiceImpl<BankMapper, Bank> implements BankService {

    @Resource
    private BankMapper bankMapper;

    /**
     * 查询银行列表
     *
     * @param current 当前页码
     * @param size    页大小
     * @return res
     * @throws BusinessException e
     */
    @Override
    public ApiResult<Page<Bank>> getList(Long current, Long size) throws BusinessException {
        Page<Bank> bank = bankMapper.selectPage(new Page<>(current, size), new QueryWrapper<Bank>().lambda().
                eq(BaseEntity::getValid, Boolean.TRUE));
        return ApiResult.successOf(bank);
    }

    /**
     * 查询所有银行
     *
     * @return res
     * @throws BusinessException e
     */
    @Override
    public ApiResult getAll() throws BusinessException {
        List<Bank> list = bankMapper.selectList(new QueryWrapper<Bank>().lambda().eq(BaseEntity::getValid, Boolean.TRUE));
        return ApiResult.successOf(list);
    }

    /**
     * 根据名称查询
     *
     * @param name 名称
     * @return res
     * @throws BusinessException e
     */
    @Override
    public Bank getByName(String name) throws BusinessException {
        return bankMapper.selectOne(new QueryWrapper<Bank>().lambda()
                .eq(Bank::getBankName, name)
                .and(wrapper -> wrapper.eq(BaseEntity::getValid, Boolean.TRUE)));
    }


}
