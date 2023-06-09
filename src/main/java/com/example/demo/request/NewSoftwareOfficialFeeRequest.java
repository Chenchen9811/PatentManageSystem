package com.example.demo.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class NewSoftwareOfficialFeeRequest {
    @NotBlank(message = "软著编号不能为空")
    private String softwareCode;
    private String officialFeeName;
    private String officialFeeStatus;
    private String dueAmount;
    private String actualPay;
    private String dueDate;
    private String actualPayDate;
    private String remark;
//    private String payStatus;
    private String feeName;


}
