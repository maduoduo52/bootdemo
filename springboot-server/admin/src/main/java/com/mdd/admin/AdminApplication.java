package com.mdd.admin;

import com.mdd.admin.config.interceptor.InterceptorUtil;
import com.mdd.admin.config.interceptor.LoginInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AdminApplication {

    public static void main(String[] args) {
        InterceptorUtil.addInterceptor(new LoginInterceptor(), "/**/**.html");
        SpringApplication.run(AdminApplication.class, args);
    }
}
