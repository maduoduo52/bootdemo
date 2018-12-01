package com.mdd.cache.util;

import com.alibaba.fastjson.JSON;
import com.mdd.cache.config.CacheConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @Desc:   缓存工具类
 * @Author Maduo
 * @Create 2018/11/24 22:48
 */
@Component
public class CacheUtil {

    /**
     * applicationName
     */
    @Value("${spring.application.name}")
    private String applicationName;

    /**
     * active
     */
    @Value("${spring.profiles.active}")
    private String active;

    public void check() {
        //判断app_name 有没有设置
        if(CacheConstant.APP_NAME.equals("NONE")){
            CacheConstant.APP_NAME = applicationName;
            CacheConstant.REDIS_PREFIX += applicationName+":"+active+":";
        }
    }

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 移除缓存
     * @param key
     */
    public void remove(String key){
        check();
        stringRedisTemplate.delete(CacheConstant.REDIS_PREFIX+key);
    }

    /**
     * 移除全部缓存
     */
    public void  removeAll(){
        check();
        stringRedisTemplate.delete(CacheConstant.REDIS_PREFIX+"*");
    }

    /**
     * 添加缓存
     * @param key
     * @param value
     * @param time
     */
    public void put(String key,Object value,long time){
        check();
        String cacheStr = null;
        if(value!=null){
            if(value instanceof String){
                cacheStr  = (String) value;
            }else{
                cacheStr = JSON.toJSONString(cacheStr);
            }
        }
        stringRedisTemplate.opsForValue().set(key, cacheStr, time, TimeUnit.SECONDS);
    }

    /**
     * 获取缓存
     * @param key
     * @param clazz
     * @return
     */
    public  <T> T get(String key,Class<T> clazz){
        check();
        String cacheStr = stringRedisTemplate.opsForValue().get(CacheConstant.REDIS_PREFIX+key);
        if(StringUtils.isEmpty(cacheStr)){
            return null;
        }
        return JSON.parseObject(cacheStr,clazz);
    }

    /**
     * 获取缓存 返回string
     * @param key
     * @return
     */
    public String get(String key){
        check();
        String cacheStr = stringRedisTemplate.opsForValue().get(CacheConstant.REDIS_PREFIX+key);
        return cacheStr;
    }

    /**
     *  获取所有的缓存key
     * @return
     */
    public Set<String> getAllKey(){
        return stringRedisTemplate.keys(CacheConstant.REDIS_PREFIX + "*");
    }

    /**
     * 获取所有的 缓存数据
     * @return
     */
    public Map<String,String> getAll(){
        check();
        Set<String> set = getAllKey();
        if(set!=null && set.size()>0){
            Map<String,String> map = new HashMap<>();
            for (String key : set) {
                String cacheStr = stringRedisTemplate.opsForValue().get(key);
                map.put(key,cacheStr);
            }
            return map;
        }
        return null;
    }
}
