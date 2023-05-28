package com.example.demo.response;

import lombok.Data;

@Data
public class GetUserResponse {
    private String roleName;
    private String phone;
    private String departmentName;
    private String userCode;
    private String userName;
}
