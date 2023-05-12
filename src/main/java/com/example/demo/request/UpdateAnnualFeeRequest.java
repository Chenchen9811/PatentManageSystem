package com.example.demo.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UpdateAnnualFeeRequest {
    @NotBlank(message = "专利年费编号不能为空")
    private String annualFeeCode;
    private String payStatus;
    private String year;
    private String dueAmount;
    private String actualAmount;
    private String dueDate;
    private String actualPayDate;
}
