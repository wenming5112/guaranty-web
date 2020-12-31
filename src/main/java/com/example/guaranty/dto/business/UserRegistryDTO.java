package com.example.guaranty.dto.business;

import com.example.guaranty.common.constant.RegexConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 用户注册
 *
 * @author ming
 * @version 1.0.0
 * @date 2020/4/20 9:51
 **/
@Data
@ApiModel(value = "用户注册DTO")
public class UserRegistryDTO {

    private static final long serialVersionUID = -5525614879976824141L;
    @ApiModelProperty(value = "用户名首个必须是字母,并且只由数字和字母组成,长度4-16位", example = "xiaom123", required = true)
    @NotBlank(message = "用户名不能为空")
    @Pattern(regexp = RegexConstant.USERNAME_REG, message = "用户名格式不正确")
    @Length(min = 4, max = 16, message = "用户名首个必须是字母,并且只由数字和字母组成,长度4-16位")
    private String username;

    @ApiModelProperty(value = "密码首个必须是字母，有数字字母下划线组成,长度为6-20位", example = "x123456", required = true)
    @NotBlank(message = "密码不能为空")
    @Length(min = 6, max = 20, message = "密码长度限制为6-20")
    @Pattern(regexp = RegexConstant.PASSWORD_REG, message = "密码首个必须是字母，有数字字母下划线组成,长度为6-20位")
    private String password;

    @ApiModelProperty(value = "邮箱", example = "1299986041@qq.com")
    @NotBlank(message = "邮箱不能为空")
    @Pattern(regexp = RegexConstant.EMAIL_REG, message = "邮箱格式不正确")
    private String email;

    @ApiModelProperty(value = "验证码")
    @NotBlank(message = "验证码不能空")
    private String verifyCode;

    @ApiModelProperty(value = "手机号码", example = "18208201778")
    @NotBlank(message = "手机不能为空")
    @Pattern(regexp = RegexConstant.TELEPHONE_REG, message = "手机号码格式不正确")
    private String telephone;

}
