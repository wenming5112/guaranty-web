package com.example.guaranty.entity.business;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.guaranty.entity.BaseEntity;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 银行信息表 实体类
 *
 * @author ming
 * @version 1.0.0
 * @date 2020-11-26 14:13:11
 **/
@Data
@ToString
@TableName("t_bank")
@Accessors(chain = true)
public class Bank extends BaseEntity<Bank> {

    /**
     * 银行名称
     */
    @TableField("bank_name")
    private String bankName;
    /**
     * 银行icon
     */
    @TableField("bank_icon")
    private String bankIcon;
    /**
     * 银行地址
     */
    @TableField("bank_addr")
    private String bankAddr;
    /**
     * 银行电话
     */
    @TableField("bank_tel")
    private String bankTel;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
