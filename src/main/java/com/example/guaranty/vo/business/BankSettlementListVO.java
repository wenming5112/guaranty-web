package com.example.guaranty.vo.business;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author ming
 * @version 1.0.0
 * @date 2020/11/27 17:11
 **/
@Data
@ApiModel(value = "银行入驻列表VO")
@Accessors(chain = true)
public class BankSettlementListVO {

    @ApiModelProperty(value = "主键ID")
    private Integer id;
    /**
     * 银行名称
     */
    @ApiModelProperty(value = "银行名称")
    private String bankName;
    /**
     * 银行地址
     */
    @ApiModelProperty(value = "银行地址")
    private String bankAddress;
    /**
     * 银行电话
     */
    @ApiModelProperty(value = "银行电话")
    private String bankTel;
    /**
     * 申请人姓名
     */
    @ApiModelProperty(value = "申请人姓名")
    private String proposer;
    /**
     * 申请人身份证
     */
    @ApiModelProperty(value = "申请人身份证")
    private String proposerIdCard;
    /**
     * 申请人电话
     */
    @ApiModelProperty(value = "申请人电话")
    private String proposerTel;
    /**
     * 法人姓名
     */
    @ApiModelProperty(value = "法人姓名")
    private String legalPersonName;
    /**
     * 法人身份证
     */
    @ApiModelProperty(value = "法人身份证")
    private String legalPersonIdCard;
    /**
     * 法人电话
     */
    @ApiModelProperty(value = "法人电话")
    private String legalPersonTel;
    /**
     * 银行牌照
     */
    @ApiModelProperty(value = "银行牌照")
    private String bankLicense;

    @ApiModelProperty(value = "未通过原因")
    private String disagreeReason;

    @ApiModelProperty(value = "创建时间")
    private String createTime;

    @ApiModelProperty(value = "状态")
    private Integer status;
}
