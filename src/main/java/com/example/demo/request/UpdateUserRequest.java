package com.example.demo.request;

import lombok.Data;

@Data
public class UpdateUserRequest {
    private String userName;
    private String userCode;
    private String phone;
    private String roleName;
    private String password;
}
