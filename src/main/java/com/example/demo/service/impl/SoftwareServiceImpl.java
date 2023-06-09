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
import java.util.*;
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

    @Resource
    private DepartmentService departmentService;


    @Transactional(rollbackFor = Exception.class)
    @Override
    public CommonResult updateOfficialFee(UpdateSoftwareOfficialFeeRequest request) {
        try {
            SoftwareOfficialFee fee = officialFeeMapper.selectById(Long.valueOf(request.getId()));
            fee.setOfficialFeeName(request.getFeeName());
            fee.setPayStatus(request.getOfficialFeeStatus());
            fee.setDueAmount(request.getDueAmount());
            fee.setActualAmount(request.getActualPay());
            fee.setDueDate(CommonUtil.stringToDate(request.getDueDate()));
            fee.setActualPayDate(CommonUtil.stringToDate(request.getActualPayDate()));
            fee.setRemark(request.getRemark());
            return officialFeeMapper.updateById(fee) == 0 ? CommonResult.failed("编辑失败") : CommonResult.success(null, "编辑成功");
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw e;
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public CommonResult deleteFile(String fileId) {
        try {
            return fileMapper.deleteById(Long.valueOf(fileId)) == 0 ? CommonResult.failed("删除失败") : CommonResult.success(null, "删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public CommonResult updateBonus(UpdateSoftwareBonusRequest request) {
        try {
            SoftwareBonus bonus = this.findBonusById(request.getBonusId());
            bonus.setSoftwareId(this.findSoftwareByCode(request.getSoftwareCode()).getId());
//            bonus.setBonusAmount(request.getBonusAmount());
            bonus.setBonusType(request.getBonusType());
            bonus.setReleaseStatus(request.getReleaseStatus());
            bonus.setActualRelease(request.getActualRelease());
            bonus.setInventorName(request.getInventorName());
            return bonusMapper.updateById(bonus) == 0 ?
                    CommonResult.failed("编辑失败") : CommonResult.success(null, "编辑成功");
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw e;
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public CommonResult deleteBonus(String id) {
        try {
            return bonusMapper.deleteById(Long.parseLong(id)) == 0 ?
                    CommonResult.failed("删除失败") : CommonResult.success(null, "删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw e;
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public CommonResult newBonus(NewSoftwareBonusRequest request) {
        try {
            Software software = this.findSoftwareByCode(request.getSoftwareCode());
            List<SoftwareBonus> bonusList = new ArrayList<>();
            List<NewSoftwareBonusRequest.inventor> inventorList = request.getListOfInventor();
            int size = inventorList.size();
            for (int i = 0; i < size; i++) {
                NewSoftwareBonusRequest.inventor inventor = inventorList.get(i);
                SoftwareBonus bonus = new SoftwareBonus();
                bonus.setRanking(i + 1);
                bonus.setBonusAmount(request.getBonusAmount());
                bonus.setSoftwareId(software.getId());
                bonus.setActualRelease(inventor.getActualRelease());
                bonus.setReleaseStatus(request.getReleaseStatus());
                bonus.setInventorName(inventor.getInventorName());
                bonus.setBonusType(request.getBonusType());
                bonusList.add(bonus);
            }
            return bonusMapper.insertBatchSomeColumn(bonusList) == 0 ? CommonResult.failed("添加失败") : CommonResult.success(null, "添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public CommonResult getList(GetSoftwareBonusRequest request) {
        try {
            List<Software> softwareList = null;
            List<SoftwareBonus> bonusList = null;
            List<User> inventorList = null;
            bonusList = bonusMapper.selectList(softwareManager.getBonusWrapper(request));
            List<Long> softwareIds = bonusList.stream().map(SoftwareBonus::getSoftwareId).distinct().collect(Collectors.toList());
            if (bonusList.size() == 0) {
                return CommonResult.failed("没有找到相关软著奖金的信息");
            }
            softwareList = this.findSoftwareListByIds(softwareIds);
            if (softwareList.size() == 0) {
                return CommonResult.failed("没有找到相关软著信息或没有符合查询条件的软著奖金");
            }
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
                response.setBonusId(String.valueOf(bonus.getId()));
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
            List<Long> softwareIds = fileList.stream().map(SoftwareFile::getSoftwareId).distinct().collect(Collectors.toList());
            softwareList = softwareList.stream().filter(software -> softwareIds.contains(software.getId())).collect(Collectors.toList());
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
                response.setFileId(String.valueOf(file.getId()));
                response.setSoftwareCode(software.getSoftwareCode());
                response.setSoftwareName(software.getSoftwareName());
                response.setFileType(file.getFileType());
                response.setUploadDate(file.getUploadDate().toString());
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
    public CommonResult deleteOfficialFee(String id) {
        try {
            // 验证是否存在
            SoftwareOfficialFee officialFee = officialFeeMapper.selectById(Long.valueOf(id));
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
            LambdaQueryWrapper<Software> softwareWrapper = softwareManager.getSoftwareWrapper(request);
            List<Software> softwareList = softwareMapper.selectList(softwareWrapper);
            if (softwareList.size() == 0) {
                return CommonResult.failed("查找失败，没有找到相关软著信息");
            }
            LambdaQueryWrapper<SoftwareOfficialFee> wrapper = softwareManager.getFeeWrapper(request);
            List<SoftwareOfficialFee> officialFeeList = officialFeeMapper.selectList(wrapper);
            if (officialFeeList.size() == 0) {
                return CommonResult.failed("查找失败，没有找到相关软著奖金信息");
            }
            Set<Long> softwareIds = softwareList.stream().map(Software::getId).collect(Collectors.toSet());
            officialFeeList.removeIf(fee -> !softwareIds.contains(fee.getSoftwareId()));
            if (officialFeeList.size() == 0) {
                return CommonResult.failed("查找失败，没有找到满足条件的软著奖金信息");
            }
            Map<Long, Long> totalAmountMap = new HashMap<>();
            for (SoftwareOfficialFee fee : officialFeeList) {
                totalAmountMap.merge(fee.getSoftwareId(), Long.parseLong(fee.getActualAmount()), Long::sum);
            }
            Map<Long, Software> softwareMap = softwareList.stream().collect(Collectors.toMap(Software::getId, software -> software));
            List<Criteria.KV> items = request.getCriteria().getItems();
            Long totalAmount = null;
            for (Criteria.KV kv : items) {
                if (kv.getKey().equals("totalAmount")) {
                    totalAmount = Long.parseLong(kv.getValue());
                    break;
                }
            }
            if (null != totalAmount) {
                Long softwareId = null;
                boolean isTotalExist = false;
                Collection<Long> values = totalAmountMap.values();
                for (Long total : values) {
                    if (total.equals(totalAmount)) {
                        isTotalExist = true;
                        // 查找总金额对上的softwareId
                        for (Object key : totalAmountMap.keySet()) {
                            if (totalAmountMap.get(key).equals(totalAmount)) {
                                softwareId = (Long) key;
                                break;
                            }
                        }
                        break;
                    }
                }
                if (isTotalExist) {
                    Long finalSoftwareId = softwareId;
                    officialFeeList.removeIf(fee -> !fee.getSoftwareId().equals(finalSoftwareId));
                    List<GetSoftwareOfficialFeeResponse> responseList = officialFeeList.stream().map(fee -> {
                        GetSoftwareOfficialFeeResponse response = new GetSoftwareOfficialFeeResponse();
                        Software software = softwareMap.get(fee.getSoftwareId());
                        Long total = totalAmountMap.get(finalSoftwareId);
                        response.setFeeName(fee.getOfficialFeeName());
                        response.setDueDate(fee.getDueAmount());
                        response.setActualPay(fee.getActualAmount());
                        response.setOfficialFeeStatus(fee.getPayStatus());
                        response.setSoftwareCode(software.getSoftwareCode());
                        response.setSoftwareName(software.getSoftwareName());
                        response.setActualPayDate(fee.getActualPayDate().toString());
                        response.setRemark(fee.getRemark());
                        response.setTotalAmount(String.valueOf(total));
                        response.setId(String.valueOf(fee.getId()));
                        response.setDueAmount(fee.getDueAmount());
                        response.setPayStatus(fee.getPayStatus());
                        response.setOfficialFeeCode(fee.getOfficialFeeCode());
                        return response;
                    }).collect(Collectors.toList());
                    return CommonResult.success(PageInfoUtil.getPageInfo(responseList, request.getPageIndex(), request.getPageSize()), "查找成功");
                }
                return CommonResult.failed("查找失败，没有找到符合总金额的软著官费");
            }
            List<GetSoftwareOfficialFeeResponse> responseList = officialFeeList.stream().map(fee -> {
                GetSoftwareOfficialFeeResponse response = new GetSoftwareOfficialFeeResponse();
                Software software = softwareMap.get(fee.getSoftwareId());
                Long total = totalAmountMap.get(fee.getSoftwareId());
                response.setFeeName(fee.getOfficialFeeName());
                response.setDueDate(fee.getDueAmount());
                response.setActualPay(fee.getActualAmount());
                response.setOfficialFeeStatus(fee.getPayStatus());
                response.setSoftwareCode(software.getSoftwareCode());
                response.setSoftwareName(software.getSoftwareName());
                response.setActualPayDate(fee.getActualPayDate().toString());
                response.setRemark(fee.getRemark());
                response.setTotalAmount(String.valueOf(total));
                response.setId(String.valueOf(fee.getId()));
                response.setDueAmount(fee.getDueAmount());
                response.setPayStatus(fee.getPayStatus());
                response.setOfficialFeeCode(fee.getOfficialFeeCode());
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
    public CommonResult newOfficialFee(NewSoftwareOfficialFeeRequest request) {
        try {
            // 验重
            SoftwareOfficialFee officialFee = this.findOfficialFeeByName(request.getOfficialFeeName());
            if (null != officialFee) {
                return CommonResult.failed("该软著官费已存在");
            }
            Software software = this.findSoftwareByCode(request.getSoftwareCode());
            if (software == null) {
                return CommonResult.failed("没有相关软著信息");
            }
            officialFee = new SoftwareOfficialFee();
            officialFee.setOfficialFeeName(request.getFeeName());
            officialFee.setOfficialFeeCode(CommonUtil.generateCode("SoftwareOFee"));
            officialFee.setPayStatus(request.getOfficialFeeStatus());
            officialFee.setActualAmount(request.getActualPay());
            officialFee.setDueAmount(request.getDueAmount());
            officialFee.setActualPayDate(CommonUtil.stringToDate(request.getActualPayDate()));
            officialFee.setDueDate(CommonUtil.stringToDate(request.getDueDate()));
            officialFee.setSoftwareId(software.getId());
            officialFee.setRemark(request.getRemark());
//            officialFee.setPayStatus(request.getPayStatus());
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
    public List<GetSoftwareResponse> getSoftware(GetSoftwareRequest request) throws Exception {
        try {
            LambdaQueryWrapper<Software> wrapper = softwareManager.getWrapper(request);
            List<Software> softwareList = softwareMapper.selectList(wrapper);
            if (softwareList.size() == 0) {
                throw new Exception("查找失败，没有相关软著信息");
            }
            List<User> inventorList = userService.findUserListByIds(softwareList.stream().map(Software::getInventorId).distinct().collect(Collectors.toList()));
            Map<Long, User> inventorMap = new HashMap<>();
            for (User inventor : inventorList) {
                inventorMap.put(inventor.getId(), inventor);
            }
            return softwareList.stream().map(software -> {
                GetSoftwareResponse response = new GetSoftwareResponse();
                response.setSoftwareName(software.getSoftwareName());
                response.setSoftwareCode(software.getSoftwareCode());
                response.setVersion(software.getVersion());
                response.setInventorName(inventorMap.get(software.getInventorId()).getUserName());
                response.setDevelopWay(software.getDevelopWay());
                response.setRegisterCode(software.getRegisterCode());
                response.setCertificateCode(software.getCertificateCode());
                response.setArchiveCode(software.getArchiveCode());
                response.setRightStatus(software.getRightStatus());
                response.setRightRange(software.getRightRange());
                response.setFinishDate(software.getFinishDate() == null ? null : CommonUtil.getYmdbyTimeStamp(software.getFinishDate()));
                response.setApplicationDate(software.getApplicationDate() == null ? null : CommonUtil.getYmdbyTimeStamp(software.getApplicationDate()));
                response.setCertificateDate(software.getCertificateDate() == null ? null : CommonUtil.getYmdbyTimeStamp(software.getCertificateDate()));
                response.setPublishDate(software.getPublishDate() == null ? null : CommonUtil.getYmdbyTimeStamp(software.getPublishDate()));
                response.setArchiveDate(software.getArchiveDate() == null ? null : CommonUtil.getYmdbyTimeStamp(software.getArchiveDate()));
                return response;
            }).collect(Collectors.toList());
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
            Department department = departmentService.findDepartmentByDepartmentName(request.getDepartmentName());
            software.setSoftwareName(request.getSoftwareName());
            software.setSoftwareCode(request.getSoftwareCode());
            User user = userService.findUserByUserName(request.getInventorName());
            if (null == user) {
                user = CommonUtil.generateUser(request.getInventorName(), hostHolder.getUser().getDepartmentId());
                userService.insertUser(user);
            }
            software.setInventorId(user.getId());
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
            software.setDepartmentId(department.getId());
            return softwareMapper.insert(software) != 0 ? CommonResult.success(null, "添加软著成功") : CommonResult.failed("添加软著失败");
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw e;
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

    @Override
    public SoftwareBonus findBonusById(Long id) {
        return bonusMapper.selectById(id);
    }

    @Override
    public List<Software> findSoftwareListByIds(List<Long> ids) {
        return softwareMapper.selectBatchIds(ids);
    }
}
