package com.example.demo.config;

import com.example.demo.Utils.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Configuration
@Slf4j
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    public void configure(WebSecurity web) throws Exception {
        // 忽略静态资源
        web.ignoring().antMatchers("/resources/**");
    }

    // 鉴权
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(
                        "/manage",
                        "/user",
                        "/proposal"
                )
                .hasAnyAuthority("管理人员权限")
                .antMatchers(
                        "/hello"
                )
                .hasAnyAuthority("主管")
                .anyRequest()
                .permitAll()
                .and().csrf().disable(); // 不开启csrf

        http.exceptionHandling()
//                .authenticationEntryPoint(new AuthenticationEntryPoint() {
//                    // 未登录时的处理
//                    @Override
//                    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
//                        String xRequestedWith = httpServletRequest.getHeader("x-requested-with");
//                        // 未登录时，如果是异步请求，那么返回JSON字符串提示前端。
//                        if ("XMLHttpRequest".equals(xRequestedWith)) { // 异步请求
//                            httpServletResponse.setContentType("application/plain;charset=utf-8");
//                            PrintWriter writer = httpServletResponse.getWriter();
//                            writer.write(CommonUtil.getJSONString(403, "您还没有登录！"));
//                        }
//                        else {
//                            // 如果访问的是普通请求，那么重定向到登陆页面
//                            logger.info("未登录!");
//                            httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + "/user/login");
//                        }
//                    }
//                })
                .accessDeniedHandler(new AccessDeniedHandler() {
                    // 权限不足时的处理
                    @Override
                    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
                        String xRequestedWith = httpServletRequest.getHeader("x-requested-with");
                        // 权限不足时，如果是异步请求，那么返回JSON字符串提示前端。
                        if ("XMLHttpRequest".equals(xRequestedWith)) { // 异步请求
                            httpServletResponse.setContentType("application/plain;charset=utf-8");
                            PrintWriter writer = httpServletResponse.getWriter();
                            writer.write(CommonUtil.getJSONString(403, "您还没有访问此功能的权限！"));
                        }
                        else {
                            // 如果访问的是普通请求，那么重定向到权限不足页面
                            httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + "/denied");
                        }
                    }
                });
        // Security底层默认会拦截"/logout"请求进行退出处理
        // 我们需要覆盖默认的退出逻辑，才能执行自己的退出逻辑
        http.logout().logoutUrl("/user/logout"); // 骗过security，执行我们自己写的/logout请求
    }
}
