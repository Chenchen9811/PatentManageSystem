package com.example.demo.response;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class GetAgencyResponse {

    private String agencyCode;
    private String agencyName;
    private String agencyAddress;
    private String agencyPhone;
    private String agencyEmail;
    private String agentName; // 代理人名字
    private String agencyHolder; // 代理机构负责人名字
    private String agencyRemark;
}
