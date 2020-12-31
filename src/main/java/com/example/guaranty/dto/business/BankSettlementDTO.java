package com.example.guaranty.dto.business;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author ming
 * @version 1.0.0
 * @date 2020/11/26 17:25
 **/
@Data
@ApiModel(value = "银行注入申请DTO")
public class BankSettlementDTO {

    /**
     * 银行名称
     */
    @ApiModelProperty(value = "银行名称", required = true)
    private String bankName;
    /**
     * 银行地址
     */
    @ApiModelProperty(value = "银行地址", required = true)
    private String bankAddress;
    /**
     * 银行电话
     */
    @ApiModelProperty(value = "银行电话", required = true)
    private String bankTel;
    /**
     * 申请人姓名
     */
    @ApiModelProperty(value = "申请人姓名", required = true)
    private String proposer;
    /**
     * 申请人身份证
     */
    @ApiModelProperty(value = "申请人身份证", required = true)
    private String proposerIdCard;
    /**
     * 申请人电话
     */
    @ApiModelProperty(value = "申请人电话", required = true)
    private String proposerTel;
    /**
     * 法人姓名
     */
    @ApiModelProperty(value = "法人姓名", required = true)
    private String legalPersonName;
    /**
     * 法人身份证
     */
    @ApiModelProperty(value = "法人身份证", required = true)
    private String legalPersonIdCard;
    /**
     * 法人电话
     */
    @ApiModelProperty(value = "法人电话", required = true)
    private String legalPersonTel;
    /**
     * 银行牌照
     */
    @ApiModelProperty(value = "银行牌照", required = true)
    private String bankLicense;

}
