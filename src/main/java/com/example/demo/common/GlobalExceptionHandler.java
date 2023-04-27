package com.example.demo.common;

import com.example.demo.Utils.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public CommonResult<Object> handleException(HttpMessageNotReadableException e) {
        log.error("", e);
        return CommonResult.failed("请求参数有错误");
    }
    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public CommonResult<Object> handleException(MethodArgumentNotValidException e) {
        String err = e.getBindingResult().getFieldError() != null ? e.getBindingResult().getFieldError().getDefaultMessage() : e.getMessage();
        log.error("", e);
        return CommonResult.failed(err);
    }
}
