package com.example.demo.response;

import lombok.Data;

@Data
public class GetSoftwareFileInfoResponse {
    private String fileName;
    private String version;
    private String proposerName;
    private String proposalDate;
    private String developWay;
    private String rightRange;
    private String uploaderName;
    private String fileId;
    private String softwareName;
    private String softwareCode;
    private String fileType;
    private String uploadDate;
}
