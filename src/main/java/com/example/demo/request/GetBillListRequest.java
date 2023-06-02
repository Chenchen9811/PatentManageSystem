package com.example.demo.request;

import lombok.Data;

@Data
public class GetBillListRequest {
    private Integer pageIndex;
    private Integer pageSize;
    private Criteria criteria;
}
