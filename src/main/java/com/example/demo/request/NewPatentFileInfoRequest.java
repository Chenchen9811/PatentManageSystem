package com.example.demo.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class NewPatentFileInfoRequest {
    @NotBlank(message = "专利编号不能为空")
    private String patentCode;
    private String fileStatus;
    private String fileName;
    private String fileType;
}
