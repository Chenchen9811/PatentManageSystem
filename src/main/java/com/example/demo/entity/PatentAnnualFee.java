package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.sql.Timestamp;

@Data
@TableName("t_patent_annual_fee")
public class PatentAnnualFee {
    @TableId(value = "id")
    private Long id;
    @TableField("annualFee_code")
    private String annualFeeCode; // 年费编号
    @TableField("annualFee_name")
    private String annualFeeName; // 年费名字: xxx（专利）xxx（年度）年费
    private Long patentId;
    private String year;
    private String dueAmount;
    private String actualAmount;
    private Timestamp dueDate;
    @TableField("actual_pay_date")
    private Timestamp actualPayDate;
    private String payStatus;
    private String remark;
    private String payerName;
    private String billUrl;
}
