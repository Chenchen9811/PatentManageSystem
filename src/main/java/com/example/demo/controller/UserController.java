package com.example.demo.controller;

import com.example.demo.Utils.CommonUtil;
import com.example.demo.Utils.TokenUtil;
import com.example.demo.common.CommonResult;
import com.example.demo.common.Constants;
import com.example.demo.common.Message;
import com.example.demo.entity.User;
import com.example.demo.request.LoginRequest;
import com.example.demo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;

@RestController
@Slf4j
@RequestMapping("/user")
public class UserController implements Constants, Message {

    @Resource
    private UserService userService;

    @ResponseBody
    @PostMapping(path = "/login")
    public CommonResult login(@Valid @RequestBody LoginRequest loginRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.error(bindingResult.getFieldError().getDefaultMessage());
        }
        // 检索用户
        User user = userService.findUserByUserName(loginRequest.getUserName());
        if (ObjectUtils.isEmpty(user)) {
            return CommonResult.failed("不存在当前用户");
        }
        // 验证密码
        if (!user.getPassword().equals(loginRequest.getPassword())) {
            return CommonResult.failed("密码错误!");
        }
        HashMap<String, Object> map = new HashMap<>();
        // 生成TOKEN
        String token = TokenUtil.getToken(String.valueOf(user.getId()));
        map.put(TOKEN, token);
        map.put(USER_ID, String.valueOf(user.getId()));
        log.info("登录用户id:{}, 登录用户名:{}", String.valueOf(user.getId()), loginRequest.getUserName());
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                user, user.getPassword(), userService.getAuthorities(user.getId())
        );
        SecurityContextHolder.setContext(new SecurityContextImpl(authentication));
        return CommonResult.success(map, MSG_S_LOG_001);
    }
}
