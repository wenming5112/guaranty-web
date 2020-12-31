package com.example.guaranty.dto.business;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author ming
 * @version 1.0.0
 * @date 2020/12/1 10:50
 **/
@Data
@ApiModel(value = "用户基本信息DTO")
public class UserBaseInfoDTO {

    @ApiModelProperty(value = "真实姓名", required = true)
    @NotBlank(message = "姓名不能为空")
    private String realName;

    @ApiModelProperty(value = "身份证号", required = true)
    @NotBlank(message = "身份证号")
    private String idNo;

    @ApiModelProperty(value = "性别", required = true)
    @NotBlank(message = "性别")
    private String sex;

    @ApiModelProperty(value = "年龄", required = true)
    @NotNull(message = "年龄")
    private Integer age;

    @ApiModelProperty(value = "手机号", required = true)
    @NotBlank(message = "手机号")
    private String tel;
}
