package com.example.demo.response;

import lombok.Data;

@Data
public class GetTrademarkResponse {
    private String trademarkCode;
    private String trademarkName;
    private String inventorName;
    private String copyRightCode;
    private String trademarkType;
    private String rightStatus;
    private String currentStatus;
    private String trademarkOwner;
}
