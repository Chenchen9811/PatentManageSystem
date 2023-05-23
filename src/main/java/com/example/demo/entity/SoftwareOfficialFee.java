package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.sql.Date;

@Data
@TableName("t_software_official_fee")
public class SoftwareOfficialFee {
    @TableId("id")
    private Long id;
    @TableField(value = "officialFee_code")
    private String officialFeeCode;
    @TableField(value = "officialFee_name")
    private String officialFeeName;
    private Long softwareId;
    private String dueAmount;
    private String actualAmount;
    private String payStatus;
    private Date dueDate;
    @TableField(value = "actual_pay_date")
    private Date actualPayDate;
    private String billUrl;
    private String remark;
}
