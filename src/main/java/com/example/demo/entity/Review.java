package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class Review {
    @TableId(value = "id")
    private Long id;
    private Long proposalId;
    private Long reviewerId;
    @TableField(value = "currentreview_state")
//    private String currentReviewState;
    private String result;
    private String suggestion;
    private Timestamp reviewDate;
}
