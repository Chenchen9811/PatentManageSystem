package com.example.demo.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class AddUserRequest {
    @NotBlank(message = "用户名不能为空")
    private String userName;
    @NotBlank(message = "工号不能为空")
    private String userCode;
    @NotBlank(message = "角色名字不能为空")
    private String roleName;
    @NotBlank(message = "密码不能为空")
    private String password;
    private String phone;
    @NotBlank(message = "部门名字不能为空")
    private String departmentName;
}
