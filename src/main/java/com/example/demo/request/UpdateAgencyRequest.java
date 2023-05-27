package com.example.demo.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UpdateAgencyRequest {
    @NotBlank(message = "代理机构编号不能为空")
    private String agencyCode;
    private String agencyName;
    private String agencyPhone;
    private String agencyAddress;
    private String agencyEmail;
    private String agentName;
    private String agencyHolder;
    private String agencyRemark;
}
