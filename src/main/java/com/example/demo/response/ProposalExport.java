package com.example.demo.response;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.util.List;

@Data
public class ProposalExport {

    @ExcelProperty("提案状态")
    private String proposalState;
    @ExcelProperty("审批状态")
    private String reviewState;
    @ExcelProperty("发明人")
    private String inventorNames;
    @ExcelProperty("提案人")
    private String proposerName;
    @ExcelProperty("提案日期")
    private String proposalDate;
    @ExcelProperty("提案编号")
    private String proposalCode;
    @ExcelProperty("提案名字")
    private String proposalName;
    @ExcelProperty("部门名字")
    private String departmentName;
    @ExcelProperty("提案状态")
    private String proposalType;
}
