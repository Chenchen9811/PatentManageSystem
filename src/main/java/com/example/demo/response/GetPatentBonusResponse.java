package com.example.demo.response;

import lombok.Data;

@Data
public class GetPatentBonusResponse {
    private Long patentBonusId;
    private String patentCode;
    private String patentType;
    private String bonusType;
    private String bonusAmount;
    private String releaseStatus;
    private String actualRelease;
    private Integer ranking;
}
