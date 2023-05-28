package com.example.demo.request;

import lombok.Data;

@Data
public class NewTrademarkOfficialFeeRequest {
    private String trademarkCode;
    private String officialFeeCode;
    private String dueAmount;
    private String actualPay;
    private String dueDate;
    private String actualPayDate;
    private String officialFeeStatus;
    private String remark;
    private String officialFeeName;
}
