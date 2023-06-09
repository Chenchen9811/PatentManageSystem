package com.example.demo.service;

import com.example.demo.common.CommonResult;
import com.example.demo.entity.*;
import com.example.demo.request.*;
import com.example.demo.response.GetPatentResponse;

import java.util.List;

public interface PatentService {
    CommonResult newPatent(NewPatentRequest request) throws Exception;

    List<GetPatentResponse> getPatent(GetPatentRequest request) throws Exception;

    CommonResult departmentPatent(Integer pageIndex, Integer pageSize) throws Exception;

    CommonResult myPatent(Integer pageIndex, Integer pageSize) throws Exception;

    CommonResult newOfficialFee(NewPatentOfficialFeeRequest request) throws Exception;

    PatentOfficialFee findPatentOfficialFeeByName(String officialFeeName);

    PatentAnnualFee findPatentAnnualFeeByCode(String annualFeeCode);

    Patent findPatentByName(String patentName);

    Patent findPatentByCode(String patentCode);

    Patent findPatentById(Long patentId);

    List<Patent> findPatentListByIds(List<Long> ids);

    List<PatentBonus> findBonusByPatentId(Long patentId);

    PatentFile findFileByName(String fileName);

    PatentFile findFileByPatentId(Long patentId);

    CommonResult getOfficialFee(GetPatentOfficialFeeRequest request) throws Exception;

    CommonResult updateOfficialFee(UpdatePatentOfficialFeeRequest request) throws Exception;

    CommonResult deleteOfficialFee(String id) throws Exception;

    CommonResult newAnnualFee(NewPatenAnnualFeeRequest request);

    CommonResult getAnnualFee(GetPatentAnnualFeeRequest request);

    CommonResult getAnnualFeeCode(String patentCode, String year);

    CommonResult updateAnnualFee(UpdateAnnualFeeRequest request);

    CommonResult deleteAnnualFee(String annualFeeCode);

    CommonResult newBonus(NewPatentBonusRequest request);

    CommonResult getBonus(GetPatentBonusListRequest request);

    CommonResult deleteBonus(String bonusId);

    CommonResult updateBonus(UpdatePatentBonusRequest request);

    CommonResult newFileInfo(NewPatentFileInfoRequest request);

    CommonResult getFileInfo(GetPatentFileInfoRequest request);

    CommonResult deleteFile(String fileId);

    CommonResult setting(Integer level1, Integer level2, Integer level3);

    CommonResult getWarning(Integer pageIndex, Integer pageSize);
}
