package com.example.demo.response;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.sql.Date;

@Data
public class GetSoftwareOfficialFeeResponse {
    private String officialFeeCode;
    private String dueAmount;
    private String actualPay;
    private String payStatus;
    private String dueDate;
    private String actualPayDate;
    private String remark;
    private String softwareCode;
    private String softwareName;
    private String id;
    private String totalAmount;
    private String officialFeeStatus;
    private String feeName;
}
