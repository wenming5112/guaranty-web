package com.example.guaranty.dto.business;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author ming
 * @version 1.0.0
 * @date 2020/12/2 17:22
 **/

@Data
@ApiModel(value = "贷款申请DTO")
public class LoanApplicationDTO {
    /**
     * 银行id
     */
    @ApiModelProperty(value = "银行ID", required = true)
    private Integer bankId;
    /**
     * 贷款产品ID
     */
    @ApiModelProperty(value = "贷款产品ID", required = true)
    private Integer loanProductId;
    /**
     * 申请人
     */
    @ApiModelProperty(value = "申请人", required = true)
    private String proposer;

}
