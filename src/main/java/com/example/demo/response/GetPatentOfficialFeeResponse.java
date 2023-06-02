package com.example.demo.response;

import lombok.Data;

@Data
public class GetPatentOfficialFeeResponse {
    private String patentCode;
    private String patentName;
    private String totalAmount;
    private String feeName;
    private String dueAmount;
    private String dueDate;
    private String officialFeeStatus;
    private String actualPay;
    private String actualPayDate;
    private String remark;
    private String id;
}
