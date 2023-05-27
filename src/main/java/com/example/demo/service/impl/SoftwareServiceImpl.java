package com.example.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.demo.Utils.CommonUtil;
import com.example.demo.Utils.HostHolder;
import com.example.demo.Utils.PageInfoUtil;
import com.example.demo.common.CommonResult;
import com.example.demo.entity.*;
import com.example.demo.mapper.SoftwareBonusMapper;
import com.example.demo.mapper.SoftwareFileMapper;
import com.example.demo.mapper.SoftwareMapper;
import com.example.demo.mapper.SoftwareOfficialFeeMapper;
import com.example.demo.request.*;
import com.example.demo.response.GetSoftwareBonusResponse;
import com.example.demo.response.GetSoftwareFileInfoResponse;
import com.example.demo.response.GetSoftwareOfficialFeeResponse;
import com.example.demo.response.GetSoftwareResponse;
import com.example.demo.service.DepartmentService;
import com.example.demo.service.ProposalService;
import com.example.demo.service.SoftwareService;
import com.example.demo.service.UserService;
import com.example.demo.service.manager.SoftwareManager;
import com.sun.corba.se.spi.copyobject.CopierManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SoftwareServiceImpl implements SoftwareService {

    @Resource
    private ProposalService proposalService;

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

    @Resource
    private SoftwareFileMapper fileMapper;

    @Resource
    private SoftwareBonusMapper bonusMapper;

    @Override
    public CommonResult getList(GetSoftwareBonusRequest request) {
        try {
            List<Software> softwareList = null;
            List<SoftwareBonus> bonusList = null;
            List<User> inventorList = null;
            softwareList = softwareMapper.selectList(softwareManager.getSoftwareWrapper(request));
            if (softwareList.size() == 0) {
                return CommonResult.failed("没有找到相关软著信息或没有符合查询条件的软著奖金");
            }
            bonusList = bonusMapper.selectList(softwareManager.getBonusWrapper(request));
            List<Long> softwareIds = bonusList.stream().map(SoftwareBonus::getSoftwareId).distinct().collect(Collectors.toList());
            if (bonusList.size() == 0) {
                return CommonResult.failed("没有找到相关软著奖金的信息");
            }
            softwareList = softwareList.stream().filter(software -> softwareIds.contains(software.getId())).collect(Collectors.toList());
            inventorList = userService.findUserListByIds(softwareList.stream().map(Software::getInventorId).collect(Collectors.toList()));
            if (inventorList.size() == 0) {
                return CommonResult.failed("相关发明人信息缺失");
            }
            Map<Long, Software> softwareMap = new HashMap<>();
            Map<String, User> inventorMap = new HashMap<>();
            for (Software software : softwareList) {
                softwareMap.put(software.getId(), software);
            }
            for (User inventor : inventorList) {
                inventorMap.put(inventor.getUserName(), inventor);
            }
            List<GetSoftwareBonusResponse> responseList = bonusList.stream().map(bonus -> {
                GetSoftwareBonusResponse response = new GetSoftwareBonusResponse();
                Software software = softwareMap.get(bonus.getSoftwareId());
                User inventor = inventorMap.get(bonus.getInventorName());
                response.setSoftwareName(software.getSoftwareName());
                response.setSoftwareCode(software.getSoftwareCode());
                response.setBonusAmount(bonus.getBonusAmount());
                response.setBonusType(bonus.getBonusType());
                response.setActualRelease(bonus.getActualRelease());
                response.setRanking(bonus.getRanking());
                response.setVersion(software.getVersion());
                response.setInventorName(inventor.getUserName());
                response.setReleaseStatus(bonus.getReleaseStatus());
                response.setBonusId(bonus.getId());
                return response;
            }).collect(Collectors.toList());
            return CommonResult.success(PageInfoUtil.getPageInfo(responseList, request.getPageIndex(), request.getPageSize()), "查找成功");
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public CommonResult getFileInfo(GetSoftwareFileInfoRequest request) {
        try {
            LambdaQueryWrapper<Software> wrapper = softwareManager.getWrapper(request);
            List<Software> softwareList = null;
            List<Proposal> proposalList = null;
            List<SoftwareFile> fileList = null;
            List<User> uploaderList = null;
            // 查询满足条件的softwareList
            softwareList = softwareMapper.selectList(wrapper);
            if (softwareList.size() == 0) return CommonResult.failed("查找不到相关软著");
            // 查询满足File条件的softwareFileList
            LambdaQueryWrapper<SoftwareFile> fileWrapper = softwareManager.getFileWrapper(request);
            fileList = fileMapper.selectList(fileWrapper);
            if (fileList.size() == 0) return CommonResult.failed("暂无对应文件");
            List<Long> softwareIds = fileList.stream().map(SoftwareFile::getSoftwareId).collect(Collectors.toList());
            softwareList = softwareList.stream().filter(software -> softwareIds.contains(software.getId())).collect(Collectors.toList());
//            List<Long> proposalIds = new ArrayList<>();
//            for (Software software : softwareList) {
//                if (softwareIds.contains(software.getId())) proposalIds.add(software.getProposalId());
//            }
            if (softwareList.size() == 0) return CommonResult.failed("没有找到符合条件的软著");
            proposalList = proposalService.findProposalListByIds(softwareList.stream().map(Software::getProposalId).collect(Collectors.toList()));
            if (proposalList.size() == 0) return CommonResult.failed("查找的软著没有对应的提案");
            uploaderList = userService.findUserListByIds(fileList.stream().map(SoftwareFile::getUploaderId).distinct().collect(Collectors.toList()));
            Map<Long, Software> softwareMap = new HashMap<>();
            Map<Long, Proposal> proposalMap = new HashMap<>();
            Map<Long, User> uploaderMap = new HashMap<>();
            for (Software software : softwareList) {
                softwareMap.put(software.getId(), software);
            }
            for (Proposal proposal : proposalList) {
                proposalMap.put(proposal.getId(), proposal);
            }
            for (User uploader : uploaderList) {
                uploaderMap.put(uploader.getId(), uploader);
            }
            List<GetSoftwareFileInfoResponse> responseList = fileList.stream().map(file -> {
                GetSoftwareFileInfoResponse response = new GetSoftwareFileInfoResponse();
                Software software = softwareMap.get(file.getSoftwareId());
                Proposal proposal = proposalMap.get(software.getProposalId());
                User uploader = uploaderMap.get(file.getUploaderId());
                response.setFileName(file.getFileName());
                response.setProposalDate(CommonUtil.getYmdbyTimeStamp(proposal.getProposalDate()));
                response.setDevelopWay(software.getDevelopWay());
                response.setVersion(software.getVersion());
                response.setRightRange(software.getRightRange());
                response.setUploaderName(uploader.getUserName());
                response.setProposerName(proposal.getProposerName());
                return response;
            }).collect(Collectors.toList());
            return CommonResult.success(PageInfoUtil.getPageInfo(responseList, request.getPageIndex(), request.getPageSize()), "查找成功");
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw e;
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public CommonResult newFileInfo(NewSoftwareFileInfoRequest request) {
        try {
            // 验重
            SoftwareFile file = this.findFileByName(request.getFileName());
            if (null != file) {
                return CommonResult.failed("该文件已存在");
            }
            Software software = this.findSoftwareByCode(request.getSoftwareCode());
            file = new SoftwareFile();
            file.setFileName(request.getFileName());
            file.setFileType(request.getFileType());
            file.setUploadDate(new Date(System.currentTimeMillis()));
            file.setSoftwareId(software.getId());
            file.setFileUrl(CommonUtil.getFileUrl(request.getFileName()));
            file.setUploaderId(hostHolder.getUser().getId());
            file.setFileStatus(request.getFileStatus() == null ? null : request.getFileStatus());
            return fileMapper.insert(file) != 0 ? CommonResult.success(null, "添加文件成功") : CommonResult.failed("添加文件失败");
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw e;
        }
    }

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
            LambdaQueryWrapper<Software> wrapper = softwareManager.getWrapper(request);
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

    @Override
    public SoftwareFile findFileByName(String fileName) {
        return fileMapper.selectOne(new LambdaQueryWrapper<SoftwareFile>().eq(SoftwareFile::getFileName, fileName));
    }
}
