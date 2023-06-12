package com.example.demo.response;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class GetTrademarkResponse {
    @ExcelProperty("商标编号")
    private String trademarkCode;
    @ExcelProperty("商标名称")
    private String trademarkName;
    @ExcelProperty("发明人")
    private String inventorName;
    @ExcelProperty("版权号")
    private String copyRightCode;
    @ExcelProperty("商标类型")
    private String trademarkType;
    @ExcelProperty("权力状态")
    private String rightStatus;
    @ExcelProperty("当前状态")
    private String currentStatus;
    @ExcelProperty("商标权人")
    private String trademarkOwner;
}
