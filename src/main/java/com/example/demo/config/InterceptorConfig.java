package com.example.demo.config;

import com.example.demo.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    /** 传入待配置的拦截器*/
    private LoginInterceptor loginInterceptor;

    // 注入构造方法
    public InterceptorConfig(LoginInterceptor loginInterceptor){
        this.loginInterceptor = loginInterceptor;
    }

    // 添加不需要拦截的类
    @Override
    public void addInterceptors(InterceptorRegistry registry){
        List<String> excludePath = new ArrayList<>();
        excludePath.add("/user/login"); //登录
        excludePath.add("/user/logout"); //登出
        excludePath.add("/assets/**");  //静态资源
        excludePath.add("/file/**");

        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(excludePath);
        WebMvcConfigurer.super.addInterceptors(registry);
    }

    /**
     * 跨域支持
     *
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedHeaders("*")
                .allowCredentials(true)
                .allowedMethods("GET", "POST", "DELETE", "PUT", "PATCH", "OPTIONS", "HEAD")
                .maxAge(3600 * 24);
    }

}
