package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class Patent {
    @TableId(value = "id")
    private Long id;

    private String patentCode;
    private String patentName;
    private String patentType;
    private Long proposalId;
    private String applicationCode;
    private Timestamp applicationDate;
    private String grantCode;
    private Timestamp grantDate;
    private String rightStatus;
    private String patentFile;
    private String agency;
    private String currentProgram;
    private Long departmentId;
    private String reserve1;
    private String reserve2;
    private String totalFee;

}
