package com.example.demo.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
public class NewTrademarkBonusRequest {
    @NotBlank(message = "商标编号不能为空")
    private String trademarkCode;
    private String trademarkType;
    @NotBlank(message = "奖金类型不能为空")
    private String bonusType;
    @NotBlank(message = "发放状态不能为空")
    private String releaseStatus;
    private String bonusAmount;
    private List<inventor> inventorList;

    @Data
    public static class inventor{
        private String inventorName;
        private String actualRelease;
    }

}
