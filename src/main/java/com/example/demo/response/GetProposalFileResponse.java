package com.example.demo.response;

import lombok.Data;

@Data
public class GetProposalFileResponse {
    private String fileName;
    private String proposalCode;
    private String proposalName;
    private String proposalType;
    private String proposalState;
    private String uploadDate;
    private String uploaderName;
}
