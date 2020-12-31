package com.example.guaranty.dao.business;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.guaranty.entity.business.BankSettlement;
import org.apache.ibatis.annotations.Mapper;

/**
 * 银行入驻申请 mapper
 *
 * @author m
 * @version 1.0.0
 * @date 2020-11-26 17:21:02
 **/
@Mapper
public interface BankSettlementMapper extends BaseMapper<BankSettlement> {

}
