package com.example.demo.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class NewTrademarkFileInfoRequest {
    @NotBlank(message = "商标编号不能为空")
    private String trademarkCode;
    @NotBlank(message = "文件类型不能为空")
    private String fileType;
    private String fileName;
    private String fileStatus;
}
