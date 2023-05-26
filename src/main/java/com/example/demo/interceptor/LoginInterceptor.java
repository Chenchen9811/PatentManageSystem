package com.example.demo.interceptor;

import com.example.demo.Utils.HostHolder;
import com.example.demo.Utils.TokenUtil;
import com.example.demo.common.CommonResult;
import com.example.demo.common.ResultCode;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LoginInterceptor implements HandlerInterceptor {
    @Resource
    private HostHolder hostHolder;

    @Resource
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 如果不是映射到方法，直接通过
//        if (!(handler instanceof HandlerMethod)) {
//            return true;
//        }
        String userId = request.getHeader("Userid");
        String token = request.getHeader("Token");
        String authorization = request.getHeader("Authorization");

        // token不为空时验证
        if (!StringUtils.isBlank(token)) {
            boolean isValidToken = false;
            try {
                isValidToken = TokenUtil.verify(token);
            } catch (Exception e) {
                // 第二步改变返回状态码为401
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return false;
            }

            if (isValidToken) {
                User user = userService.findUserByUserId(Long.valueOf(userId));
                hostHolder.setUser(user);
                // 构建用户认证结果并存入SecurityContext，以便于Security进行授权
//                Authentication authentication = new UsernamePasswordAuthenticationToken(
//                        sysUser, sysUser.getPassword(), userService.getAuthorities(sysUser.getUserId())
//                );
//                SecurityContextHolder.setContext(new SecurityContextImpl(authentication));
                return true;
            }
            else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                CommonResult.unauthorized(ResultCode.UNAUTHORIZED.getMessage());
                response.sendRedirect(request.getContextPath() + "/user/login");
            }
        }
//        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        CommonResult.unauthorized(ResultCode.UNAUTHORIZED.getMessage());
//        response.sendRedirect(request.getContextPath() + "/user/login");
        return false;
    }
}
