package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@Data
public class User {
    @TableId(value = "id")
    private Long id;

    @TableField(value = "usercode")
    private String userCode;

    @TableField(value = "username")
    private String userName;

    private String sex;


    private Long departmentId;

    private String password;


    private String phone;

    private String email;

    private String note;

    private Long roleId;

    private String delFlag;

    private String reserve1;

    private String reserve2;
}
