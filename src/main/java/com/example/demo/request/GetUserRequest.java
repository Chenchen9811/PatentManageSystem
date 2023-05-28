package com.example.demo.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class GetUserRequest {
    @NotBlank(message = "当前页码不能为空")
    private Integer pageIndex;
    @NotBlank(message = "每页显示条数不能为空")
    private Integer pageSize;
    private Criteria criteria;
}
