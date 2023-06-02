package com.example.demo.request;

import lombok.Data;

@Data
public class UpdateBillRequest {
    private String billCode;
    private String proposalName;
    private String agency;
    private String dueAmount;
    private String payStatus;
    private String actualPay;
    private String actualPayDate;
    private String remark;
}
