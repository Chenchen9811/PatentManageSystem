package com.example.demo.service.manager;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.example.demo.Utils.CommonUtil;
import com.example.demo.entity.Patent;
import com.example.demo.entity.PatentAnnualFee;
import com.example.demo.entity.PatentFile;
import com.example.demo.entity.PatentOfficialFee;
import com.example.demo.request.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class PatentManager {

    public LambdaQueryWrapper<PatentFile> getWrapper(GetPatentFileInfoRequest request) {
        LambdaQueryWrapper<PatentFile> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(request.getFileName())) {
            wrapper.eq(PatentFile::getFileName, request.getFileName());
        }
        if (StringUtils.isNotBlank(request.getUploadDateBegin()) && StringUtils.isNotBlank(request.getUploadDateEnd())) {
            wrapper.between(PatentFile::getUploadDate, request.getUploadDateBegin(), request.getUploadDateEnd());
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
        if (StringUtils.isNotBlank(request.getApplicationCode())) {
            wrapper.eq(Patent::getApplicationCode, request.getApplicationCode());
        }
        if (StringUtils.isNotBlank(request.getGrantCode())) {
            wrapper.eq(Patent::getGrantCode, request.getGrantCode());
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
        if (StringUtils.isNotBlank(request.getApplicationBeginDate()) && StringUtils.isNotBlank(request.getApplicationEndDate())) {
            wrapper.between(Patent::getApplicationDate, request.getApplicationBeginDate(), request.getApplicationEndDate());
        }
        if (StringUtils.isNotBlank(request.getGrantStartDate()) && StringUtils.isNotBlank(request.getGrantEndDate())) {
            wrapper.between(Patent::getGrantDate, request.getGrantStartDate(), request.getGrantEndDate());
        }
        return wrapper;
    }
}
