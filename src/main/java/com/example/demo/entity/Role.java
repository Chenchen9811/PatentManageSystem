package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class Role {
    @TableId(value = "F_ID")
    private Integer F_ID;
    @TableField(value = "F_RoleCode")
    private String F_RoleCode;
    @TableField(value = "F_RoleName")
    private String F_RoleName;
    @TableField(value = "F_RoleNote")
    private String F_RoleNote;
}
