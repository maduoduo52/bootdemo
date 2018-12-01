package com.mdd.admin.config.exception;

import com.alibaba.fastjson.JSON;
import com.mdd.admin.config.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @Desc:
 * @Author Maduo
 * @Create 2018/12/1 16:41
 */
@ControllerAdvice
@Slf4j
public class MddExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ModelAndView defaultErrorHandler(Exception e, HttpServletRequest request) throws Exception{
        ModelAndView mav = new ModelAndView();
        mav.setViewName("500");
        Result result = null;
        log.error("异常信息:", e);
        if (e instanceof NeedLoginException) {
            result =  Result.error("需要登录");
            result.setCode(10001);
            mav.setViewName("needLogin");
        }else if (e instanceof ValidationException) {
            if(e instanceof ConstraintViolationException){
                ConstraintViolationException exc= (ConstraintViolationException) e;
                Set<ConstraintViolation<?>> set = exc.getConstraintViolations();
                Map<String,Object> checkMap = new HashMap<>();
                for (ConstraintViolation<?> constraintViolation : set) {
                    checkMap.put(constraintViolation.getPropertyPath().toString(), constraintViolation.getMessageTemplate());
                }
                result =  Result.validationError(JSON.toJSONString(checkMap));
                result.setData("");
            }
            result = Result.validationError(e.getMessage());
            result.setData("");
        }else {
            result = Result.error(e.getMessage());
            result.setData("");
        }
        if(isAjax(request)){
            MappingJackson2JsonView view = new MappingJackson2JsonView();
            view.setAttributesMap(JSON.parseObject(JSON.toJSONString(result),Map.class));
            mav.setView(view);
        }

        mav.addObject("exMsg", getStackTrace(e));
        return mav;
    }

    /**
     * 判断是不是ajax请求
     * @param request
     * @return
     */
    public boolean isAjax(HttpServletRequest request){
        String requestType = request.getHeader("X-Requested-With");
        if("XMLHttpRequest".equals(requestType)){
            return true;
        }else{
            return false;
        }
    }

    public static String getStackTrace(Throwable t) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        try {
            t.printStackTrace(pw);
            return sw.toString();
        } finally {
            pw.close();
        }
    }
}
