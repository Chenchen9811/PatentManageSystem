package com.example.demo.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class AddRoleRequest {
    @NotBlank(message = "角色名称不能为空")
    private String roleName;
    @NotBlank(message = "角色编号不能为空")
    private String roleCode;
    @NotBlank(message = "角色权限不能为空")
    private String permissionName;
}
