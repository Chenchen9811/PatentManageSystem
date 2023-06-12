package com.example.demo.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class PatentExport {

    @ExcelProperty("专利编号")
    private String patentCode;
    @ExcelProperty("专利名称")
    private String patentName;
    @ExcelProperty("专利类型")
    private String patentType;
    @ExcelProperty("发明人")
    private String inventorNames;
    @ExcelProperty("申请号")
    private String applicationCode;
    @ExcelProperty("申请日期")
    private String applicationDate;
    @ExcelProperty("授权号")
    private String grantCode;
    @ExcelProperty("授权日期")
    private String grantDate;
    @ExcelProperty("权力状态")
    private String rightStatus;
    @ExcelProperty("当前程序")
    private String currentProgram;

}
