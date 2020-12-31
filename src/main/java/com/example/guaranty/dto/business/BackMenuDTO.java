package com.example.guaranty.dto.business;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 后台菜单DTO
 *
 * @author ming
 * @version 1.0.0
 * @date 2019 14:37
 */
@ApiModel(value = "后台菜单DTO")
@Data
public class BackMenuDTO implements Serializable {

    private final static long serialVersionUID = 1L;

    @ApiModelProperty(value = "菜单id")
    private Integer menuId;

    @ApiModelProperty(value = "标题名", required = true)
    @NotBlank(message = "标题不能为空")
    private String title;

    @ApiModelProperty(value = "图标")
    private String icon;

    @ApiModelProperty(value = "前端路由", required = true)
    private String path;

    @ApiModelProperty(value = "前端组件路径", required = true, example = "/system/menu/")
    private String component;

    @ApiModelProperty(value = "权限", example = "user:add")
    private String permission;

    @ApiModelProperty(value = "类型(0-按钮 1-菜单)", example = "1", required = true, allowableValues = "0,1")
    @NotNull(message = "类型不能为空")
    private Integer type;

    @ApiModelProperty(value = "后台接口路径(开发者填写)")
    private String url;

    @ApiModelProperty(dataType = "int", value = "父级菜单id", required = true, example = "0")
    @NotNull(message = "父级菜单id不能为空")
    private Integer pid;

    @ApiModelProperty(value = "菜单描述", example = "xx(功能描述)")
    private String description;

}
