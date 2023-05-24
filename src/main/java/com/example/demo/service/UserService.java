package com.example.demo.service;

import com.example.demo.common.CommonResult;
import com.example.demo.entity.Permission;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.request.AddRoleRequest;
import com.example.demo.request.AddUserRequest;
import com.example.demo.request.UpdateUserRequest;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;

public interface UserService {
    /**
     * 获取用户权限
     * @param userId 用户id
     * @return
     */
    public Collection<? extends GrantedAuthority> getAuthorities(Long userId);


    User findUserByUserId(Long userId);

    User findUserByUserName(String userName);

    User findUserByUserCode(String userCode);

    Role findRoleByRoleName(String roleName);

    Role findRoleByUserId(Long userId);

    List<Permission> findPermissionByRoleId(Long roleId);

    List<User> findUserListByIds(List<Long> userIds);

    CommonResult addUser(AddUserRequest request) throws Exception;

    CommonResult updateUser(UpdateUserRequest result) throws Exception;

    CommonResult deleteUser(String userCode) throws Exception;

    CommonResult addRole(AddRoleRequest request) throws Exception;

    CommonResult getUser(String userName) throws Exception;

    CommonResult getRole(String roleName) throws Exception;

    CommonResult deleteRole(String roleName) throws Exception;
}
