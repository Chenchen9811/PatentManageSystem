package com.example.demo.response;

import lombok.Data;

@Data
public class GetPatentOfficialFeeResponse {
    private String patentCode;
    private String patentName;
    private String totalFee;
    private String proposerName;
}
