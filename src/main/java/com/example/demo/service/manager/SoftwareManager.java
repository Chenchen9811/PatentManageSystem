package com.example.demo.service.manager;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.demo.entity.Software;
import com.example.demo.request.GetSoftwareRequest;
import com.example.demo.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;

@Service
public class SoftwareManager {

    @Resource
    private UserService userService;

    public LambdaQueryWrapper<Software> getWrapperByGetSoftwareRequest(GetSoftwareRequest request) {
        LambdaQueryWrapper<Software> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(request.getSoftwareName())) {
            wrapper.eq(Software::getSoftwareName, request.getSoftwareName());
        }
        if (StringUtils.isNotBlank(request.getSoftwareCode())) {
            wrapper.eq(Software::getSoftwareCode, request.getSoftwareCode());
        }
        if (StringUtils.isNotBlank(request.getVersion())) {
            wrapper.eq(Software::getVersion, request.getVersion());
        }
        if (StringUtils.isNotBlank(request.getInventorName())) {
            wrapper.eq(Software::getInventorId, userService.findUserByUserName(request.getInventorName()).getId());
        }
        if (StringUtils.isNotBlank(request.getAgency())) {
            wrapper.eq(Software::getAgency, request.getAgency());
        }
        if (StringUtils.isNotBlank(request.getRegisterCode())) {
            wrapper.eq(Software::getRegisterCode, request.getRegisterCode());
        }
        if (StringUtils.isNotBlank(request.getCertificateCode())) {
            wrapper.eq(Software::getCertificateCode, request.getCertificateCode());
        }
        if (StringUtils.isNotBlank(request.getArchiveCode())) {
            wrapper.eq(Software::getArchiveCode, request.getArchiveCode());
        }
        if (StringUtils.isNotBlank(request.getRightStatus())) {
            wrapper.eq(Software::getRightStatus, request.getRightStatus());
        }
        if (StringUtils.isNotBlank(request.getRightRange())) {
            wrapper.eq(Software::getRightRange, request.getRightRange());
        }
        if (StringUtils.isNotBlank(request.getProposalBeginDate()) && StringUtils.isNotBlank(request.getProposalEndDate())) {
            wrapper.between(Software::getProposalDate, request.getProposalBeginDate(), request.getProposalEndDate());
        }
        if (StringUtils.isNotBlank(request.getApplicationBeginDate()) && StringUtils.isNotBlank(request.getApplicationEndDate())) {
            wrapper.between(Software::getApplicationDate, request.getApplicationBeginDate(), request.getApplicationEndDate());
        }
        if (StringUtils.isNotBlank(request.getCertificateBeginDate()) && StringUtils.isNotBlank(request.getCertificateEndDate())) {
            wrapper.between(Software::getCertificateDate, request.getCertificateBeginDate(), request.getCertificateEndDate());
        }
        if (StringUtils.isNotBlank(request.getPublishBeginDate()) && StringUtils.isNotBlank(request.getPublishEndDate())) {
            wrapper.between(Software::getPublishDate, request.getPublishBeginDate(), request.getPublishEndDate());
        }
        if (StringUtils.isNotBlank(request.getArchiveBeginDate()) && StringUtils.isNotBlank(request.getArchiveEndDate())) {
            wrapper.between(Software::getArchiveDate, request.getArchiveBeginDate(), request.getArchiveEndDate());
        }
        if (StringUtils.isNotBlank(request.getFinishBeginDate()) && StringUtils.isNotBlank(request.getFinishEndDate())) {
            wrapper.between(Software::getFinishDate, request.getFinishBeginDate(), request.getFinishEndDate());
        }
        return wrapper;
    }
}
