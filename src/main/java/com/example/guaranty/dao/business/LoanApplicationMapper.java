package com.example.guaranty.dao.business;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import com.example.guaranty.entity.business.LoanApplication;

/**
 * 贷款申请表 mapper
 *
 * @author m
 * @version 1.0.0
 * @date 2020-11-26 14:13:11
 **/
@Mapper
public interface LoanApplicationMapper extends BaseMapper<LoanApplication> {

}
