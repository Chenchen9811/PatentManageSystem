package com.example.demo.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class NewPatentOfficialFeeRequest {

    private String patentName;
    @NotBlank(message = "官费名字不能为空")
    private String officialFeeName;
    @NotBlank(message = "官费状态不能为空")
    private String officialFeeStatus;
    @NotBlank(message = "应缴官费不能为空")
    private String dueAmount;
    @NotBlank(message = "实缴官费不能为空")
    private String actualAmount;
    private String dueDate;
    private String actualPayDate;
    private String remark;
}
