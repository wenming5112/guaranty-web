package com.example.guaranty.entity.business;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.guaranty.entity.BaseEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * 角色菜单
 *
 * @author ming
 * @date 2020/04/16
 */
@TableName("t_backstage_role_menu")
@Data
public class BackstageRoleMenu extends BaseEntity<BackstageRoleMenu> {

    private static final long serialVersionUID = 5460104780843016805L;
    /**
     * 后台角色ID
     */
    @TableField("back_role_id")
    private Integer backRoleId;
    /**
     * 后台菜单ID
     */
    @TableField("back_menu_id")
    private Integer backMenuId;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
