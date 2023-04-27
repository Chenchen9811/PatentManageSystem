package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.sql.Time;
import java.sql.Timestamp;

@Data
@TableName("t_role_permission")
public class RolePermission {
    @TableId(value = "id")
    private Long id;
    private Long roleId;
    private Long permissionId;
    private Timestamp createTime;
    private Long createUser;
    private Timestamp updateTime;
    private Long updateUser;
    private String delFlag;
}
