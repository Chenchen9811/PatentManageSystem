package com.example.demo.common;

public enum ReviewStatus {
    PASSED("通过",1),
    FAILED("不通过",2);

    private String message;
    private Integer code;
    private ReviewStatus(String message, Integer code){
        this.message = message;
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public Integer getCode() {
        return code;
    }


}
