package com.example.guaranty.vo.business;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户信息VO
 *
 * @author ming
 * @version 1.0.0
 * @date 2019 10:30
 */
@Data
@ApiModel(value = "用户信息VO")
@JsonIgnoreProperties(value = "handler")
public class UserInfoVO extends UserLoginVO implements Serializable {

    private static final long serialVersionUID = 4340499357347542307L;

    @ApiModelProperty(value = "jwtToken", hidden = true)
    private String jwtToken;

    /**
     * 为bank-staff 的时候需要设置
     */
    @ApiModelProperty(value = "bankId")
    private Integer bankId;

    @Override
    public String toString() {
        return "UserInfoVO{" +
                "userId=" + userId +
                ", username='" + userName + '\'' +
                ", telephone='" + telephone + '\'' +
                ", email='" + email + '\'' +
                ", userStatus=" + userStatus +
                ", operationStatus=" + operationStatus +
                ", token='" + token + '\'' +
                ", jwtToken='" + jwtToken + '\'' +
                ", roles=" + roles +
                ", menus=" + menus +
                ", createTime=" + createTime +
                ", modifyTime=" + modifyTime +
                '}';
    }
}
