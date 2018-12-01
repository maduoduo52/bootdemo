package com.mdd.cache.config;

/**
 * @Desc:
 * @Author Maduo
 * @Create 2018/11/24 22:47
 */
public class CacheConstant {

    /**
     * redis 缓存前缀
     */
    public static String REDIS_PREFIX="com.mdd.demo.cache.data:";

    /**
     * 项目前缀  防止多个项目使用同一个redis产生key值影响
     */
    public static String APP_NAME="NONE";
}
