package com.example.demo.service;

import com.example.demo.common.CommonResult;
import com.example.demo.entity.Software;
import com.example.demo.entity.SoftwareOfficialFee;
import com.example.demo.request.GetSoftwareOfficialFeeRequest;
import com.example.demo.request.GetSoftwareRequest;
import com.example.demo.request.NewSoftwareOfficialFeeRequest;
import com.example.demo.request.NewSoftwareRequest;

public interface SoftwareService {
    CommonResult newSoftware(NewSoftwareRequest request) throws Exception;

    Software findSoftwareByName(String softwareName);

    Software findSoftwareByCode(String softwareCode);

    SoftwareOfficialFee findOfficialFeeByName(String officialFeeName);

    SoftwareOfficialFee findOfficialFeeByCode(String officialFeeCode);

    CommonResult getSoftware(GetSoftwareRequest request) throws Exception;

    CommonResult software(Integer pageIndex, Integer pageSize, Integer isDepartment) throws Exception;

    CommonResult newOfficialFee(NewSoftwareOfficialFeeRequest request);

    CommonResult getOfficialFee(GetSoftwareOfficialFeeRequest request);

    CommonResult deleteOfficialFee(String officialFeeCode) throws Exception;
}
