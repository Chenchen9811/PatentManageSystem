package com.example.demo.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class NewBillRequest {
    @NotBlank(message = "账单编号不能为空")
    private String billCode;

    private String proposalCode;
    private String agencyName;
    private String payStatus;
    private String dueAmount;
    private String actualPayAmount;
    private String payDate;
    private String billFileName;
}
