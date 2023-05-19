package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("t_trademark_bonus")
public class TrademarkBonus {
    @TableId(value = "id")
    private Long id;
    private Long trademarkId;
    private String inventorName;
    private Integer ranking;
    private String actualRelease;
    private String releaseStatus;
    private String bonusType;
    private String bonusAmount;
}
