package com.example.demo.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UpdatePatentOfficialFeeRequest {
    private String id;
    private String patentCode;
    private String officialFeeStatus;
    private String feeName;
    private String dueAmount;
    private String actualPay;
    private String dueDate;
    private String actualPayDate;
    private String remark;
}
