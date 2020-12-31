package com.example.guaranty.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义注解，用来标识请求类 或者方法是否使用AOP加密解密
 *
 * @author ming
 * @version 1.0.0
 * @date 2019-12-05 13:48:03
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RsaDecode {

    // 参数类中传递加密数据的属性名，默认encryptStr
    String encryptStrName() default "encodeData";
}
