package com.example.guaranty.dao.business;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import com.example.guaranty.entity.business.Bank;

/**
 * 银行信息表 mapper
 *
 * @author m
 * @version 1.0.0
 * @date 2020-11-26 14:13:11
 **/
@Mapper
public interface BankMapper extends BaseMapper<Bank> {

}
