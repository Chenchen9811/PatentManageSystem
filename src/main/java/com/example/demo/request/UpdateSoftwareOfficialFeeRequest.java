package com.example.demo.request;

import lombok.Data;

@Data
public class UpdateSoftwareOfficialFeeRequest {
    private String id;
    private String softwareCode;
    private String officialFeeName;
    private String officialFeeStatus;
    private String dueAmount;
    private String actualPay;
    private String dueDate;
    private String actualPayDate;
    private String remark;
    private String feeName;
}
