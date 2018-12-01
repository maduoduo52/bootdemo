package com.mdd.cache.aop.Lock;

import java.lang.annotation.*;

/**
 * @Desc: 分布式锁注解
 * @Author Maduo
 * @Create 2018/11/25 14:03
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Lock {

    /**
     * 加锁时间长  单位毫秒   必须设置 不然会出现死锁的情况
     * @return
     */
    int time() default 1000;

    /**
     * 获取方法的那些参数作为key值   全部进行hash操作 所以具有hash碰撞风险
     */
    String [] params() default {};

    /**
     * afterThrowing 后是否删除锁
     * @return
     */
    boolean afterThrowing() default true;

    /**
     * after 后是否删除锁
     * @return
     */
    boolean after() default true;

    /**
     * 最大等待时间
     * @return
     */
    long waitTime() default 0L;

    /**
     * 尝试间隔时间
     * @return
     */
    long inTime() default 0L;
}
