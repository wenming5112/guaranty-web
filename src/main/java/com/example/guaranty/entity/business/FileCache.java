package com.example.guaranty.entity.business;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.guaranty.entity.BaseEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * 文件存储 实体类
 *
 * @author m
 * @date 2020/04/16
 */
@Data
@TableName("t_file_cache")
public class FileCache extends BaseEntity<FileCache> {

    private static final long serialVersionUID = 5540225530500214422L;
    /**
     * 文件名
     */
    @TableField("title")
    private String title;
    /**
     * 文件名后缀
     */
    @TableField("title_suffix")
    private String titleSuffix;
    /**
     * 文件大小
     */
    @TableField("file_size")
    private Long fileSize;
    /**
     * 链接
     */
    @TableField("url")
    private String url;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
