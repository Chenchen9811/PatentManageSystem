package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class PatentBonus {
    @TableId(value = "id")
    private Long id;
    private Long patentId;
    private String inventorName;
    private String bonusType;
    private String bonusAmount;
    private String releaseStatus;
    private String actualRelease;
    private Integer ranking;
}
