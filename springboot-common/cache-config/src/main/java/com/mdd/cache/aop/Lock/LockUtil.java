package com.mdd.cache.aop.Lock;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.concurrent.TimeUnit;

/**
 * @Desc:
 * @Author Maduo
 * @Create 2018/11/25 14:06
 */
@Component
@Slf4j
public class LockUtil {

    @Autowired
    @Qualifier("cacheRedisTemplate20180710")
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 加锁
     *
     * @param key  加锁的表示 唯一
     * @param time 加锁的时间  单位为毫秒
     * @return
     */
    public boolean lock(String key, long time) {
        boolean flag = false;
        try {
            flag = stringRedisTemplate.opsForValue().setIfAbsent(key, System.currentTimeMillis() + "");
        } catch (Exception e) {
            log.error("加锁异常:" + e);
        } finally {
            if (flag) {
                log.info("加锁完成:{}--{}", key, time);
                stringRedisTemplate.expire(key, time, TimeUnit.MILLISECONDS);
            }
        }
        return flag;
    }

    /**
     * 解锁
     *
     * @param key 加锁的表示 唯一
     */
    public void unLock(String key) {
        stringRedisTemplate.delete(key);
        log.warn("解锁完成:{}", key);
    }

    /**
     * 获取加锁的时间戳 毫秒   为空时无锁  不为空时有锁
     *
     * @param key
     * @return
     */
    public Long getLockTime(String key) {
        String value = stringRedisTemplate.opsForValue().get(key);
        if (StringUtils.isEmpty(value)) {
            return null;
        }
        Long time = Long.parseLong(value);
        return time;
    }


    /**
     * 判断锁是否存在
     *
     * @param key 加锁的表示 唯一
     * @return
     */
    public boolean isLock(String key) {
        Long time = getLockTime(key);
        if (time == null) {
            return false;
        }
        return true;
    }

    /**
     * 尝试获取锁
     *
     * @param key      锁的key值
     * @param time     加锁时间 单位毫秒
     * @param waitTime 最大等待时间 单位毫秒
     * @return
     */
    public boolean tryLock(String key, long time, long waitTime) {
        //获取系统当前时间
        long nowTime = System.currentTimeMillis();
        boolean flag = false;
        try {
            while (!flag) {
                if (System.currentTimeMillis() - nowTime > waitTime) {
                    throw new LockException("超过最大获取时间！");
                }
                flag = lock(key, time);
            }
        } catch (Exception e) {
            log.error("tryLock Exception " + e);
        } finally {
            return flag;
        }
    }

    /**
     * 尝试获取锁
     *
     * @param key      锁的key值
     * @param time     加锁时间 单位毫秒
     * @param waitTime 最大等待时间 单位毫秒
     * @param inTime   尝试间隔时间
     * @return
     */
    public boolean tryLock(String key, long time, long waitTime, long inTime) {
        //获取系统当前时间
        long nowTime = System.currentTimeMillis();
        boolean flag = false;
        try {
            while (!flag) {
                if (System.currentTimeMillis() - nowTime > waitTime) {
                    throw new LockException("超过最大获取时间！");
                }
                flag = lock(key, time);
                log.warn("休眠:{},{},{}", key, waitTime, inTime);
                Thread.sleep(inTime);
            }
        } catch (Exception e) {
            log.error("tryLock Exception " + e);
        } finally {
            return flag;
        }
    }
}
