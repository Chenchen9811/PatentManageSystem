package com.example.demo.response;


import lombok.Data;

@Data
public class GetPatentAnnualFeeResponse {
    private String payStatus;
    private String year;
    private String dueAmount;
    private String dueDate;
    private String actualAmount;
    private String actualPayDate;
}
