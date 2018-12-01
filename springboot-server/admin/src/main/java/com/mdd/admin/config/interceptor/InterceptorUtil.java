package com.mdd.admin.config.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;

import java.util.LinkedList;
import java.util.List;

/**
 * @Desc:
 * @Author Maduo
 * @Create 2018/12/1 17:06
 */
public class InterceptorUtil {

    /**
     * 私有化构造函数
     */
    private InterceptorUtil(){}
    /**
     * 拦截器列表
     */
    private static List<HandlerInterceptor> list = new LinkedList<>();
    /**
     * 拦截器规则
     */
    private static List<String> PATHPATTERNS_LIST = new LinkedList<>();

    /**
     * 添加拦截器
     * @param interceptor
     * @return
     */
    public static void addInterceptor(HandlerInterceptor interceptor, String pathPatterns) {
        list.add(interceptor);
        PATHPATTERNS_LIST.add(pathPatterns);
    }

    /**
     * 获取拦截器
     * @return
     */
    public static List<HandlerInterceptor> get(){
        return list;
    }

    /**
     * 获取拦截器规则
     * @param i
     * @return
     */
    public static String getpathPatterns(int i){
        return PATHPATTERNS_LIST.get(i);
    }
}
