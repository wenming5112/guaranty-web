package com.example.guaranty.vo.business;

import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author ming
 * @date 2019:08:31 10:41
 */
@Data
@ApiModel(value = "用户角色VO")
public class BackRoleVO implements Serializable {

    private final static long serialVersionUID = 1L;

    @ApiModelProperty(value = "角色id")
    private Integer roleId;

    @ApiModelProperty(value = "角色名")
    private String roleName;

    @ApiModelProperty(value = "角色描述")
    private String roleDesc;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
    private String createTime;

    @ApiModelProperty(value = "修改时间")
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
    private String modifyTime;

    @ApiModelProperty(value = "菜单集")
    protected List<BackMenuVO> menus;
}
