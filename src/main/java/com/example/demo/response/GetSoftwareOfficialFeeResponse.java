package com.example.demo.response;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.sql.Date;

@Data
public class GetSoftwareOfficialFeeResponse {
    private String officialFeeCode;
    private String officialFeeName;
    private String dueAmount;
    private String actualAmount;
    private String payStatus;
    private String dueDate;
    private String actualPayDate;
    private String remark;
}
