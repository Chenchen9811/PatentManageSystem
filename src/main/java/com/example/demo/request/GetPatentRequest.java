package com.example.demo.request;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.List;

@Data
public class GetPatentRequest {
    private String patentCode;
    private String patentName;
    private String patentType;
    private String rightStatus;
    private String agency;
    private String currentProgram;
    private String inventorName;
    private String departmentName;
    @NotNull(message = "当前页码不能为空")
    private Integer pageIndex;
    @NotNull(message = "每页显示条数不能为空")
    private Integer pageSize;

    private Criteria criteria;

}
