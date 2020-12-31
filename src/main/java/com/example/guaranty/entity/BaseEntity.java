package com.example.guaranty.entity;

import cn.hutool.core.date.DatePattern;
import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 基础实体
 *
 * @author ming
 * @date 2020/04/16
 */
@Data
@Accessors(chain = true)
public abstract class BaseEntity<T> extends Model<BaseEntity<T>> {

    private static final long serialVersionUID = 5796049886922555842L;
    @TableId(value = "id")
    public Integer id;

    /**
     * 1 true 0 false
     */
    @TableField("valid")
    public Boolean valid = true;

    /**
     * 创建者
     */
    @TableField("creator")
    public String creator;

    /**
     * 最后一次修改者
     */
    @TableField("modifier")
    public String modifier;

    /**
     * 创建时间
     */
    @JSONField(format = DatePattern.NORM_DATETIME_PATTERN)
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
    @TableField(value = "create_time")
    public String createTime;

    /**
     * 修改时间
     */
    @JSONField(format = DatePattern.NORM_DATETIME_PATTERN)
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
    @TableField(value = "modify_time")
    public String modifyTime;

    /**
     * 查询关键字
     */
    @TableField(exist = false)
    public String searchKey;

    /**
     * 状态（0：正常；1：删除）
     */
    public static final Boolean VALID = true;
    public static final Boolean IN_VALID = false;
}
