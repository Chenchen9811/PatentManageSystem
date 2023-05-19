package com.example.demo.request;

import lombok.Data;

@Data
public class GetTrademarkOfficialFeeRequest {
    private String trademarkCode;
    private String trademarkName;
    private String inventorName;
    private String actualPayBeginDate;
    private String actualPayEndDate;
    private String dueAmount;
    private Integer pageIndex;
    private Integer pageSize;
}
