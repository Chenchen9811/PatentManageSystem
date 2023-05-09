package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
public class PatentInventor {
    @TableId(value = "id")
    private Long id;

    private Long patentId;

    private Long inventorId;

    private String inventorCode;

    private String inventorName;

    private Integer rate;

    private BigDecimal contribute;

    private Timestamp createTime;

    private Long createUser;

    private Timestamp updateTime;

    private Long updateUser;
}
