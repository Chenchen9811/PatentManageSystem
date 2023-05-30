package com.example.demo.request;

import lombok.Data;

@Data
public class UpdatePatentBonusRequest {
    private String patentCode;
    private String bonusType;
    private String releaseStatus;
    private String patentName;
    private String inventorName;
    private String actualRelease;
    private String bonusId;
}
