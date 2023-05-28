package com.example.demo.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class GetSoftwareFileInfoRequest {
    @NotNull(message = "当前页码不能为空")
    private Integer pageIndex;
    @NotNull(message = "每页显示条数不能为空")
    private Integer pageSize;
    @NotBlank(message = "文件类型不能为空")
    private String fileType;
    private String fileName;
    private String uploadDateBegin;
    private String uploadDateEnd;
    private String softwareCode;
    private Criteria criteria;
}
