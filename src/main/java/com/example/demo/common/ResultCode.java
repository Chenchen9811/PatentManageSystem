package com.example.demo.common;

public enum ResultCode implements IErrorCode{
    SUCCESS("200", "操作成功"),
    FAILED("500", "操作失败"),
    VALIDATE_FAILED("402", "参数检验失败"),
    UNAUTHORIZED("401", "暂未登录或token已经过期"),
    FORBIDDEN("403", "没有相关权限");
    private String code;
    private String message;

    /**
     * 构造方法
     * @param code 返回值
     * @param message 返回信息
     * */
    private ResultCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 获取返结果code
     *
     * */
    public String getCode() {
        return code;
    }

    /**
     * 获取结果信息
     *
     * */
    public String getMessage() {
        return message;
    }
}
