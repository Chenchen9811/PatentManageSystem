package com.example.demo.response;

import lombok.Data;

import java.util.List;

@Data
public class ProposalVo1 {
    private String proposalState;
    private Integer reviewState;
    private List<String> inventorNameList;
    private String proposerName;
    private String proposalDate;
    private String proposalCode;
    private String proposalName;
    private String departmentName;
    private String proposalType;
}
