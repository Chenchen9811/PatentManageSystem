package com.example.demo.response;

import lombok.Data;

@Data
public class GetSoftwareBonusResponse {
    private Long bonusId;
    private String softwareCode;
    private String softwareName;

    private String version;
    private String bonusType;
    private String bonusAmount;
    private String releaseStatus;
    private String inventorName;
    private Integer ranking;
    private String actualRelease;
}
