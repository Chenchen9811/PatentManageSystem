package com.example.demo.request;

import lombok.Data;

@Data
public class UpdateTrademarkOfficialFeeRequest {
    private String id;
    private String trademarkCode;
    private String dueAmount;
    private String actualPay;
    private String dueDate;
    private String actualPayDate;
    private String officialFeeStatus;
    private String remark;
    private String feeName;
    private String trademarkName;
}
