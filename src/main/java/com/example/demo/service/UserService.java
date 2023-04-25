package com.example.demo.service;

import com.example.demo.entity.User;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public interface UserService {
    /**
     * 获取用户权限
     * @param userId 用户id
     * @return
     */
    public Collection<? extends GrantedAuthority> getAuthorities(Long userId);


    User findUserByUserId(Integer userId);

    User findUserByUserName(String userName);
}
