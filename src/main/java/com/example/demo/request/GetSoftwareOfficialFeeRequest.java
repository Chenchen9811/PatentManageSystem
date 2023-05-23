package com.example.demo.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class GetSoftwareOfficialFeeRequest {
    @NotNull(message = "当前页码不能为空")
    private Integer pageIndex;
    @NotNull(message = "每页显示条数不能为空")
    private Integer pageSize;
    private String officialFeeName;
    private String dueDateBegin;
    private String dueDateEnd;
    private String actualPayDateBegin;
    private String actualPayDateEnd;
    private String dueAmount;
    private String actualAmount;
}
