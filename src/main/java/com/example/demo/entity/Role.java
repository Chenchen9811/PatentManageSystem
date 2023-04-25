package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class Role {
    @TableId(value = "id")
    private Long id;

    private String roleCode;

    private String roleName;

    private String roleNode;
}
