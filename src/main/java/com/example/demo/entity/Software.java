package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class Software {
    @TableId(value = "id")
    private Long id;

    private Long inventorId;
    private String version;
    private String softwareCode;
    private String softwareName;
    private Long proposalId;
    private Timestamp applicationDate;
    private Timestamp grantDate;
    private String rightStatus;
    private String softwareFile;
    private String agency;
    private Long departmentId;
    private Timestamp proposalDate; // 提案日期
    private Timestamp certificateDate; // 发证日期
    private Timestamp archiveDate; // 封存日期
    private Timestamp publishDate; // 发布日期
    private Timestamp finishDate; // 完成日期
    private String developWay; // 开发方式
    private String rightRange; // 权利范围
    private String certificateCode; // 证书号
    private String archiveCode; // 封存号
    private String registerCode; // 登记号
}
