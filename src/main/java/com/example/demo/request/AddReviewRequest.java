package com.example.demo.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class AddReviewRequest {
    @NotBlank(message = "提案编号不能为空")
    public String proposalCode;
    @NotBlank(message = "审批意见不能为空")
    public String suggestion;
    @NotNull(message = "审批结论不能为空")
    public Integer result;
}
