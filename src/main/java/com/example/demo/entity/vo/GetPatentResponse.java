package com.example.demo.entity.vo;

import lombok.Data;

import java.util.List;

@Data
public class GetPatentResponse {
    private String patentCode;
    private String patentName;
    private String patentType;
    private List<String> inventorNameList;
    private String applicationCode;
    private String applicationDate;
    private String grantCode;
    private String grantDate;
    private String rightStatus;
    private String currentProgram;
}
