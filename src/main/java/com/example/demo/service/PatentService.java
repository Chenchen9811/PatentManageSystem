package com.example.demo.service;

import com.example.demo.common.CommonResult;
import com.example.demo.entity.*;
import com.example.demo.request.*;

import java.util.List;

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

    Patent findPatentById(Long patentId);

    List<PatentBonus> findBonusByPatentId(Long patentId);

    PatentFile findFileByName(String fileName);

    PatentFile findFileByPatentId(Long patentId);

    CommonResult getOfficialFee(GetPatentOfficialFeeRequest request) throws Exception;

    CommonResult updateOfficialFee(UpdatePatentOfficialFeeRequest request) throws Exception;

    CommonResult deleteOfficialFee(String officialFeeName) throws Exception;

    CommonResult newAnnualFee(NewPatenAnnualFeeRequest request);

    CommonResult getAnnualFee(String patentName, Integer pageIndex, Integer pageSize);

    CommonResult getAnnualFeeCode(String patentCode, String year);

    CommonResult updateAnnualFee(UpdateAnnualFeeRequest request);

    CommonResult deleteAnnualFee(String annualFeeCode);

    CommonResult newBonus(NewPatentBonusRequest request);

    CommonResult getBonus(String patentCode, Integer pageIndex, Integer pageSize);

    CommonResult deleteBonus(String patentCode, String inventorName);

    CommonResult updateBonus(NewPatentBonusRequest request);

    CommonResult newFileInfo(NewPatentFileInfoRequest request);

    CommonResult getFileInfo(GetPatentFileInfoRequest request);
}
