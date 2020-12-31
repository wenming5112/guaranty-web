package com.example.guaranty.dto.business;

import com.example.guaranty.common.constant.RegexConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 后台用户新增
 *
 * @author ming
 * @version 1.0.0
 * @date 2020/4/20 9:51
 **/
@Data
@ApiModel(value = "新增用户")
public class AddUserDTO {

    private static final long serialVersionUID = -5525614879976824141L;
    @ApiModelProperty(value = "用户名首个必须是字母,并且只由数字和字母组成,长度4-20位", example = "xiaom123", required = true)
    @NotBlank(message = "用户名不能为空")
    @Pattern(regexp = RegexConstant.PASSWORD_REG, message = "用户名格式不正确")
    @Length(min = 4, max = 16, message = "用户名首个必须是字母,并且只由数字和字母组成,长度4-20位")
    private String username;

    @ApiModelProperty(value = "密码首个必须是字母，有数字字母下划线组成,长度为6-20位", example = "x123456", required = true)
    @NotBlank(message = "密码不能为空")
    @Length(min = 6, max = 20, message = "密码长度限制为6-20")
    @Pattern(regexp = RegexConstant.PASSWORD_REG, message = "密码首个必须是字母，有数字字母下划线组成,长度为6-20位")
    private String password;

    @ApiModelProperty(value = "邮箱(暂未引入邮箱验证服务，可以不填写)", hidden = true)
//    @NotBlank(message = "邮箱不能为空")
//    @Pattern(regexp = RegexConstant.EMAIL_REG, message = "邮箱格式不正确")
    private String email;

    @ApiModelProperty(value = "手机号码", hidden = true)
//    @NotBlank(message = "手机不能为空")
//    @Pattern(regexp = RegexConstant.TELEPHONE_REG, message = "手机号码格式不正确")
    private String telephone;

    @ApiModelProperty(value = "用户角色(数组里面是角色id),如果不修改,则默认普通用户", example = "[1,2,3,4,5]", hidden = true)
    private Integer[] roles;
}
