package com.example.demo.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.List;

@Data
public class NewPatentRequest {
    @NotBlank(message = "专利编号不能为空")
    private String patentCode;
    @NotBlank(message = "专利名称不能为空")
    private String patentName;
    @NotBlank(message = "专利类型不能为空")
    private String patentType;
    private String applicationCode;
    private String applicationDate;
    private String grantCode;
    private String grantDate;
    private String rightStatus;
    private String agency;
    private String currentProgram;
    @NotBlank(message = "部门名字不能为空")
    private String departmentName;
    private List<Inventor> inventorList;



    @Data
    public static class Inventor {
        private String inventorName; // 发明人名字
        private Integer rate; // 贡献
    }
}
