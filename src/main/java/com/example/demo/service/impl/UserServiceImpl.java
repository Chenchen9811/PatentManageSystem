package com.example.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.demo.Utils.CommonUtil;
import com.example.demo.Utils.HostHolder;
import com.example.demo.common.CommonResult;
import com.example.demo.entity.*;
import com.example.demo.request.*;
import com.example.demo.response.GetUserResponse;
import com.example.demo.response.RoleVo;
import com.example.demo.mapper.*;
import com.example.demo.service.DepartmentService;
import com.example.demo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Resource
    private DepartmentService departmentService;

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
    public CommonResult getPermissionList() {
        try {
            List<Permission> permissionList = permissionMapper.selectList(new LambdaQueryWrapper<>());
            Map<String, List<String>> map = new HashMap<>();
            map.put("permissionList", permissionList.stream().map(Permission::getPermissionName).collect(Collectors.toList()));
            return CommonResult.success(map, "查找成功");
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public CommonResult getRoleList() {
        try {
            List<Role> roleList = roleMapper.selectList(new LambdaQueryWrapper<Role>().eq(Role::getDelFlag, "N"));
            Map<String, List<String>> map = new HashMap<>();
            map.put("roleNameList", roleList.stream().map(Role::getRoleName).collect(Collectors.toList()));
            return CommonResult.success(map, "查找成功");
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw e;
        }
    }

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
        return permissionMapper.selectBatchIds(rolePermissionMapper.selectList(
                new QueryWrapper<RolePermission>().eq("role_id", roleId).eq("del_flag", "N"))
                .stream().map(RolePermission::getPermissionId).collect(Collectors.toList()));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public CommonResult addUser(AddUserRequest request) throws Exception {
        try {
            User user = null;
            user = userMapper.selectOne(new QueryWrapper<User>().eq("usercode", request.getUserCode()));
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
//            userMapper.insert(user);

            UserRole userRole = new UserRole();
            // 获取角色id
            String roleName = request.getRoleName();
            Role role = roleMapper.selectOne(new QueryWrapper<Role>().eq("role_name", roleName));
            user.setRoleId(role.getId());
            user.setDelFlag("N");
            int insert = userMapper.insert(user);
            if (insert == 0) {
                CommonResult.failed("新增用户失败");
            }
            userRole.setRoleId(role.getId());
            userRole.setUserId(user.getId());
            userRole.setCreateUser(hostHolder.getUser().getId());
            userRole.setCreateTime(new Timestamp(System.currentTimeMillis()));
            return  userRoleMapper.insert(userRole) == 1 ?
                    CommonResult.success(null, "新增用户成功") : CommonResult.failed("新增用户失败");
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
    public CommonResult deleteUser(String userCode) throws Exception {
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
    public CommonResult getUser(GetUserRequest request) throws Exception {
        try {
//            User user = this.findUserByUserName(userName);
//            if (null == user) {
//                return CommonResult.failed("用户不存在");
//            }
            LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(User::getDelFlag, "N");
            List<Criteria.KV> items = request.getCriteria().getItems();
            for (Criteria.KV kv : items) {
                if (kv.getKey().equals("userName")) {
                    wrapper.eq(User::getUserName, kv.getValue());
                    break;
                }
            }
            List<User> userList = userMapper.selectList(wrapper);
            if (userList.size() == 0) {
                return CommonResult.failed("用户不存在");
            }
            List<Long> roleIds = userList.stream().map(User::getRoleId).distinct().collect(Collectors.toList());
            List<Long> departmentIds = userList.stream().map(User::getDepartmentId).distinct().collect(Collectors.toList());
            List<Department> departmentList = departmentService.getDepartmentListByIds(departmentIds);
            if (departmentList.size() == 0) {
                return CommonResult.failed("没有找到相关用户所对应的部门");
            }
            List<Role> roleList = roleMapper.selectBatchIds(roleIds);
            if (roleList.size() == 0) {
                return CommonResult.failed("没有找到相关用户所对应的角色");
            }
            Map<Long, Department> departmentMap = new HashMap<>();
            Map<Long, Role> roleMap = new HashMap<>();
            for (Department department : departmentList) {
                departmentMap.put(department.getId(), department);
            }
            for (Role role : roleList) {
                roleMap.put(role.getId(), role);
            }
            List<GetUserResponse> responseList = userList.stream().map(user -> {
                GetUserResponse response = new GetUserResponse();
                Department department = departmentMap.get(user.getDepartmentId());
                Role role = roleMap.get(user.getRoleId());
                response.setUserName(user.getUserName());
                response.setRoleName(role.getRoleName());
                response.setDepartmentName(department.getDepartmentName());
                response.setPhone(user.getPhone());
                response.setUserCode(user.getUserCode());
                return response;
            }).collect(Collectors.toList());
            Map<String, List<GetUserResponse>> map = new HashMap<>();
            map.put("list", responseList);
            return CommonResult.success(map, "查找成功");
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
            Role role = roleMapper.selectOne(new QueryWrapper<Role>().eq("role_name", request.getRoleName()).last("for update"));
            if (null != role) {
                return CommonResult.failed("该角色已存在");
            }
            role = new Role();
            role.setRoleCode(request.getRoleCode());
            role.setRoleName(request.getRoleName());
            role.setDelFlag("N");
            roleMapper.insert(role);
            // 给角色配置权限
            List<String> permissionNameList = request.getPermission();
            List<Permission> permissionList = permissionMapper.selectList(new LambdaQueryWrapper<Permission>().in(Permission::getPermissionName, permissionNameList));
            List<RolePermission> rolePermissionList = new ArrayList<>();
            for (Permission permission : permissionList) {
                RolePermission rolePermission = new RolePermission();
                rolePermission.setRoleId(role.getId());
                rolePermission.setPermissionId(permission.getId());
                rolePermission.setCreateUser(hostHolder.getUser().getId());
                rolePermission.setCreateTime(new Timestamp(System.currentTimeMillis()));
                rolePermission.setDelFlag("N");
                rolePermissionList.add(rolePermission);
            }
            return rolePermissionMapper.insertBatchSomeColumn(rolePermissionList) != 0?
                    CommonResult.success(null, "添加角色成功") : CommonResult.failed("添加角色失败");
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public CommonResult getRole(GetRoleRequest request) throws Exception {
        try {
//            Role role = this.findRoleByRoleName(roleName);
//            if (null == role) {
//                return CommonResult.failed("该角色不存在");
//            }
            List<Criteria.KV> items = request.getCriteria().getItems();
            LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
            if (items.size() != 0) {
                wrapper.eq(Role::getRoleName, items.get(0).getValue());
            }
            wrapper.eq(Role::getDelFlag, "N");
            List<Role> roleList = roleMapper.selectList(wrapper);
            // 查询角色对应的权限
            Map<String, List<RoleVo>> rolePermissionMap = new HashMap<>();
            List<RoleVo> roleVoList = new ArrayList<>();
            for (Role role : roleList) {
                RoleVo roleVo = new RoleVo();
                roleVo.setRoleName(role.getRoleName());
                roleVo.setRoleCode(role.getRoleCode());
                List<Permission> permissionList = this.findPermissionByRoleId(role.getId());
                roleVo.setPermission(permissionList.stream().map(Permission::getPermissionName).collect(Collectors.toList()));
                roleVoList.add(roleVo);
            }
            log.info("roleListSize:{}", roleList.size());
            rolePermissionMap.put("roleList", roleVoList);

            return CommonResult.success(rolePermissionMap, "查找成功");
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
            UpdateWrapper<Role> roleUpdateWrapper = new UpdateWrapper<>();
            Role role = this.findRoleByRoleName(roleName);
            roleUpdateWrapper.eq("role_name", roleName);
            role.setDelFlag("Y");
            roleMapper.update(role, roleUpdateWrapper);
            List<RolePermission> rolePermissionList = rolePermissionMapper.selectList(new LambdaQueryWrapper<RolePermission>().eq(RolePermission::getRoleId, role.getId()));
            for (RolePermission rolePermission : rolePermissionList) {
                rolePermission.setDelFlag("Y");
                rolePermissionMapper.updateById(rolePermission);
            }
//
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
