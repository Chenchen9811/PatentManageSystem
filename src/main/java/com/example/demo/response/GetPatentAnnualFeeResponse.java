package com.example.demo.response;


import lombok.Data;

@Data
public class GetPatentAnnualFeeResponse {
    private String patentCode;
    private String patentName;
    private String annual;
    private String feeStatus;
//    private String year;
    private String dueAmount;
    private String dueDate;
    private String actualPay;
    private String actualPayDate;
    private String remark;

    private String id;
}
