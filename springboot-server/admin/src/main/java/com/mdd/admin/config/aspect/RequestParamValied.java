package com.mdd.admin.config.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import javax.validation.executable.ExecutableValidator;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;


/**
 * @Desc:
 * @Author Maduo
 * @Create 2018/12/1 22:37
 */
@Aspect
@Component
@Slf4j
public class RequestParamValied {

    /**
     * 指定切点
     */
    @Pointcut("execution(* com.mdd.admin.controller..*.*(..))")
    public void controllerBefore() {
    }

    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final ExecutableValidator validator = factory.getValidator().forExecutables();

    private <T> Set<ConstraintViolation<T>> validMethodParams(T obj, Method method, Object[] params) {
        return validator.validateParameters(obj, method, params);
    }

    @Before("controllerBefore()")
    public void before(JoinPoint point) throws RuntimeException {
        log.info("========>参数校验开始");
        //  获得切入目标对象
        Object target = point.getThis();
        //  获得切入目标参数
        Object[] args = point.getArgs();
        //  获得切入方法
        Method method = ((MethodSignature) point.getSignature()).getMethod();
        // 执行校验，获得校验结果
        Set<ConstraintViolation<Object>> validResult = validMethodParams(target, method, args);
        //如果有校验不通过的
        if (!validResult.isEmpty()) {
            List<String> messageList = new LinkedList<>();
            //获取提示信息
            for (ConstraintViolation<Object> constraintViolation : validResult) {
                messageList.add(constraintViolation.getMessage());
            }
            log.info("========>参数校验异常：{}" , messageList);
            throw new RuntimeException(messageList.toString());
        }
        log.info("========>参数校验结束");
    }
}
