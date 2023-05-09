package com.example.demo.service;

import com.example.demo.common.CommonResult;
import com.example.demo.request.GetPatentRequest;
import com.example.demo.request.NewPatentRequest;

public interface PatentService {
    CommonResult newPatent(NewPatentRequest request) throws Exception;

    CommonResult getPatent(GetPatentRequest request) throws Exception;

    CommonResult departmentPatent(Integer pageIndex, Integer pageSize) throws Exception;

    CommonResult myPatent(Integer pageIndex, Integer pageSize) throws Exception;
}
