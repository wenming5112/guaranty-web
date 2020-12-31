package com.example.guaranty.entity.business;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.guaranty.entity.BaseEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户角色 实体类
 *
 * @author ming
 * @date 2020/04/16
 */
@TableName("t_backstage_user_role")
@Data
public class BackstageUserRole extends BaseEntity<BackstageUserRole> {

    private static final long serialVersionUID = 2833283339748745991L;
    /**
     * 后台用户ID
     */
    @TableField("back_user_id")
    private Integer backUserId;
    /**
     * 后台角色ID
     */
    @TableField("back_role_id")
    private Integer backRoleId;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
