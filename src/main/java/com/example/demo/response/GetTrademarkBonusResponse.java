package com.example.demo.response;

import lombok.Data;

@Data
public class GetTrademarkBonusResponse {
    private String trademarkCode;
    private String trademarkType;
    private String bonusType;
    private String bonusAmount;
    private String releaseStatus;
    private String inventorName;
    private Integer ranking;
    private String actualRelease;
}
