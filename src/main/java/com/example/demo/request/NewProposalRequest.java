package com.example.demo.request;

import com.example.demo.entity.Inventor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class NewProposalRequest {
    @NotBlank(message = "提案编号不能为空")
    private String proposalCode;
    @NotBlank(message = "提案名称不能为空")
    private String proposalName;
    @NotBlank(message = "提案人名字不能为空")
    private String proposerName;
    private String datePicker;
    @NotNull(message = "知识产权类型不能为空")
    private Integer patentType;
    private List<InventorVo> listOfInventor;
    private String detailText;

    @Data
    public static class InventorVo {
        private String inventorName; // 发明人名字
        private String rate; // 贡献
    }
}
