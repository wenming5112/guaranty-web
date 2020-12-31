package com.example.guaranty.dto.business;

import com.example.guaranty.common.constant.RegexConstant;
import com.example.guaranty.common.constant.RegexConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 后台用户 DTO
 *
 * @author ming
 * @version 1.0.0
 * @date 2019:08:28 16:17
 */
@Data
@ApiModel(value = "后台用户DTO")
public class BackUserDTO {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "要修改的用户id", required = true)
    private Integer userId;

    @ApiModelProperty(value = "邮箱", required = true)
    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String email;

    @ApiModelProperty(value = "手机号码", required = true)
    @NotBlank(message = "手机号码不能为空")
    @Pattern(regexp = RegexConstant.TELEPHONE_REG, message = "手机号码格式不正确")
    private String telephone;

    @ApiModelProperty(value = "角色列表", required = true)
    private Integer[] roles;
}
