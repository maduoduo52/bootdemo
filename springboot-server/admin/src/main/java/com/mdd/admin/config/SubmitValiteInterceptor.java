package com.mdd.admin.config;

import com.alibaba.fastjson.JSON;
import com.mdd.admin.config.redis.RedisTemplateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * @author Maduo
 * @date 2019/11/28 18:33
 */
@Slf4j
public class SubmitValiteInterceptor extends HandlerInterceptorAdapter {

    private RedisTemplateUtils redisTemplateUtils;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (redisTemplateUtils == null) {
            this.redisTemplateUtils = SpringUtil.getBean(RedisTemplateUtils.class);
        }

        if (handler instanceof HandlerMethod) {

            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();

            SubmitValite annotation = method.getAnnotation(SubmitValite.class);

            if (annotation != null) {
                log.info("===> 重复提交校验开始");

                String key = Constant.get().getEmpId() + method.getName() + request.getRequestURI();
                //需要做重复校验
                boolean b = redisTemplateUtils.exists(key);

                log.info("===> redis是否存在数据：{}", b);
                if (b) {
                    return false;
                } else {
                    //请求数据保存3s
                    redisTemplateUtils.set(key, JSON.toJSONString(request.getParameterMap()), annotation.times());
                    return true;
                }
            } else {
                return true;
            }
        } else {
            return super.preHandle(request, response, handler);
        }
    }
}
