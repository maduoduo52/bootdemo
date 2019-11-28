package com.mdd.admin.config;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * @author Maduo
 * @date 2019/11/28 18:31
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface SubmitValite {

    long times() default 3;
}
