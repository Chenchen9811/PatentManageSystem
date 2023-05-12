package com.example.demo.response;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class GetSoftwareResponse {
    private String softwareCode;
    private String softwareName;
    private String version;
    private String inventorName;
    private String agency;
    private String developWay;
    private String registerCode;
    private String certificateCode;
    private String archiveCode;
    private String rightStatus;
    private String rightRange;
    private String applicationDate;
    private String certificateDate;
    private String publishDate;
    private String archiveDate;
    private String finishDate;
}
