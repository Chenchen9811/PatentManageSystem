package com.example.demo.response;

import lombok.Data;

@Data
public class GetPatentFileInfoResponse {
    private String patentCode;
    private String uploaderName;
    private String fileType;
    private String fileName;
    private String uploadDate;
    private String fileStatus;
    private String fileId;
    private String patentName;
    private String patentType;
}
