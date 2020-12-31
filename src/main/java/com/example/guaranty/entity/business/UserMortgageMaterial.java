package com.example.guaranty.entity.business;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.guaranty.entity.BaseEntity;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * 用户抵押资料 实体类
 *
 * @author ming
 * @version 1.0.0
 * @date 2020-12-01 13:59:44
 **/
@Data
@ToString
@TableName("t_user_mortgage_material")
public class UserMortgageMaterial extends BaseEntity<UserMortgageMaterial> {

    /**
     * 用户ID
     */
    @TableField("user_id")
    private Integer userId;
    /**
     * 房产资料地址
     */
    @TableField("house_material_url")
    private String houseMaterialUrl;
    /**
     * 房产资料Hash
     */
    @TableField("house_material_hash")
    private String houseMaterialHash;
    /**
     * 产权资料地址
     */
    @TableField("property_material_url")
    private String propertyMaterialUrl;
    /**
     * 产权资料hash
     */
    @TableField("property_material_hash")
    private String propertyMaterialHash;
    /**
     * 车辆资料地址
     */
    @TableField("car_material_url")
    private String carMaterialUrl;
    /**
     * 车辆资料hash
     */
    @TableField("car_material_hash")
    private String carMaterialHash;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
