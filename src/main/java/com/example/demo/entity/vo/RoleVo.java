package com.example.demo.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class RoleVo {
    private String roleName;
    private List<String> permission;
}
