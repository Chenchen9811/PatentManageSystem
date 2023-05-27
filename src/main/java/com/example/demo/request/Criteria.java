package com.example.demo.request;

import lombok.Data;

import java.util.List;

@Data
public class Criteria {

    List<KV> items;

    @Data
    public static class KV {
        private String key;
        private String value;
    }
}
