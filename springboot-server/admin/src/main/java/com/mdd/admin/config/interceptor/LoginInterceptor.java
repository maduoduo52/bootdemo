package com.mdd.admin.config.interceptor;

import com.alibaba.fastjson.JSON;
import com.mdd.admin.config.*;
import com.mdd.admin.config.constant.LoginConstant;
import com.mdd.admin.config.constant.NotInterceptor;
import com.mdd.admin.config.exception.NeedLoginException;
import com.mdd.admin.config.redis.RedisTemplateUtils;
import com.mdd.admin.model.SysUserEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.weaver.ast.Not;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

/**
 * @Desc:   登录拦截
 * @Author Maduo
 * @Create 2018/12/1 17:20
 */
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {

    private RedisTemplateUtils redisTemplateUtils;

    private LoginConfig loginConfig;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(redisTemplateUtils==null){
            this.redisTemplateUtils = SpringUtil.getBean(RedisTemplateUtils.class);
        }
        if(loginConfig==null){
            this.loginConfig = SpringUtil.getBean(LoginConfig.class);
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        //反射获取注解信息
        IgnoreLogin ignoreLogin = method.getAnnotation(IgnoreLogin.class);
        if (ignoreLogin != null) {
            return true;
        }
        Cookie[] cookies = request.getCookies();
        if (cookies == null || cookies.length == 0) {
            throw new NeedLoginException("登录信息失效！！");
        }

        Cookie lc = null;
        for (Cookie cookie : cookies) {
            if (LoginConstant.LOGIN_COOKIE.equals(cookie.getName())) {
                lc = cookie;
            }
        }
        if (lc == null) {
            throw new NeedLoginException("登录信息失效！！");
        }
        String cookieVal = lc.getValue();
        if (StringUtils.isEmpty(cookieVal)) {
            throw new NeedLoginException("cookie异常");
        }
        String value = AESCodeUtil.decryptAES(cookieVal, loginConfig.aesKey);
        if(org.springframework.util.StringUtils.isEmpty(value)){
            throw new NeedLoginException("cookie值非法");
        }
        Map<String, Object> map = JSON.parseObject(value);
        if (map.isEmpty() || map == null) {
            throw new NeedLoginException("cookie异常");
        }
        String userId = (String) map.get("userId");
        String token = (String) map.get("token");
        String key = LoginConstant.LOGIN_KEY + userId + "." + token;
        String userInfo = redisTemplateUtils.get(key, String.class);
        if (StringUtils.isEmpty(userInfo)) {
            throw new NeedLoginException("登录信息已过期");
        }

        //登录拦截通过
        SysUserEntity userEntity = JSON.parseObject(userInfo, SysUserEntity.class);
        HeaderDto headerDto = HeaderDto.initEmp(Long.parseLong((String) map.get("postId")), userEntity);
        //token信息放入header
        headerDto.setToken(token);
        //信息存入本地线程变量
        Constant.set(headerDto);
        //cookie 的生存时间
        lc.setMaxAge(Math.toIntExact(loginConfig.time));
        //cookie 放入response
        response.addCookie(lc);

        //权限检测
        if (userEntity.getAdmin() == null || !userEntity.getAdmin()) {
            String uri = request.getRequestURI();
            if (NotInterceptor.INDEX.equals(uri) || NotInterceptor.LOGOUT.equals(uri)) {
                //主页和登出不需要权限校验
                return true;
            }

            String authKey = LoginConstant.USER_AUTH_KEY + Constant.getEmpId() + "." + Constant.get().getPostId();
            //获取redis用户权限数据
            String authData = redisTemplateUtils.get(authKey, String.class);
            Set<String> authSet = JSON.parseObject(authData, Set.class);
            if (authSet == null || authSet.isEmpty()) {
                throw new RuntimeException("您无权操作：" + uri);
            }
            if (authSet.contains(uri)) {
                redisTemplateUtils.set(authKey, authData, loginConfig.time);
            }else {
                throw new RuntimeException("您无权操作：" + uri);
            }
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        Constant.remove();
    }
}
