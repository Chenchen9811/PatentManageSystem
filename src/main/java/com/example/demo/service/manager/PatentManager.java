package com.example.demo.service.manager;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.demo.entity.Patent;
import com.example.demo.request.GetPatentRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class PatentManager {

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
