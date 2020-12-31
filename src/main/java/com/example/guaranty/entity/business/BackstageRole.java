package com.example.guaranty.entity.business;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.guaranty.entity.BaseEntity;
import com.example.guaranty.entity.BaseEntity;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 角色 实体类
 *
 * @author ming
 * @date 2020/04/16
 */
@TableName(value = "t_backstage_role", autoResultMap = true)
@Data
public class BackstageRole extends BaseEntity<BackstageRole> {

    private static final long serialVersionUID = 6145852080601800747L;
    /**
     * 角色名
     */
    @TableField("role_name")
    private String roleName;
    /**
     * 角色说明
     */
    @TableField("role_desc")
    private String roleDesc;
    /**
     *
     */
    @TableField("status_id")
    private Integer statusId;

    /**
     * 角色资源列表
     */
    @TableField(exist = false)
    private List<BackstageMenu> menus;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
