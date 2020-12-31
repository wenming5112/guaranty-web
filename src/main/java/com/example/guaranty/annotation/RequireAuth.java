package com.example.guaranty.annotation;

import java.lang.annotation.*;

/**
 * @author ming
 * @version 1.0.0
 * @date 2020/11/27 10:55
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface RequireAuth {

    String[] roleName() default {"user"};

}
