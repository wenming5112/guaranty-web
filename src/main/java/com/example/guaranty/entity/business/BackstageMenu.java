package com.example.guaranty.entity.business;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.guaranty.entity.BaseEntity;
import com.example.guaranty.entity.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 菜单 实体类
 *
 * @author ming
 * @date 2020/04/16
 */
@TableName("t_backstage_menu")
@Data
public class BackstageMenu extends BaseEntity<BackstageMenu> {

    private static final long serialVersionUID = 4814002326662320464L;
    /**
     * 导航栏显示名
     */
    @TableField("title")
    private String title;
    /**
     * 路由图标
     */
    @TableField("icon")
    private String icon;
    /**
     * 前端路由
     */
    @TableField("path")
    private String path;

    /**
     * 前端组件路径
     */
    @TableField("component")
    private String component;

    /**
     * 权限
     */
    @TableField("permission")
    private String permission;

    /**
     * 描述
     */
    @TableField("description")
    private String description;
    /**
     * 1-按钮 2-下拉菜单
     */
    @TableField("type")
    private Integer type;
    /**
     * 父级id
     */
    @TableField("parent_id")
    private Integer parentId;

    /**
     * 后台访问的url
     */
    @TableField("url")
    private String url;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
