package com.example.demo.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class NewPatenAnnualFeeRequest {
    @NotBlank(message = "缴费状态不能为空")
    private String feeStatus;
    @NotBlank(message = "年度不能为空")
    private String annual;
    @NotBlank(message = "应缴金额不能为空")
    private String dueAmount;
    @NotBlank(message = "实缴金额不能为空")
    private String actualAmount;
    @NotBlank(message = "应缴日期不能为空")
    private String dueDate;
    @NotBlank(message = "实缴日期不能为空")
    private String actualPayDate;
    @NotBlank(message = "年费所属专利名字不能为空")
    private String patentName;
    private String remark;
}
