package com.example.demo.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewVo {
    private String reviewerName;
    private String reviewerRoleName;
    private String reviewDate;
    private String reviewResult;
}
