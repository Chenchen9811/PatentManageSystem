package com.example.demo.service;

import com.example.demo.common.CommonResult;
import com.example.demo.entity.Patent;
import com.example.demo.entity.PatentAnnualFee;
import com.example.demo.entity.PatentOfficialFee;
import com.example.demo.request.*;

public interface PatentService {
    CommonResult newPatent(NewPatentRequest request) throws Exception;

    CommonResult getPatent(GetPatentRequest request) throws Exception;

    CommonResult departmentPatent(Integer pageIndex, Integer pageSize) throws Exception;

    CommonResult myPatent(Integer pageIndex, Integer pageSize) throws Exception;

    CommonResult newOfficialFee(NewPatentOfficialFeeRequest request) throws Exception;

    PatentOfficialFee findPatentOfficialFeeByName(String officialFeeName);

    PatentAnnualFee findPatentAnnualFeeByCode(String annualFeeCode);

    Patent findPatentByName(String patentName);

    Patent findPatentByCode(String patentCode);

    CommonResult getOfficialFee(GetPatentOfficialFeeRequest request) throws Exception;

    CommonResult updateOfficialFee(UpdatePatentOfficialFeeRequest request) throws Exception;

    CommonResult deleteOfficialFee(String officialFeeName) throws Exception;

    CommonResult newAnnualFee(NewPatenAnnualFeeRequest request);

    CommonResult getAnnualFee(String patentName, Integer pageIndex, Integer pageSize);

    CommonResult getAnnualFeeCode(String patentCode, String year);

    CommonResult updateAnnualFee(UpdateAnnualFeeRequest request);

    CommonResult deleteAnnualFee(String annualFeeCode);
}
