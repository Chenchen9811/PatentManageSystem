package com.example.demo.response;

import lombok.Data;

@Data
public class GetTrademarkOfficialFeeResponse {
    private String trademarkCode;
    private String trademarkName;
    private String actualPayDate;
    private String dueDate;
    private String officialFeeStatus;
    private String dueAmount;
    private String actualPay;
    private Long id;
}
