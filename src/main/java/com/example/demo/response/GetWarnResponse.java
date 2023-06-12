package com.example.demo.response;

import lombok.Data;

@Data
public class GetWarnResponse {
    private String patentName;
    private String annualFeeCode;
    private String annualFeeName;
    private String dueDate;
    private String dueAmount;
    private String payStatus;
    private Integer level;
}
