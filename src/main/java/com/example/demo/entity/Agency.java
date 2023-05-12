package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class Agency {
    @TableId(value = "id")
    private Long id;
    private String agencyCode;
    private String agencyName;
    private String agencyAddress;
    private String agencyPhone;
    private String agencyEmail;
    private String agentName; // 代理人名字
    private String agencyHolder; // 代理机构负责人名字
    private String agencyRemark;
    private String reserve1;
    private String reserve2;
}
