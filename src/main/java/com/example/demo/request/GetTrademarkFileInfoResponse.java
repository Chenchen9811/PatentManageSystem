package com.example.demo.request;

import lombok.Data;

@Data
public class GetTrademarkFileInfoResponse {
    private String fileName;
    private String trademarkCode;
    private String proposerName;
    private String proposalDate;
    private String trademarkOwnerName;
    private String trademarkType;
    private String uploadDate;
    private String copyRightCode;
    private String uploaderName;
    private String fileType;
}
