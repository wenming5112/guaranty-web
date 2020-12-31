package com.example.guaranty.dao.business;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import com.example.guaranty.entity.business.LoanProduct;

/**
 * 贷款产品表 mapper
 *
 * @author m
 * @version 1.0.0
 * @date 2020-11-29 12:11:50
 **/
@Mapper
public interface LoanProductMapper extends BaseMapper<LoanProduct> {

}
