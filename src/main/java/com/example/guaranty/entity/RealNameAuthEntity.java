package com.example.guaranty.entity;

import lombok.Data;
import lombok.ToString;

/**
 * @author ming
 * @version 1.0.0
 * @date 2020/11/27 10:30
 **/
@Data
@ToString
public class RealNameAuthEntity {
    private String name;
    private String idNo;
    private String respMessage;
    private String respCode;
    private String province;
    private String city;
    private String county;
    private String birthday;
    private String sex;
    private String age;
}
