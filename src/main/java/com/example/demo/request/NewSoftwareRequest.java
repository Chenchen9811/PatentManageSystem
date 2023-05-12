package com.example.demo.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Data
public class NewSoftwareRequest {

    private String version;
    @NotBlank(message = "软著编号不能为空")
    private String softwareCode;
    @NotBlank(message = "软著名称不能为空")
    private String softwareName;
    private String applicationDate;
    private String rightStatus;
    private String rightRange; // 权利范围
    private String agency;
    private String departmentName;
    private String proposalDate; // 提案日期
    private String certificateDate; // 发证日期
    private String archiveDate; // 封存日期
    private String publishDate; // 发布日期
    private String finishDate; // 完成日期
    private String developWay; // 开发方式
    private String certificateCode; // 证书号
    private String archiveCode; // 封存号
    private String registerCode; // 登记号
    private String inventorName;
}
