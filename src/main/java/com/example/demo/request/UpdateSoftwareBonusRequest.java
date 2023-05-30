package com.example.demo.request;

import lombok.Data;
import org.springframework.web.bind.annotation.DeleteMapping;

@Data
public class UpdateSoftwareBonusRequest {
    private Long bonusId;
    private String softwareCode;
    private String softwareName;
    private String bonusType;
    private String releaseStatus;
//    private String bonusAmount;
    private String inventorName;
    private String actualRelease;
}
