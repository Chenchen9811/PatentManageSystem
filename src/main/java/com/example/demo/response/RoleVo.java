package com.example.demo.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class RoleVo {
    private String roleName;
    private List<String> permission;
}
