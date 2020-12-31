package com.example.guaranty.vo.business;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author ming
 * @version 1.0.0
 * @date 2020/12/1 14:45
 **/
@Data
@ApiModel(value = "资料上传VO")
public class MaterialUploadVO {

    @ApiModelProperty(value = "资料URL")
    private String materialUrl;

    @ApiModelProperty(value = "资料Hash")
    private String materialHash;
}
