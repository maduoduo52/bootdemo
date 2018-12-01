package com.mdd.cache.aop.Lock;

import javassist.*;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Objects;
import java.util.TreeMap;

/**
 * @Desc:
 * @Author Maduo
 * @Create 2018/11/25 14:05
 */
@Aspect
@Component
@Order(1)
@Slf4j
public class LockAspect {

    @Value("${spring.application.name: NONE-LOCK}")
    private String appName;

    @Autowired
    private LockUtil lockUtil;

    /**
     * 执行之间   加锁
     *
     * @param pjp
     * @param lock
     */
    @Around("@annotation(lock)")
    public Object lock(ProceedingJoinPoint pjp, Lock lock) throws Throwable {
        if ("NONE-LOCK".equals(appName)) {
            throw new LockException("未获取到appName不执行,{} 加锁失败");
        }
        String key = getKey(pjp, lock);
        boolean flag;
        //获取最大间隔时间
        long waitTime = lock.waitTime();
        //获取最大等待时间
        long inTime = lock.inTime();
        if (waitTime > 0) {
            if (inTime > 0) {
                flag = lockUtil.tryLock(key, lock.time(), waitTime, inTime);
            } else {
                flag = lockUtil.tryLock(key, lock.time(), waitTime);
            }
        } else {
            flag = lockUtil.lock(key, lock.time());
        }
        if (!flag) {
            log.warn("{} 锁 已经存在 ", key);
            throw new LockException("此方法已被锁定:" + key);
        }
        try {
            Object o = pjp.proceed();
            if (lock.after()) {
                lockUtil.unLock(key);
                log.warn("执行完成:删除锁:{}", key);
            }
            return o;
        } catch (Throwable e) {
            log.error("", e);
            if (lock.afterThrowing()) {
                lockUtil.unLock(key);
                log.warn("执行异常:删除锁:{}", key);
            }
            throw e;
        }
    }


    /**
     * 获取key
     *
     * @param pjp
     * @param lock
     * @return
     */
    private String getKey(JoinPoint pjp, Lock lock) throws Exception {
        //Signature 包含了方法名、申明类型以及地址等信息
        String class_name = pjp.getTarget().getClass().getName();
        String method_name = pjp.getSignature().getName();
        StringBuilder builder = new StringBuilder(100);
        builder.append(appName).append(":").append(class_name).append(".").append(method_name);
        //获取需要缓存的参数
        String[] cacheParams = lock.params();
        if (cacheParams != null && cacheParams.length > 0) {
            builder.append(":");
            //方法的参数
            String[] paramNames = getFieldsName(class_name, method_name);
            Object objs[] = pjp.getArgs();
            if (paramNames != null && paramNames.length > 0) {
                for (String cacheParam : cacheParams) {
                    for (int i = 0; i < paramNames.length; i++) {
                        if (cacheParam.equals(paramNames[i])) {
                            builder.append(Objects.hashCode(objs[i])).append(".");
                            break;
                        }
                    }
                }
            }
        }
        return builder.toString();
    }


    /**
     * 使用javassist来获取方法参数名称
     *
     * @param class_name  类名
     * @param method_name 方法名
     * @return
     * @throws Exception
     */
    private String[] getFieldsName(String class_name, String method_name) throws Exception {
        Class<?> clazz = Class.forName(class_name);
        String clazz_name = clazz.getName();
        ClassPool pool = ClassPool.getDefault();
        ClassClassPath classPath = new ClassClassPath(clazz);
        pool.insertClassPath(classPath);

        CtClass ctClass = pool.get(clazz_name);
        CtMethod ctMethod = ctClass.getDeclaredMethod(method_name);
        MethodInfo methodInfo = ctMethod.getMethodInfo();
        CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
        LocalVariableAttribute attr = (LocalVariableAttribute) codeAttribute.getAttribute(LocalVariableAttribute.tag);
        if (attr == null) {
            return null;
        }
        String[] paramsArgsName = new String[ctMethod.getParameterTypes().length];
        TreeMap<Integer, String> sortMap = new TreeMap();
        for (int i = 0; i < attr.tableLength(); i++) {
            sortMap.put(attr.index(i), attr.variableName(i));
        }
        int pos = Modifier.isStatic(ctMethod.getModifiers()) ? 0 : 1;
        paramsArgsName = Arrays.copyOfRange(sortMap.values().toArray(new String[0]), pos, paramsArgsName.length + pos);
        return paramsArgsName;
    }
}
