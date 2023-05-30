package com.example.demo.request;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.sql.Timestamp;

@Data
public class NewTrademarkRequest {
    @NotBlank(message = "商标编码不能为空")
    private String trademarkCode;
    @NotBlank(message = "商标名字不能为空")
    private String trademarkName;
    @NotBlank(message = "商标权人不能为空")
    private String trademarkOwner; //商标权人
    private String trademarkType;
    private String rightStatus;
    private String currentStatus;
//    private String applyDate;
//    private String grantDate;
    private String copyRightCode;
    private String agency;
    private String departmentName;
    @NotBlank(message = "发明人名字不能为空")
    private String inventorName;
}
