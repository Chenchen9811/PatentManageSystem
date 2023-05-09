package com.example.demo.request;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Data
public class GetPatentRequest {
    private String patentCode;
    private String patentName;
    private String patentType;
    private String applicationCode;
    private String applicationBeginDate;
    private String applicationEndDate;
    private String grantCode;
    private String grantStartDate;
    private String grantEndDate;
    private String rightStatus;
    private String patentFile;
    private String agency;
    private String currentProgram;
    private String inventorName;
    @NotNull(message = "当前页码不能为空")
    private Integer pageIndex;
    @NotNull(message = "每页显示条数不能为空")
    private Integer pageSize;
}
