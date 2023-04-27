package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.sql.Timestamp;


@Data
public class Proposal {
    @TableId(value = "id")
    private Long id;

    private String proposalCode;

    private String proposalName;

    private Long proposerId;

    private String proposerName;

    private Timestamp proposalDate;

    private String proposalType;

    private String referenceBook;

    private String substance;

    private String note;
}
