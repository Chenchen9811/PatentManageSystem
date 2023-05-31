package com.example.demo.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class GetSoftwareRequest {
    private String softwareCode;
    private String softwareName;
    private String inventorName;
    private String agency;
    private String developWay;
    private String rightStatus;
    private String proposalBeginDate;
    private String proposalEndDate;
    private String departmentName;
    @NotNull(message = "当前页码不能为空")
    private Integer pageIndex;
    @NotNull(message = "每页显示条数不能为空")
    private Integer pageSize;
    private Criteria criteria;
}
