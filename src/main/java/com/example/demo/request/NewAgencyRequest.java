package com.example.demo.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class NewAgencyRequest {
    @NotBlank(message = "代理机构编号不能为空")
    private String agencyCode;
    @NotBlank(message = "代理机构名字不能为空")
    private String agencyName;
    private String agencyAddress;
    private String agencyPhone;
    private String agencyEmail;
    private String agentName; // 代理人名字
    private String agencyHolder; // 代理机构负责人名字
    private String agencyRemark;
}
