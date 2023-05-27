package com.example.demo.request;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class GetProposalRequest1 {
    @NotNull(message = "当前页码不能为空")
    private Integer pageIndex;
    @NotNull(message = "每页显示条数不能为空")
    private Integer pageSize;
    private Boolean pageable;
    private Criteria criteria;
    @Data
    public static class Criteria {
        List<KV> items;

        @Data
        public static class KV {
            private String key;
            private String value;
        }
    }
}
