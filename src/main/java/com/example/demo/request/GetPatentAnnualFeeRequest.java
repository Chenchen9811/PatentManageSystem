package com.example.demo.request;

import lombok.Data;

@Data
public class GetPatentAnnualFeeRequest {
    private Integer pageIndex;
    private Integer pageSize;
    private Criteria criteria;
}
