package com.example.demo.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class GetProposalRequest1 {
    @NotNull(message = "当前页码不能为空")
    private Integer pageIndex;
    @NotNull(message = "每页显示条数不能为空")
    private Integer pageSize;
    private String startDate;
    private String endDate;
    private String proposerName;
    private String proposerCode;
    private String inventorCode;
    private String inventorName;
    private Integer proposalState;
    private Integer proposalType;
    private String proposalName;
    private String proposalCode;
    private String departmentName;
}
