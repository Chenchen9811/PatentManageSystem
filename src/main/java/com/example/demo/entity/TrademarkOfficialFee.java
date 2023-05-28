package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.sql.Date;

@Data
@TableName("t_trademark_official_fee")
public class TrademarkOfficialFee {
    @TableId(value = "id")
    private Long id;
    @TableField(value = "officialFee_Code")
    private String officialFeeCode;
    private Long trademarkId;
    private String dueAmount;
    private String actualPay;
    @TableField(value = "officialFee_status")
    private String officialFeeStatus;
    @TableField(value = "actual_pay_date")
    private Date actualPayDate;
    private Date dueDate;
    private String billUrl;
    private String remark;
    @TableField(value = "officialFee_name")
    private String officialFeeName;
}
