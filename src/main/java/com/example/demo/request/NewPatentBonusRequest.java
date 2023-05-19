package com.example.demo.request;


import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
public class NewPatentBonusRequest {
    @NotBlank(message = "专利编号不能为空")
    private String patentCode;
    private String bonusType;
    private String releaseStatus;
    private String bonusAmount;
    private List<inventor> inventorList;


    @Data
    public static class inventor {
        private String inventorName;
        private String actualRelease;
    }
}
