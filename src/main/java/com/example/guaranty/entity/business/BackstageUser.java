package com.example.guaranty.entity.business;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.guaranty.entity.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * 用户 实体类
 *
 * @author ming
 * @date 2020/04/16
 */
@TableName("t_backstage_user")
@Data
@Accessors(chain = true)
public class BackstageUser extends BaseEntity<BackstageUser> {

    private static final long serialVersionUID = 8217603031866514703L;
    /**
     * 用户名
     */
    @TableField("user_name")
    private String userName;
    /**
     * 真实姓名
     */
    @TableField("real_name")
    private String realName;
    /**
     * 密码
     */
    @TableField("password")
    private String password;
    /**
     * 邮箱
     */
    @TableField("email")
    private String email;
    /**
     * 状态
     */
    @TableField("status")
    private Integer status = 1;
    /**
     * 手机号码
     */
    @TableField("telephone")
    private String telephone;

    /**
     * 是否实名认证(1-实名，0-未实名)
     */
    @TableField("real_name_authentication")
    private Boolean realNameAuthentication = false;

    /**
     * 证件号
     */
    @TableField("id_card")
    private String idCard;

    /**
     * 银行ID
     */
    @TableField("bank_id")
    private Integer bankId;

    /**
     * 操作状态
     */
    @TableField("operation_status")
    private Integer operationStatus;

    /**
     * 登录ip
     */
    @TableField("login_ip")
    private String loginIp;

    /**
     * 登录地址
     */
    @TableField("login_address")
    private String loginAddress;

    @TableField("sex")
    private String sex;

    @TableField("age")
    private Integer age;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @TableField(exist = false)
    private List<BackstageRole> roles;
}
