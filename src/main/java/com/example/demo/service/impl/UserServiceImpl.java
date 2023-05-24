package com.example.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.demo.Utils.CommonUtil;
import com.example.demo.Utils.HostHolder;
import com.example.demo.common.CommonResult;
import com.example.demo.entity.*;
import com.example.demo.response.RoleVo;
import com.example.demo.mapper.*;
import com.example.demo.request.AddRoleRequest;
import com.example.demo.request.AddUserRequest;
import com.example.demo.request.UpdateUserRequest;
import com.example.demo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private DepartmentMapper departmentMapper;

    @Resource
    private PermissionMapper permissionMapper;

    @Resource
    private HostHolder hostHolder;

    @Resource
    private RolePermissionMapper rolePermissionMapper;

    @Resource
    private UserRoleMapper userRoleMapper;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(Long userId) {
//        List<GrantedAuthority> list = new ArrayList<>();
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        // 查找用户有多少个角色
        List<UserRole> userRoleList = userRoleMapper.selectList(new QueryWrapper<UserRole>().eq("user_id", userId));
        for (UserRole ur : userRoleList) {
            // 查询每个角色的权限
            List<Permission> permissionList = this.findPermissionByRoleId(ur.getRoleId());
            for (Permission permission : permissionList) {
                authorities.add(new SimpleGrantedAuthority(permission.getPermissionName()));
            }
        }
//        list.add(new GrantedAuthority() {
//            @Override
//            public String getAuthority() {
//                return roleMapper.selectById(userId).getRoleName();
//            }
//        });
        return authorities;
    }

    @Override
    public User findUserByUserId(Long userId) {
        return userMapper.selectOne(new QueryWrapper<User>().eq("id", userId).eq("del_flag", "N"));
}

    @Override
    public User findUserByUserName(String userName) {
        return userMapper.selectOne(new QueryWrapper<User>().eq("username", userName).eq("del_flag", "N"));
    }

    @Override
    public Role findRoleByRoleName(String roleName) {
        return roleMapper.selectOne(new QueryWrapper<Role>().eq("role_name", roleName));
    }

    @Override
    public User findUserByUserCode(String userCode) {
        return userMapper.selectOne(new QueryWrapper<User>().eq("usercode", userCode).eq("del_flag", "N"));
    }

    @Override
    public Role findRoleByUserId(Long userId) {
        UserRole userRole = userRoleMapper.selectOne(new QueryWrapper<UserRole>().eq("user_id", userId));
        return roleMapper.selectById(userRole.getRoleId());
    }

    @Override
    public List<Permission> findPermissionByRoleId(Long roleId) {
        List<RolePermission> rolePermissionList = rolePermissionMapper.selectList(new QueryWrapper<RolePermission>().eq("role_id", roleId));
        List<Permission> list = new ArrayList<>();
        for (RolePermission rp : rolePermissionList) {
            list.add(permissionMapper.selectById(rp.getPermissionId()));
        }
        return list;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public CommonResult addUser(AddUserRequest request) throws Exception {
        try {
            User user = null;
            user = userMapper.selectOne(new QueryWrapper<User>().eq("user_code", request.getUserCode()));
            if (null != user) {
                return CommonResult.failed("用户已存在!");
            }
            user = new User();
            user.setUserName(request.getUserName());
            user.setUserCode(request.getUserCode());
            // 获取部门id
            Department department = departmentMapper.selectOne(new QueryWrapper<Department>().eq("department_name", request.getDepartmentName()));
            user.setDepartmentId(department.getId());
            user.setPassword(request.getPassword());
            user.setPhone(request.getPhone());
            userMapper.insert(user);
            for (String roleName : request.getRoleName()) {
                UserRole userRole = new UserRole();
                // 获取角色id
                Role role = roleMapper.selectOne(new QueryWrapper<Role>().eq("role_name", roleName));
                userRole.setRoleId(role.getId());
                userRole.setUserId(userRole.getUserId());
                userRole.setCreateUser(hostHolder.getUser().getId());
                userRoleMapper.insert(userRole);
            }
            return CommonResult.success(null, "新增用户成功");
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public CommonResult updateUser(UpdateUserRequest request) throws Exception {
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        try {
            User user = this.findUserByUserCode(request.getUserCode());
            updateWrapper.eq("usercode", request.getUserCode());
            if (!StringUtils.isBlank(request.getUserName())) {
                user.setUserName(request.getUserName());
            }
            if (StringUtils.isNotBlank(request.getPassword())) {
                user.setUserName(request.getPassword());
            }
            if (StringUtils.isNotBlank(request.getRoleName())) {
                user.setRoleId(this.findRoleByRoleName(request.getRoleName()).getId());
            }
            if (StringUtils.isNotBlank(request.getPhone())) {
                user.setPhone(request.getPhone());
            }
            userMapper.update(user, updateWrapper);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw new Exception(e.getMessage());
        }

        return CommonResult.success(null, "用户信息修改成功!");
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public CommonResult deleteUser(String userCode) throws Exception{
        try {
            UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
            User user = this.findUserByUserCode(userCode);
            updateWrapper.eq("usercode", userCode);
            user.setDelFlag("Y");
            userMapper.update(user, updateWrapper);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw new Exception(e.getMessage());
        }
        return CommonResult.success(null, "删除成功!");
    }

    @Override
    public CommonResult getUser(String userName) throws Exception {
        try {
            User user = this.findUserByUserName(userName);
            if (null == user) {
                return CommonResult.failed("用户不存在");
            }
            return CommonResult.success(user, "查找成功");
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public CommonResult addRole(AddRoleRequest request) throws Exception {
        try {
            // 校验重复
            Role role = roleMapper.selectOne(new QueryWrapper<Role>().eq("role_name", request.getRoleName()));
            Permission permission = permissionMapper.selectOne(new QueryWrapper<Permission>().eq("permission_name", request.getPermissionName()));
            if (null != role) {
                return CommonResult.failed("该角色已存在");
            }
            if (null != permission) {
                return CommonResult.failed("该权限已存在");
            }
            role = new Role();
            role.setRoleCode(request.getRoleCode());
            role.setRoleName(request.getRoleName());
            roleMapper.insert(role);
            permission = new Permission();
            permission.setCreateUser(hostHolder.getUser().getId());
            permission.setPermissionName(request.getPermissionName());
            permission.setPermissionCode(CommonUtil.generatePermissionCode());
            permission.setCreateTime(new Timestamp(System.currentTimeMillis()));
            permission.setDelFlag("N");
            permissionMapper.insert(permission);
            // role_permission表中添加映射记录
            RolePermission rolePermission = new RolePermission();
            rolePermission.setRoleId(role.getId());
            rolePermission.setPermissionId(permission.getId());
            rolePermission.setCreateUser(hostHolder.getUser().getId());
            rolePermission.setCreateTime(new Timestamp(System.currentTimeMillis()));
            rolePermission.setDelFlag("N");
            rolePermissionMapper.insert(rolePermission);
            return CommonResult.success(null, "添加角色成功");
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public CommonResult getRole(String roleName) throws Exception {
        try {
            Role role = this.findRoleByRoleName(roleName);
            if (null == role) {
                return CommonResult.failed("该角色不存在");
            }
            // 查询角色对应的权限
            List<Permission> permissionList = this.findPermissionByRoleId(role.getId());
            return CommonResult.success(new RoleVo(role.getRoleName(),
                    permissionList.stream()
                            .map(Permission::getPermissionName)
                            .collect(Collectors.toList())), "查找成功");
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public CommonResult deleteRole(String roleName) throws Exception {
        try {
            UpdateWrapper<Role> updateWrapper = new UpdateWrapper<>();
            Role role = this.findRoleByRoleName(roleName);
            updateWrapper.eq("roleName", roleName);
            role.setDelFlag("Y");
            roleMapper.update(role, updateWrapper);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw new Exception(e.getMessage());
        }
        return CommonResult.success(null, "删除成功!");
    }

    @Override
    public List<User> findUserListByIds(List<Long> userIds) {
        return userMapper.selectBatchIds(userIds);
    }
}
