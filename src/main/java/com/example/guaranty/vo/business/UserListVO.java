package com.example.guaranty.vo.business;

import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 用户信息VO
 *
 * @author ming
 * @version 1.0.0
 * @date 2020/5/26 16:47
 **/
@Data
@ApiModel(value = "用户信息列表VO")
public class UserListVO implements Serializable {

    @ApiModelProperty(value = "用户id")
    protected Integer userId;

    @ApiModelProperty(value = "用户名")
    protected String username;

    @ApiModelProperty(value = "手机号码")
    protected String telephone;

    @ApiModelProperty(value = "邮箱")
    protected String email;

    @ApiModelProperty(value = "状态")
    protected Integer userStatus;

    @ApiModelProperty(value = "操作状态")
    protected Integer operationStatus;

    @ApiModelProperty(value = "登陆IP")
    private String loginIp;

    @ApiModelProperty(value = "登陆地址")
    private String loginAddress;

    @ApiModelProperty(value = "角色集")
    protected List<BackRoleVO> roles;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
    protected String createTime;

    @ApiModelProperty(value = "修改时间")
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
    protected String modifyTime;
}
