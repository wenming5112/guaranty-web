package com.example.guaranty.entity.business;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.guaranty.entity.BaseEntity;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 贷款申请表 实体类
 *
 * @author ming
 * @version 1.0.0
 * @date 2020-11-26 14:13:11
 **/
@Data
@ToString
@TableName("t_loan_application")
@Accessors(chain = true)
public class LoanApplication extends BaseEntity<LoanApplication> {

    /**
     * 银行id
     */
    @TableField("bank_id")
    private Integer bankId;
    /**
     * 贷款产品ID
     */
    @TableField("loan_product_id")
    private Integer loanProductId;
    /**
     * 申请人
     */
    @TableField("proposer")
    private String proposer;
    /**
     * 状态(1-access,0-refuse)
     */
    @TableField("status")
    private Integer status;
    /**
     * 拒绝原因
     */
    @TableField("refuse_reason")
    private String refuseReason;

    @TableField("material_url")
    private String materialUrl;

    @TableField("material_hash")
    private String materialHash;

    @TableField(exist = false)
    private BackstageUser user;

    @TableField(exist = false)
    private LoanProduct loanProduct;

    @TableField(exist = false)
    private Bank bank;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
