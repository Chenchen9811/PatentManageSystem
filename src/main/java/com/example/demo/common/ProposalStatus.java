package com.example.demo.common;

import lombok.Data;


public enum ProposalStatus {
    ALL("全部", 0),
    ON_REVIEW("在审",1),
    PASSED("通过",2),
    FAIL("不通过",3),
    ADDITIONAL_DOCUMENTS_REQUIRED("补充文件",4);

    private String name;
    private Integer code;
    private ProposalStatus(String name, Integer code) {
        this.name = name;
        this.code = code;
    }

    public String getName(){
        return name;
    }
    public Integer getCode() {
        return code;
    }
}
