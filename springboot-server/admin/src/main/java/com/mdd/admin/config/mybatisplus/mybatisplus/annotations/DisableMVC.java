package com.mdd.admin.config.mybatisplus.mybatisplus.annotations;

import com.mdd.admin.config.mybatisplus.mybatisplus.model.MVCEnume;

import java.lang.annotation.*;

/**
 * @Desc:
 * @Author Maduo
 * @Create 2018/12/1 15:57
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface DisableMVC {

    /**
     * 那些地址禁止访问
     * @return
     */
    MVCEnume[] address() default {MVCEnume.deleteBatchIds};
}
