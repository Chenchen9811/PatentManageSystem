package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class Trademark {
    @TableId(value = "id")
    private Long id;
    private String trademarkCode;
    private String trademarkName;
    private Long proposalId;
    private Long inventorId;
    private String rightStatus;
    private String currentStatus;
    private String trademarkOwner; //商标权人
    private Timestamp applyDate;
    private Timestamp grantDate;
    private String trademarkType;
    @TableField(value = "copyright_code")
    private String copyRightCode;
    private String trademarkDesign;
    private String agency;
    private Long departmentId;
}
