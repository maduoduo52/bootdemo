package com.mdd.admin.config;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * @Desc:
 * @Author Maduo
 * @Create 2018/12/1 17:29
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface IgnoreLogin {

}
