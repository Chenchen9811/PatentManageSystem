package com.example.demo.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class GetSoftwareRequest {
    private String softwareCode;
    private String softwareName;
    private String version;
    private String inventorName;
    private String agency;
    private String developWay;
    private String registerCode;
    private String certificateCode;
    private String archiveCode;
    private String rightStatus;
    private String rightRange;
    private String proposalBeginDate;
    private String proposalEndDate;
    private String applicationBeginDate;
    private String applicationEndDate;
    private String certificateBeginDate;
    private String certificateEndDate;
    private String publishBeginDate;
    private String publishEndDate;
    private String archiveBeginDate;
    private String archiveEndDate;
    private String finishBeginDate;
    private String finishEndDate;
    @NotNull(message = "当前页码不能为空")
    private Integer pageIndex;
    @NotNull(message = "每页显示条数不能为空")
    private Integer pageSize;
    private Criteria criteria;
}
