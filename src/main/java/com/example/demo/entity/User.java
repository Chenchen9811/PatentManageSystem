package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@Data
public class User {
    @TableId(value = "F_ID")
    private Integer F_ID;
    @TableField(value = "F_UserCode")
    private String F_UserCode;
    @TableField(value = "F_UserName")
    private String F_UserName;
    @TableField(value = "F_UserSex")
    private String F_UserSex;
    @TableField(value = "F_DepartmentId")
    private Integer F_DepartmentId;
    @TableField(value = "F_UserPassword")
    private String F_UserPassword;

    @TableField(value = "F_UserPhone")
    private String F_UserPhone;
    @TableField(value = "F_UserMail")
    private String F_UserMail;
    @TableField(value = "F_UserNote")
    private String F_UserNote;
    @TableField(value = "F_YL3")
    private String F_YL3;
    @TableField(value = "F_YL4")
    private String F_YL4;
}
