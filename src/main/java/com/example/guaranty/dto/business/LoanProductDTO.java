package com.example.guaranty.dto.business;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author ming
 * @version 1.0.0
 * @date 2020/11/29 12:18
 **/
@Data
@ApiModel(value = "贷款产品DTO")
public class LoanProductDTO {
//    /**
//     * 银行ID
//     */
//    @ApiModelProperty(value = "银行ID", required = true)
//    private Integer bankId;
    /**
     * 类型名称
     */
    @ApiModelProperty(value = "类型名称", required = true)
    private String typeName;
    /**
     * 描述
     */
    @ApiModelProperty(value = "描述", required = true)
    private String description;
    /**
     * 贷款额度
     */
    @ApiModelProperty(value = "贷款额度", required = true)
    private String amount;
}
