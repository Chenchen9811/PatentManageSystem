package com.example.demo.service.manager;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.example.demo.Utils.CommonUtil;
import com.example.demo.entity.*;
import com.example.demo.mapper.PatentMapper;
import com.example.demo.request.*;
import com.example.demo.service.DepartmentService;
import com.example.demo.service.PatentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PatentManager {


    @Resource
    private PatentMapper patentMapper;

    @Resource
    private DepartmentService departmentService;

    public LambdaQueryWrapper<PatentBonus> getBonusWrapper(GetPatentBonusListRequest request) {
        LambdaQueryWrapper<PatentBonus> wrapper = new LambdaQueryWrapper<>();
        List<Criteria.KV> items = request.getCriteria().getItems();
        for (Criteria.KV kv : items) {
            switch (kv.getKey()) {
                case "bonusAmount": {
                    wrapper.eq(PatentBonus::getBonusAmount, kv.getValue());
                    break;
                }
                case "bonusType": {
                    if (Integer.valueOf(kv.getValue()).equals(0)) break;
                    wrapper.eq(PatentBonus::getBonusType, kv.getValue());
                    break;
                }
                case "releaseStatus": {
                    if (Integer.valueOf(kv.getValue()).equals(0)) break;
                    wrapper.eq(PatentBonus::getReleaseStatus, kv.getValue());
                    break;
                }
                case "inventorName": {
                    wrapper.eq(PatentBonus::getInventorName, kv.getValue());
                    break;
                }
            }
        }
        return wrapper;
    }


    public LambdaQueryWrapper<Patent> getPatentWrapper(GetPatentBonusListRequest request) {
        LambdaQueryWrapper<Patent> wrapper = new LambdaQueryWrapper<>();
        List<Criteria.KV> items = request.getCriteria().getItems();
        for (Criteria.KV kv : items) {
            switch (kv.getKey()) {
                case "patentName" : {
                    wrapper.eq(Patent::getPatentName, kv.getValue());
                    break;
                }
                case "patentCode" : {
                    wrapper.eq(Patent::getPatentCode, kv.getValue());
                    break;
                }
                case "patentType" : {
                    if (Integer.valueOf(kv.getValue()).equals(0)) break;
                    wrapper.eq(Patent::getPatentType, kv.getValue());
                    break;
                }
            }
        }
        return wrapper;
    }


    public LambdaQueryWrapper<PatentFile> getFileWrapper(GetPatentFileInfoRequest request) {
        LambdaQueryWrapper<PatentFile> wrapper = new LambdaQueryWrapper<>();
//        if (StringUtils.isNotBlank(request.getFileName())) {
//            wrapper.eq(PatentFile::getFileName, request.getFileName());
//        }
//        if (StringUtils.isNotBlank(request.getUploadDateBegin()) && StringUtils.isNotBlank(request.getUploadDateEnd())) {
//            wrapper.between(PatentFile::getUploadDate, request.getUploadDateBegin(), request.getUploadDateEnd());
//        }
        List<Criteria.KV> items = request.getCriteria().getItems();
        for (Criteria.KV kv : items) {
            switch (kv.getKey()) {
                case "fileType": {
                    if (Integer.parseInt(kv.getValue()) == 0) break;
                    wrapper.eq(PatentFile::getFileType, kv.getValue());
                    break;
                }
                case "patentCode": {
                    Patent patent = patentMapper.selectOne(new LambdaQueryWrapper<Patent>().eq(Patent::getPatentCode, kv.getValue()));
                    wrapper.eq(PatentFile::getPatentId, patent.getId());
                    break;
                }
                case "uploadDateBegin": {
                    String endDate = null;
                    for (Criteria.KV kV : items) {
                        if (kV.getKey().equals("uploadDateEnd")) {
                            endDate = kV.getValue();
                            break;
                        }
                    }
                    wrapper.between(PatentFile::getUploadDate, kv.getValue(), endDate);
                    break;
                }
            }
        }
         wrapper.eq(PatentFile::getFileType, request.getFileType());
        return wrapper;
    }


    public PatentAnnualFee getUpdatedAnnualFee(UpdateAnnualFeeRequest request, PatentAnnualFee annualFee) {
        if (StringUtils.isNotBlank(request.getActualAmount())) {
            annualFee.setActualAmount(request.getActualAmount());
        }
        if (StringUtils.isNotBlank(request.getDueAmount())) {
            annualFee.setDueAmount(request.getDueAmount());
        }
        if (StringUtils.isNotBlank(request.getDueDate())) {
            annualFee.setDueDate(CommonUtil.stringDateToTimeStamp(request.getDueDate()));
        }
        if (StringUtils.isNotBlank(request.getActualPayDate())) {
            annualFee.setActualPayDate(CommonUtil.stringDateToTimeStamp(request.getActualPayDate()));
        }
        if (StringUtils.isNotBlank(request.getYear())) {
            annualFee.setYear(request.getYear());
        }
        if (StringUtils.isNotBlank(request.getPayStatus())) {
            annualFee.setPayStatus(request.getPayStatus());
        }
        return annualFee;
    }


    public PatentOfficialFee getUpdatedOfficialFee(UpdatePatentOfficialFeeRequest request, PatentOfficialFee officialFee) {
        if (StringUtils.isNotBlank(request.getOfficialFeeName())) {
            officialFee.setOfficialFeeName(request.getOfficialFeeName());
        }
        if (StringUtils.isNotBlank(request.getOfficialFeeStatus())) {
            officialFee.setOfficialFeeStatus(request.getOfficialFeeStatus());
        }
        if (StringUtils.isNotBlank(request.getPayerName())) {
            officialFee.setPayerName(request.getPayerName());
        }
        if (StringUtils.isNotBlank(request.getDueAmount())) {
            officialFee.setDueAmount(request.getDueAmount());
        }
        if (StringUtils.isNotBlank(request.getActualAmount())) {
            officialFee.setActualAmount(request.getActualAmount());
        }
        if (StringUtils.isNotBlank(request.getDueDate())) {
            officialFee.setDueDate(CommonUtil.stringDateToTimeStamp(request.getDueDate()));
        }
        if (StringUtils.isNotBlank(request.getActualPayDate())) {
            officialFee.setActualPayDate(CommonUtil.stringDateToTimeStamp(request.getActualPayDate()));
        }
        return officialFee;
    }

    public LambdaQueryWrapper<Patent> getWrapperByOfficialFee(GetPatentOfficialFeeRequest request) {
        LambdaQueryWrapper<Patent> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(request.getPatentCode())) {
            wrapper.eq(Patent::getPatentCode, request.getPatentCode());
        }
        if (StringUtils.isNotBlank(request.getPatentName())) {
            wrapper.eq(Patent::getPatentName, request.getPatentName());
        }
        if (StringUtils.isNotBlank(request.getTotalFee())) {
            wrapper.eq(Patent::getTotalFee, request.getTotalFee());
        }
        return wrapper;
    }

    public LambdaQueryWrapper<Patent> getWrapper(GetPatentOfficialFeeRequest request) {
        LambdaQueryWrapper<Patent> wrapper = new LambdaQueryWrapper<>();
        List<Criteria.KV> items = request.getCriteria().getItems();
        for (Criteria.KV kv : items) {
            switch (kv.getKey()) {
                case "patentCode" : {
                    wrapper.eq(Patent::getPatentCode, kv.getValue());
                    break;
                }
                case "patentName" : {
                    wrapper.eq(Patent::getPatentName, kv.getValue());
                    break;
                }
                case "totalFee" : {
                    wrapper.eq(Patent::getTotalFee, kv.getValue());
                    break;
                }
            }
        }
        return wrapper;
    }


    public Map<String, Object> getWrapper(GetPatentRequest request) {
        LambdaQueryWrapper<Patent> patentWrapper = new LambdaQueryWrapper<>();
        LambdaQueryWrapper<PatentInventor> patentInventorWrapper = new LambdaQueryWrapper<>();
        List<Criteria.KV> items = request.getCriteria().getItems();
        for (Criteria.KV kv : items) {
            switch (kv.getKey()) {
                case "patentName" : {
                    patentWrapper.eq(Patent::getPatentName, kv.getValue());
                    break;
                }
                case "patentType" : {
                    if (Integer.valueOf(kv.getValue()).equals(0)) break;
                    patentWrapper.eq(Patent::getPatentType, kv.getValue());
                    break;
                }
                case "patentCode" : {
                    patentWrapper.eq(Patent::getPatentCode, kv.getValue());
                    break;
                }
                case "currentProgram" : {
                    if (Integer.valueOf(kv.getValue()).equals(0)) break;
                    patentWrapper.eq(Patent::getCurrentProgram, kv.getValue());
                    break;
                }
                case "rightStatus" : {
                    if (Integer.valueOf(kv.getValue()).equals(0)) break;
                    patentWrapper.eq(Patent::getRightStatus, kv.getValue());
                    break;
                }
                case "inventorName" : {
                    patentInventorWrapper.eq(PatentInventor::getInventorName, request.getInventorName());
                    break;
                }
                case "departmentName" : {
                    if (Integer.valueOf(kv.getValue()).equals(0)) break;
                    Department department = departmentService.findDepartmentByDepartmentName(kv.getKey());
                    patentWrapper.eq(Patent::getDepartmentId, department.getId());
                    break;
                }
                default:break;
            }
        }
        Map<String, Object> map = new HashMap<>();
        map.put("patentWrapper", patentWrapper);
        map.put("patentInventorWrapper", patentInventorWrapper);
        return map;
    }

    public LambdaQueryWrapper<Patent> getWrapperByGetPatentRequest(GetPatentRequest request) {
        LambdaQueryWrapper<Patent> wrapper = new LambdaQueryWrapper<>();

        if (StringUtils.isNotBlank(request.getPatentName())) {
            wrapper.eq(Patent::getPatentName, request.getPatentName());
        }
        if (StringUtils.isNotBlank(request.getPatentCode())) {
            wrapper.eq(Patent::getPatentCode, request.getPatentCode());
        }
        if (StringUtils.isNotBlank(request.getPatentType())) {
            wrapper.eq(Patent::getPatentType, request.getPatentType());
        }
        if (StringUtils.isNotBlank(request.getAgency())) {
            wrapper.eq(Patent::getAgency, request.getAgency());
        }
        if (StringUtils.isNotBlank(request.getCurrentProgram())) {
            wrapper.eq(Patent::getCurrentProgram, request.getCurrentProgram());
        }
        if (StringUtils.isNotBlank(request.getRightStatus())) {
            wrapper.eq(Patent::getRightStatus, request.getRightStatus());
        }
        return wrapper;
    }
}
