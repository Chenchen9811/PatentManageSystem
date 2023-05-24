package com.example.demo.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class NewSoftwareFileInfoRequest {
    @NotBlank(message = "软著编号不能为空")
    private String softwareCode;
    private String fileName;
    private String fileType;
    private String fileStatus;
}
