package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.sql.Timestamp;

@Data
@TableName("t_user_role")
public class UserRole {
    @TableId(value = "id")
    private Long id;
    private Long userId;
    private Long roleId;
    private Timestamp createTime;
    private Long createUser;
    private Timestamp updateTime;
    private Long updateUser;
}
