package com.example.guaranty.entity.business;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.guaranty.entity.BaseEntity;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 银行入驻申请 实体类
 *
 * @author ming
 * @version 1.0.0
 * @date 2020-11-26 17:21:02
 **/
@Data
@ToString
@TableName("t_bank_settlement")
@Accessors(chain = true)
public class BankSettlement extends BaseEntity<BankSettlement> {

    /**
     * 银行名称
     */
    @TableField("bank_name")
    private String bankName;
    /**
     * 银行地址
     */
    @TableField("bank_address")
    private String bankAddress;
    /**
     * 银行电话
     */
    @TableField("bank_tel")
    private String bankTel;
    /**
     * 申请人姓名
     */
    @TableField("proposer")
    private String proposer;
    /**
     * 申请人身份证
     */
    @TableField("proposer_id_card")
    private String proposerIdCard;
    /**
     * 申请人电话
     */
    @TableField("proposer_tel")
    private String proposerTel;
    /**
     * 法人姓名
     */
    @TableField("legal_person_name")
    private String legalPersonName;
    /**
     * 法人身份证
     */
    @TableField("legal_person_id_card")
    private String legalPersonIdCard;
    /**
     * 法人电话
     */
    @TableField("legal_person_tel")
    private String legalPersonTel;
    /**
     * 银行牌照
     */
    @TableField("bank_license")
    private String bankLicense;
    /**
     * 未通过理由
     */
    @TableField("disagree_reason")
    private String disagreeReason;
    /**
     * 状态
     */
    @TableField("status")
    private Integer status;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
