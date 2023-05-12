package com.example.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.demo.Utils.CommonUtil;
import com.example.demo.Utils.HostHolder;
import com.example.demo.Utils.PageInfoUtil;
import com.example.demo.common.CommonResult;
import com.example.demo.entity.Department;
import com.example.demo.entity.Software;
import com.example.demo.entity.User;
import com.example.demo.mapper.SoftwareMapper;
import com.example.demo.request.GetSoftwareRequest;
import com.example.demo.request.NewSoftwareRequest;
import com.example.demo.response.GetSoftwareResponse;
import com.example.demo.service.DepartmentService;
import com.example.demo.service.SoftwareService;
import com.example.demo.service.UserService;
import com.example.demo.service.manager.SoftwareManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SoftwareServiceImpl implements SoftwareService {

    @Resource
    private DepartmentService departmentService;

    @Resource
    private UserService userService;

    @Resource
    private SoftwareMapper softwareMapper;

    @Resource
    private SoftwareManager softwareManager;

    @Resource
    private HostHolder hostHolder;

    @Override
    public Software findSoftwareByName(String softwareName) {
        return softwareMapper.selectOne(new LambdaQueryWrapper<Software>().eq(Software::getSoftwareName, softwareName));
    }


    @Override
    public CommonResult software(Integer pageIndex, Integer pageSize, Integer isDepartment) throws Exception {
        try {
            List<Software> softwareList = null;
            User user = hostHolder.getUser();
            if (isDepartment == 0) {
                softwareList = softwareMapper.selectList(new LambdaQueryWrapper<Software>().eq(Software::getDepartmentId, user.getDepartmentId()));
            } else {
                softwareList = softwareMapper.selectList(new LambdaQueryWrapper<Software>().eq(Software::getInventorId, user.getId()));
            }
            return CommonResult.success(PageInfoUtil.getPageInfo(softwareList.stream().map(software -> {
                        GetSoftwareResponse response = new GetSoftwareResponse();
                        response.setSoftwareName(software.getSoftwareName());
                        response.setSoftwareCode(software.getSoftwareCode());
                        response.setVersion(software.getVersion());
                        response.setInventorName(userService.findUserByUserId(software.getInventorId()).getUserName());
                        response.setDevelopWay(software.getDevelopWay());
                        response.setRegisterCode(software.getRegisterCode());
                        response.setCertificateCode(software.getCertificateCode());
                        response.setArchiveCode(software.getArchiveCode());
                        response.setRightStatus(software.getRightStatus());
                        response.setRightRange(software.getRightRange());
                        response.setFinishDate(CommonUtil.getYmdbyTimeStamp(software.getFinishDate()));
                        response.setApplicationDate(CommonUtil.getYmdbyTimeStamp(software.getApplicationDate()));
                        response.setCertificateDate(CommonUtil.getYmdbyTimeStamp(software.getCertificateDate()));
                        response.setPublishDate(CommonUtil.getYmdbyTimeStamp(software.getPublishDate()));
                        response.setArchiveDate(CommonUtil.getYmdbyTimeStamp(software.getArchiveDate()));
                        return response;
                    }).collect(Collectors.toList()),
                    pageIndex, pageSize), "查找成功");
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public CommonResult getSoftware(GetSoftwareRequest request) throws Exception {
        try {
            LambdaQueryWrapper<Software> wrapper = softwareManager.getWrapperByGetSoftwareRequest(request);
            List<Software> softwareList = softwareMapper.selectList(wrapper);
            return CommonResult.success(PageInfoUtil.getPageInfo(softwareList.stream().map(software -> {
                        GetSoftwareResponse response = new GetSoftwareResponse();
                        response.setSoftwareName(software.getSoftwareName());
                        response.setSoftwareCode(software.getSoftwareCode());
                        response.setVersion(software.getVersion());
                        response.setInventorName(userService.findUserByUserId(software.getInventorId()).getUserName());
                        response.setDevelopWay(software.getDevelopWay());
                        response.setRegisterCode(software.getRegisterCode());
                        response.setCertificateCode(software.getCertificateCode());
                        response.setArchiveCode(software.getArchiveCode());
                        response.setRightStatus(software.getRightStatus());
                        response.setRightRange(software.getRightRange());
                        response.setFinishDate(CommonUtil.getYmdbyTimeStamp(software.getFinishDate()));
                        response.setApplicationDate(CommonUtil.getYmdbyTimeStamp(software.getApplicationDate()));
                        response.setCertificateDate(CommonUtil.getYmdbyTimeStamp(software.getCertificateDate()));
                        response.setPublishDate(CommonUtil.getYmdbyTimeStamp(software.getPublishDate()));
                        response.setArchiveDate(CommonUtil.getYmdbyTimeStamp(software.getArchiveDate()));
                        return response;
                    }).collect(Collectors.toList()),
                    request.getPageIndex(), request.getPageSize()), "查找成功");
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public CommonResult newSoftware(NewSoftwareRequest request) throws Exception {
        try {
            Software software = this.findSoftwareByName(request.getSoftwareName());
            if (null != software) {
                return CommonResult.failed("该软著已存在");
            }
            software = new Software();
            software.setSoftwareName(request.getSoftwareName());
            software.setSoftwareCode(request.getSoftwareCode());
            software.setInventorId(userService.findUserByUserName(request.getInventorName()).getId());
            software.setAgency(request.getAgency());
            software.setVersion(request.getVersion());
            software.setDevelopWay(request.getDevelopWay());
            software.setRegisterCode(request.getRegisterCode());
            software.setCertificateCode(request.getCertificateCode());
            software.setArchiveCode(request.getArchiveCode());
            software.setApplicationDate(request.getApplicationDate() == null? null : CommonUtil.stringDateToTimeStamp(request.getApplicationDate()));
            software.setCertificateDate(request.getCertificateDate() == null? null : CommonUtil.stringDateToTimeStamp(request.getCertificateDate()));
            software.setArchiveDate(request.getArchiveDate() == null? null : CommonUtil.stringDateToTimeStamp(request.getArchiveDate()));
            software.setRightStatus(request.getRightStatus());
            software.setRightRange(request.getRightRange());
            software.setProposalDate(request.getProposalDate() == null? null : CommonUtil.stringDateToTimeStamp(request.getProposalDate()));
            software.setFinishDate(request.getFinishDate() == null? null : CommonUtil.stringDateToTimeStamp(request.getFinishDate()));
            software.setPublishDate(request.getPublishDate() == null? null : CommonUtil.stringDateToTimeStamp(request.getPublishDate()));
            softwareMapper.insert(software);
            return CommonResult.success(null, "添加软著成功");
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw new Exception(e.getMessage());
        }
    }
}
