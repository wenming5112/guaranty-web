package com.example.guaranty.entity.business;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.guaranty.entity.BaseEntity;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 贷款产品表 实体类
 *
 * @author ming
 * @version 1.0.0
 * @date 2020-11-29 12:11:50
 **/
@Data
@ToString
@TableName("t_loan_product")
@Accessors(chain = true)
public class LoanProduct extends BaseEntity<LoanProduct> {

    /**
     * 银行ID
     */
    @TableField("bank_id")
    private Integer bankId;

    /**
     * 类型名称
     */
    @TableField("type_name")
    private String typeName;
    /**
     * 描述
     */
    @TableField("description")
    private String description;
    /**
     * 贷款额度
     */
    @TableField("amount")
    private String amount;

    @TableField(exist = false)
    private String bankName;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
