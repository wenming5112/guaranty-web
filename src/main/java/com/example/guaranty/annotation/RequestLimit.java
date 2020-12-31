package com.example.guaranty.annotation;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.lang.annotation.*;

/**
 * 请求次数限制
 *
 * @author ming
 * @version 1.0.0
 * @date 2020/6/16 15:18
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
@Order(Ordered.HIGHEST_PRECEDENCE)
public @interface RequestLimit {

    /**
     * 运行的最大访问次数
     *
     * @return int
     */
    int count() default 3;

    /**
     * 时间段，单位为毫秒，默认值10秒
     *
     * @return long
     */
    long time() default 10000;

    /**
     * 接口名
     *
     * @return String
     */
    String apiName() default "*";

    String message() default "系统繁忙";
}
