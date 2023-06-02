package com.example.demo.response;

import lombok.Data;

@Data
public class GetBillListResponse {
    private String proposalCode;
    private String proposalName;
    private String agency;
    private String billCode;
    private String dueAmount;
    private String payStatus;
    private String officialFeeStatus;
    private String actualPay;
    private String remark;
}
