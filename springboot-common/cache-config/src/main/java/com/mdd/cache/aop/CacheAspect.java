package com.mdd.cache.aop;

import com.alibaba.fastjson.JSON;
import com.mdd.cache.config.CacheConstant;
import javassist.*;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Objects;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

/**
 * @Desc:
 * @Author Maduo
 * @Create 2018/11/25 12:02
 */
@Component
@Aspect
@Order(50)
@Slf4j
public class CacheAspect {

    @Autowired
    @Qualifier("cacheRedisTemplate20180710")
    private StringRedisTemplate stringRedisTemplate;

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

    @Around("@annotation(insertCache)")
    public Object RequestLog(ProceedingJoinPoint pjp, InsertCache insertCache) throws Throwable {
        //获取返回的class
        Class<?> clazz = insertCache.clazz();
        //判断是否需要去自动识别class
        if (clazz.getSimpleName().equals(NoCacheClazz.class.getSimpleName())) {
            MethodInvocationProceedingJoinPoint methodPjp = (MethodInvocationProceedingJoinPoint) pjp;
            MethodSignature methodSignature = (MethodSignature) methodPjp.getSignature();
            //获取返回的calss
            clazz = methodSignature.getReturnType();
        }
        //判断app_name 有没有设置
        if (CacheConstant.APP_NAME.equals("NONE")) {
            CacheConstant.APP_NAME = applicationName;
            CacheConstant.REDIS_PREFIX += applicationName + ":" + active + ":";
        }
        /**
         * Signature 包含了方法名、申明类型以及地址等信息
         */
        String class_name = pjp.getTarget().getClass().getName();
        String method_name = pjp.getSignature().getName();

        //判断key是否存在
        String key = insertCache.key();
        if ("".equals(key)) {
            //如果不存在 生成key
            key = class_name + "." + method_name;
        }

        int time = insertCache.time();
        //获取需要缓存的参数
        String[] cacheParams = insertCache.params();
        if (cacheParams != null && cacheParams.length > 0) {
            String keyStr = ":";
            //方法的参数
            String[] paramNames = getFeildParams(class_name, method_name);
            Object objs[] = pjp.getArgs();
            if (paramNames != null && paramNames.length > 0) {
                for (String cacheParam : cacheParams) {
                    for (int i = 0; i < paramNames.length; i++) {
                        if (cacheParam.equals(paramNames[i])) {
                            keyStr += Objects.hashCode(objs[i]) + ".";
                            break;
                        }
                    }
                }
            }
            key += keyStr;
        }
        key = CacheConstant.REDIS_PREFIX + key;
        if (stringRedisTemplate.hasKey(key)) {
            log.info("从缓存中获取数据:{}.{} key:{}", class_name, method_name, key);
            String cacheStr = stringRedisTemplate.opsForValue().get(key);
            return JSON.parseObject(cacheStr, clazz);
        }
        Object o = pjp.proceed();
        //空值不进行缓存
        if (o == null) {
            return o;
        }
        String cacheStr = JSON.toJSONString(o);
        stringRedisTemplate.opsForValue().set(key, cacheStr, time, TimeUnit.SECONDS);
        log.warn("缓存数据:{}.{} key:{} time:{}  data:{}", class_name, method_name, key, time, cacheStr);
        return o;
    }

    /**
     * 重要日志切入点
     * @param pjp
     * @param removeCache
     * @return
     * @throws Throwable
     */
    @Around("@annotation(removeCache)")
    public Object removeCache(ProceedingJoinPoint pjp, RemoveCache removeCache) throws Throwable {
        String key = removeCache.key();
        boolean keyAll = removeCache.keyAll();
        key=CacheConstant.REDIS_PREFIX+key;
        if(keyAll){
            log.info("删除缓存数据:{}",key);
            stringRedisTemplate.delete(key);
        }else{
            /**
             * Signature 包含了方法名、申明类型以及地址等信息
             */
            String class_name = pjp.getTarget().getClass().getName();
            String method_name = pjp.getSignature().getName();
            //获取需要缓存的参数
            String [] cacheParams =  removeCache.params();
            if(cacheParams!=null &&  cacheParams.length>0) {
                String keyStr = ":";
                //方法的参数
                String[] paramNames = getFeildParams(class_name, method_name);
                Object objs[] = pjp.getArgs();
                if (paramNames != null && paramNames.length > 0) {
                    for (String cacheParam : cacheParams) {
                        for (int i = 0; i < paramNames.length; i++) {
                            if (cacheParam.equals(paramNames[i])) {
                                keyStr += Objects.hashCode(objs[i])+".";
                                break;
                            }
                        }
                    }
                }
                key += keyStr;
            }
            log.info("删除缓存数据:{}",key);
            stringRedisTemplate.delete(stringRedisTemplate.keys(key + "*"));
        }
        return  pjp.proceed();
    }

    /**
     * 使用javassist来获取方法参数名称
     *
     * @param className
     * @param methodName 方法名
     * @return
     * @throws Exception
     */
    private String[] getFeildParams(String className, String methodName) throws Exception {
        Class<?> clazz = Class.forName(className);
        String clazzName = clazz.getName();
        ClassPool pool = ClassPool.getDefault();
        ClassClassPath classPath = new ClassClassPath(clazz);
        pool.insertClassPath(classPath);

        CtClass ctClass = pool.get(clazzName);
        CtMethod ctMethod = ctClass.getDeclaredMethod(methodName);
        MethodInfo methodInfo = ctMethod.getMethodInfo();
        CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
        LocalVariableAttribute attr = (LocalVariableAttribute) codeAttribute.getAttribute(LocalVariableAttribute.tag);
        if (attr == null) {
            return null;
        }

        String[] paramsArgs = new String[ctMethod.getParameterTypes().length];
        TreeMap<Integer, String> sortMap = new TreeMap<>();
        for (int i = 0; i < attr.tableLength(); i++) {
            sortMap.put(attr.index(i), attr.variableName(i));
        }
        int pos = Modifier.isStatic(ctMethod.getModifiers()) ? 0 : 1;
        paramsArgs = Arrays.copyOfRange(sortMap.values().toArray(new String[0]), pos, paramsArgs.length + pos);
        return paramsArgs;
    }
}
