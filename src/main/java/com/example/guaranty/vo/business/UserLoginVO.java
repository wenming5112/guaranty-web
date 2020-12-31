package com.example.guaranty.vo.business;

import cn.hutool.core.date.DatePattern;
import com.example.guaranty.entity.TreeNode;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.example.guaranty.entity.TreeNode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.util.ObjectUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 * @date 2019:08:26 16:14
 */
@Data
@ApiModel(value = "用户信息VO")
@Accessors(chain = true)
public class UserLoginVO implements Serializable {

    private static final long serialVersionUID = -3468247231212840519L;
    @ApiModelProperty(value = "用户id")
    protected Integer userId;

    @ApiModelProperty(value = "用户名")
    protected String userName;

    @ApiModelProperty(value = "手机号码")
    protected String telephone;

    @ApiModelProperty(value = "邮箱")
    protected String email;

    @ApiModelProperty(value = "状态")
    protected Integer userStatus;

    @ApiModelProperty(value = "操作状态")
    protected Integer operationStatus;

    @ApiModelProperty(value = "token")
    protected String token;

    @ApiModelProperty(value = "loginIp")
    private String loginIp;

    @ApiModelProperty(value = "loginAddress")
    private String loginAddress;

    @ApiModelProperty(value = "是否实名")
    private Boolean realNameAuthentication;

    @ApiModelProperty(value = "真实姓名")
    private String realName;

    @ApiModelProperty(value = "身份证号")
    private String idNo;

    @ApiModelProperty(value = "角色集")
    protected List<BackRoleVO> roles;

    @ApiModelProperty(value = "菜单集")
    protected List<BackMenuVO> menus;

    @ApiModelProperty(value = "用户路由")
    private List<TreeNode<BackMenuVO>> userRoutes;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
    protected String createTime;

    @ApiModelProperty(value = "修改时间")
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
    protected String modifyTime;

    public void setMyMenus(List<BackRoleVO> roles) {
        List<BackMenuVO> menusSet = new ArrayList<>();
        for (BackRoleVO role : roles) {
            if (!ObjectUtils.isEmpty(role.getMenus())) {
                menusSet.addAll(role.getMenus());
            }
        }
        this.menus = menusSet;
    }
}
