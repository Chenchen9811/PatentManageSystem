package com.example.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.demo.Utils.CommonUtil;
import com.example.demo.Utils.HostHolder;
import com.example.demo.Utils.PageInfoUtil;
import com.example.demo.common.CommonResult;
import com.example.demo.entity.Department;
import com.example.demo.entity.Software;
import com.example.demo.entity.SoftwareOfficialFee;
import com.example.demo.entity.User;
import com.example.demo.mapper.SoftwareMapper;
import com.example.demo.mapper.SoftwareOfficialFeeMapper;
import com.example.demo.request.GetSoftwareOfficialFeeRequest;
import com.example.demo.request.GetSoftwareRequest;
import com.example.demo.request.NewSoftwareOfficialFeeRequest;
import com.example.demo.request.NewSoftwareRequest;
import com.example.demo.response.GetSoftwareOfficialFeeResponse;
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

    @Resource
    private SoftwareOfficialFeeMapper officialFeeMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public CommonResult deleteOfficialFee(String officialFeeCode) {
        try {
            // 验证是否存在
            SoftwareOfficialFee officialFee = this.findOfficialFeeByCode(officialFeeCode);
            if (null == officialFee) {
                return CommonResult.failed("要删除的官费不存在");
            }
            return officialFeeMapper.deleteById(officialFee.getId()) != 0 ? CommonResult.success(null, "删除成功") : CommonResult.failed("删除失败");
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public CommonResult getOfficialFee(GetSoftwareOfficialFeeRequest request) {
        try {
            LambdaQueryWrapper<SoftwareOfficialFee> wrapper = softwareManager.getWrapper(request);
            List<SoftwareOfficialFee> officialFeeList = officialFeeMapper.selectList(wrapper);
            List<GetSoftwareOfficialFeeResponse> list = officialFeeList.stream().map(fee -> {
                GetSoftwareOfficialFeeResponse response = new GetSoftwareOfficialFeeResponse();
                response.setOfficialFeeCode(fee.getOfficialFeeCode());
                response.setOfficialFeeName(fee.getOfficialFeeName());
                response.setActualAmount(fee.getActualAmount());
                response.setDueAmount(fee.getDueAmount());
                response.setDueDate(fee.getDueDate().toString());
                response.setActualPayDate(fee.getActualPayDate().toString());
                response.setPayStatus(fee.getPayStatus());
                response.setRemark(fee.getRemark());
                return response;
            }).collect(Collectors.toList());
            return CommonResult.success(PageInfoUtil.getPageInfo(list, request.getPageIndex(), request.getPageSize()), "查找成功");
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw e;
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public CommonResult newOfficialFee(NewSoftwareOfficialFeeRequest request) {
        try {
            // 验重
            SoftwareOfficialFee officialFee = this.findOfficialFeeByName(request.getOfficialFeeName());
            if (null != officialFee) {
                return CommonResult.failed("该软著官费已存在");
            }
            Software software = this.findSoftwareByCode(request.getSoftwareCode());
            officialFee = new SoftwareOfficialFee();
            officialFee.setOfficialFeeName(request.getOfficialFeeName());
            officialFee.setOfficialFeeCode(CommonUtil.generateCode("SoftwareOFee"));
            officialFee.setActualAmount(request.getActualAmount());
            officialFee.setDueAmount(request.getDueAmount());
            officialFee.setActualPayDate(CommonUtil.stringToDate(request.getActualPayDate()));
            officialFee.setDueDate(CommonUtil.stringToDate(request.getDueDate()));
            officialFee.setSoftwareId(software.getId());
            officialFee.setRemark(request.getRemark());
            officialFee.setPayStatus(request.getPayStatus());
            return officialFeeMapper.insert(officialFee) != 0 ?
                    CommonResult.success(null, "新增软著官费成功") : CommonResult.failed("新增软著官费失败");
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw e;
        }
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
            software.setApplicationDate(request.getApplicationDate() == null ? null : CommonUtil.stringDateToTimeStamp(request.getApplicationDate()));
            software.setCertificateDate(request.getCertificateDate() == null ? null : CommonUtil.stringDateToTimeStamp(request.getCertificateDate()));
            software.setArchiveDate(request.getArchiveDate() == null ? null : CommonUtil.stringDateToTimeStamp(request.getArchiveDate()));
            software.setRightStatus(request.getRightStatus());
            software.setRightRange(request.getRightRange());
            software.setProposalDate(request.getProposalDate() == null ? null : CommonUtil.stringDateToTimeStamp(request.getProposalDate()));
            software.setFinishDate(request.getFinishDate() == null ? null : CommonUtil.stringDateToTimeStamp(request.getFinishDate()));
            software.setPublishDate(request.getPublishDate() == null ? null : CommonUtil.stringDateToTimeStamp(request.getPublishDate()));
            softwareMapper.insert(software);
            return CommonResult.success(null, "添加软著成功");
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public Software findSoftwareByName(String softwareName) {
        return softwareMapper.selectOne(new LambdaQueryWrapper<Software>().eq(Software::getSoftwareName, softwareName));
    }

    @Override
    public SoftwareOfficialFee findOfficialFeeByName(String officialFeeName) {
        return officialFeeMapper.selectOne(new LambdaQueryWrapper<SoftwareOfficialFee>().eq(SoftwareOfficialFee::getOfficialFeeName, officialFeeName));
    }

    @Override
    public Software findSoftwareByCode(String softwareCode) {
        return softwareMapper.selectOne(new LambdaQueryWrapper<Software>().eq(Software::getSoftwareCode, softwareCode));
    }

    @Override
    public SoftwareOfficialFee findOfficialFeeByCode(String officialFeeCode) {
        return officialFeeMapper.selectOne(new LambdaQueryWrapper<SoftwareOfficialFee>().eq(SoftwareOfficialFee::getOfficialFeeCode, officialFeeCode));
    }
}