package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class Permission {
    @TableId(value = "id")
    private Long id;
    private String permissionCode;
    private String permissionName;
    private String intro;
    private int category;
    private Long url;
    private Timestamp createTime;
    private Long createUser;
    private Timestamp updateTime;
    private Long updateUser;
    private String delFlag;
}
