package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.sql.Date;

@Data
public class Bill {
    @TableId(value = "id")
    private Long id;
    private Long agencyId;
    private Long proposalId;
    private String billCode;
    private String payStatus;
    private String dueAmount;
    private String actualPayAmount;
    private Date payDate;
    private String billUrl;
    private String remark;
    private String reserve1;
    private String reserve2;
}
