package com.example.demo.response;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class GetSoftwareResponse {
    @ExcelProperty("软著编号")
    private String softwareCode;
    @ExcelProperty("软著名称")
    private String softwareName;
    @ExcelProperty("版本号")
    private String version;
    @ExcelProperty("发明人")
    private String inventorName;
    @ExcelProperty("代理机构")
    private String agency;
    @ExcelProperty("开发方式")
    private String developWay;
    @ExcelProperty("注册号")
    private String registerCode;
    @ExcelProperty("授权号")
    private String certificateCode;
    @ExcelProperty("归档号")
    private String archiveCode;
    @ExcelProperty("权力状态")
    private String rightStatus;
    @ExcelProperty("权利范围")
    private String rightRange;
    @ExcelProperty("申请日期")
    private String applicationDate;
    @ExcelProperty("授权日期")
    private String certificateDate;
    @ExcelProperty("出版日期")
    private String publishDate;
    @ExcelProperty("归档日期")
    private String archiveDate;
    @ExcelProperty("完成日期")
    private String finishDate;
}
