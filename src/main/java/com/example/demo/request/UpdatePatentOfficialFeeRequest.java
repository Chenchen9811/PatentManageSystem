package com.example.demo.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UpdatePatentOfficialFeeRequest {
    @NotBlank(message = "专利编号不能为空")
    private String officialFeeCode;
    private String officialFeeName;
    private String officialFeeStatus;
    private String payerName;
    private String dueAmount;
    private String actualAmount;
    private String dueDate;
    private String actualPayDate;
}
