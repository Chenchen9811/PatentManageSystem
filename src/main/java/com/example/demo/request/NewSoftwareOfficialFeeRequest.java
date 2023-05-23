package com.example.demo.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class NewSoftwareOfficialFeeRequest {
    @NotBlank(message = "软著编号不能为空")
    private String softwareCode;
    private String officialFeeName;
    private String payStatus;
    private String dueAmount;
    private String dueDate;
    private String actualAmount;
    private String actualPayDate;
    private String remark;
}
