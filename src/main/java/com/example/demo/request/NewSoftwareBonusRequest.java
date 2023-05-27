package com.example.demo.request;

import lombok.Data;

import java.util.List;

@Data
public class NewSoftwareBonusRequest {
    private String softwareCode;
    private String softwareName;
    private String bonusType;
    private String releaseStatus;
    private String bonusAmount;
    private List<inventor> listOfInventor;
    @Data
    public static class inventor {
        private String inventorName;
        private String actualRelease;
    }
}
