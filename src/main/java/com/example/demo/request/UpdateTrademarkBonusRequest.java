package com.example.demo.request;

import lombok.Data;

@Data
public class UpdateTrademarkBonusRequest {
    private String bonusId;
    private String actualRelease;
    private String inventorName;
    private String trademarkName;
    private String releaseStatus;
    private String bonusType;
    private String trademarkCode;
}
