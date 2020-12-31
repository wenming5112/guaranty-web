package com.example.guaranty.entity;

import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 树形菜单实体类
 *
 * @author ming
 * @since 2020-11-11
 */
@Data
public class TreeNode<T> {

    private Integer menuId;

    private String title;

    private String path;

    private String component;

    private Integer pid;

    private String url;

    private String icon;

    private Integer type;

    private String permission;

    private String description;

    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
    private String createTime;

    @JsonIgnore
    private boolean hasParent = false;

    @JsonIgnore
    private boolean hasChildren = false;

    public List<TreeNode<T>> children;

    public void initChildren() {
        this.children = new ArrayList<>();
    }

}
