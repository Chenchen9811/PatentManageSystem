package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("t_software_bonus")
public class SoftwareBonus {
    @TableId(value = "id")
    private Long id;
    private Long softwareId;
    private String inventorName;
    private String bonusType;
    private String bonusAmount;
    private String releaseStatus;
    private String actualRelease;
    private Integer ranking;
}
