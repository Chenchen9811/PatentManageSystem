package com.example.demo.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class GetTrademarkRequest {
    private String trademarkCode;
    private String trademarkName;
    private String trademarkOwner; //商标权人
    private String trademarkType;
    private String rightStatus;
    private String currentStatus;
    private String copyRightCode;
    private String agency;
    private String inventorName;
    @NotNull(message = "当前页码不能为空")
    private Integer pageIndex;
    @NotNull(message = "每页显示条数不能为空")
    private Integer pageSize;
}
