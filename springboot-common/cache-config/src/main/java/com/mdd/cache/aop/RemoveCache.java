package com.mdd.cache.aop;

import java.lang.annotation.*;

/**
 * @Desc:
 * @Author Maduo
 * @Create 2018/11/25 13:54
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RemoveCache {

    /**
     * key 值 不能为空，需要自己去指定
     * @return
     */
    String key();

    /**
     * 删除所有的这个key相关的数据
     * @return
     */
    boolean keyAll() default false;

    /**
     * 获取方法的那些参数作为key值  全部进行hash操作 所以具有hash碰撞风险
     */
    String [] params() default {};
}
