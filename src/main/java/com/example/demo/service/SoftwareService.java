package com.example.demo.service;

import com.example.demo.common.CommonResult;
import com.example.demo.entity.Software;
import com.example.demo.request.GetSoftwareRequest;
import com.example.demo.request.NewSoftwareRequest;

public interface SoftwareService {
    CommonResult newSoftware(NewSoftwareRequest request) throws Exception;

    Software findSoftwareByName(String softwareName);

    CommonResult getSoftware(GetSoftwareRequest request) throws Exception;

    CommonResult software(Integer pageIndex, Integer pageSize, Integer isDepartment) throws Exception;
}
