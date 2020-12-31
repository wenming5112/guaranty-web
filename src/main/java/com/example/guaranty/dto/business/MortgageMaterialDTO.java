package com.example.guaranty.dto.business;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author ming
 * @version 1.0.0
 * @date 2020/12/1 13:47
 **/
@Data
@ApiModel(value = "抵押资料DTO")
public class MortgageMaterialDTO {
    @ApiModelProperty(value = "房产抵押URL", required = true)
    @NotBlank(message = "房产抵押URL不能为空")
    private String houseMaterialUrl;

    @ApiModelProperty(value = "产权抵押URL", required = true)
    @NotBlank(message = "产权抵押URL不能为空")
    private String propertyMaterialUrl;

    @ApiModelProperty(value = "车辆抵押URL", required = true)
    @NotBlank(message = "车辆抵押URL不能为空")
    private String carMaterialUrl;

    /**
     * 房产资料Hash
     */
    @ApiModelProperty(value = "房产抵押Hash", required = true)
    @NotBlank(message = "房产抵押Hash不能为空")
    private String houseMaterialHash;

    /**
     * 产权资料hash
     */
    @ApiModelProperty(value = "产权抵押Hash", required = true)
    @NotBlank(message = "产权抵押Hash不能为空")
    private String propertyMaterialHash;

    /**
     * 车辆资料hash
     */
    @ApiModelProperty(value = "车辆抵押Hash", required = true)
    @NotBlank(message = "车辆抵押Hash不能为空")
    private String carMaterialHash;
}
