package com.example.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.demo.entity.User;
import com.example.demo.mapper.RoleMapper;
import com.example.demo.mapper.UserMapper;
import com.example.demo.service.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private RoleMapper roleMapper;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(Integer userId) {
        List<GrantedAuthority> list = new ArrayList<>();
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        list.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return roleMapper.selectById(userId).getF_RoleName();
            }
        });
        return list;
    }

    @Override
    public User findUserByUserId(Integer userId) {
        return userMapper.selectById(userId);
    }

    @Override
    public User findUserByUserName(String userName) {
        return userMapper.selectOne(new QueryWrapper<User>().eq("F_UserName", userName));
    }
}
