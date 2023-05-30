package com.example.demo.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class GetPatentBonusListRequest {
    @NotNull(message = "当前页码不能为空")
    private Integer pageIndex;
    @NotNull(message = "每页显示条数不能为空")
    private Integer pageSize;
    private Criteria criteria;
}
