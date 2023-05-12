package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.sql.Timestamp;

@Data
@TableName("t_patent_official_fee")
public class PatentOfficialFee {
    @TableId(value = "id")
    private Long id;
    @TableField(value = "officialFee_code")
    private String officialFeeCode;
    @TableField(value = "officialFee_Name")
    private String officialFeeName;
    private Long patentId;
    private String dueAmount; // 应缴金额
    private String actualAmount; // 实缴金额
    @TableField(value = "officialFee_status")
    private String officialFeeStatus;
    private Timestamp dueDate; // 应缴日期

    @TableField(value = "actual_pay_date")
    private Timestamp actualPayDate; // 实缴日期
    private String billUrl;
    private String remark;

    private String payerName;

}
