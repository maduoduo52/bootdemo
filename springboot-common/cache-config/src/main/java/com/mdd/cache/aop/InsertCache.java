package com.mdd.cache.aop;

import java.lang.annotation.*;

/**
 * @Desc:
 * @Author Maduo
 * @Create 2018/11/25 13:51
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface InsertCache {

    /**
     * key 为空的情况下  则为 类路径.方法名  切记方法名可能会重复
     * @return
     */
    String key() default "";

    /**
     * 缓存时间  默认60S
     * @return
     */
    int time() default 60;

    /**
     * 获取方法的那些参数作为key值   全部进行hash操作 所以具有hash碰撞风险
     */
    String [] params() default {};

    /**
     * 方法返回的class
     * @return
     */
    Class<?> clazz() default NoCacheClazz.class;
}
