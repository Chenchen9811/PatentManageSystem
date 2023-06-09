package com.example.demo.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class NewBillRequest {
    @NotBlank(message = "账单编号不能为空")
    private String billCode;
    private String proposalName;
    private String agency;
    private String dueAmount;
    private String payStatus;
    private String actualPay;
    private String actualPayDate;
    private String proposalCode;
    private String remark;
}
