package com.example.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.example.demo.Utils.CommonUtil;
import com.example.demo.Utils.HostHolder;
import com.example.demo.Utils.PageInfoUtil;
import com.example.demo.common.CommonResult;
import com.example.demo.entity.*;
import com.example.demo.mapper.*;
import com.example.demo.request.*;
import com.example.demo.response.*;
import com.example.demo.service.DepartmentService;
import com.example.demo.service.PatentService;
import com.example.demo.service.ProposalService;
import com.example.demo.service.UserService;
import com.example.demo.service.manager.PatentManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PatentServiceImpl implements PatentService {
    @Resource
    private PatentMapper patentMapper;

    @Resource
    private PatentInventorMapper patentInventorMapper;

    @Resource
    private UserService userService;

    @Resource
    private HostHolder hostHolder;

    @Resource
    private PatentManager patentManager;

    @Resource
    private DepartmentService departmentService;

    @Resource
    private PatentOfficialFeeMapper patentOfficialFeeMapper;

    @Resource
    private ProposalService proposalService;

    @Resource
    private PatentAnnualFeeMapper annualFeeMapper;

    @Resource
    private PatentBonusMapper bonusMapper;

    @Resource
    private PatentFileMapper fileMapper;


    @Override
    public CommonResult getFileInfo(GetPatentFileInfoRequest request) {
        try {
            List<Patent> patentList = null;
            List<PatentFile> fileList = null;
            List<User> uploaderList = null;
            LambdaQueryWrapper<PatentFile> wrapper = patentManager.getFileWrapper(request);
            Map<Long, Patent> patentMap = new HashMap<>();
            Map<Long, User> uploaderMap = new HashMap<>();
            fileList = fileMapper.selectList(wrapper);
            if (fileList.size() == 0) {
                return CommonResult.failed("没有符合条件的文件");
            }
            patentList = this.findPatentListByIds(fileList.stream().map(PatentFile::getPatentId).distinct().collect(Collectors.toList()));
            uploaderList = userService.findUserListByIds(fileList.stream().map(PatentFile::getUploaderId).distinct().collect(Collectors.toList()));
            for (Patent patent : patentList) {
                patentMap.put(patent.getId(), patent);
            }
            for (User user : uploaderList) {
                uploaderMap.put(user.getId(), user);
            }
            List<GetPatentFileInfoResponse> responseList = fileList.stream().map(file -> {
                GetPatentFileInfoResponse response = new GetPatentFileInfoResponse();
                User uploader = uploaderMap.get(file.getUploaderId());
                Patent patent = patentMap.get(file.getPatentId());
                response.setFileName(file.getFileName());
                response.setFileStatus(file.getFileStatus());
                response.setFileType(file.getFileType());
                response.setPatentCode(patent.getPatentCode());
                response.setUploaderName(uploader.getUserName());
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
    public CommonResult newFileInfo(NewPatentFileInfoRequest request) {
        try {
            // 验重
            PatentFile file = this.findFileByName(request.getFileName());
            if (null != file) {
                return CommonResult.failed("该文件已存在");
            }
            file = new PatentFile();
            file.setFileName(request.getFileName());
            file.setFileStatus(request.getFileStatus());
            Patent patent = this.findPatentByCode(request.getPatentCode());
            file.setPatentId(patent.getId());
            file.setUploaderId(hostHolder.getUser().getId());
            file.setUploadDate(new Date(System.currentTimeMillis()));
            file.setFileUrl(CommonUtil.getFileUrl(request.getFileName()));
            file.setFileType(request.getFileType());
            return fileMapper.insert(file) != 0 ? CommonResult.success(null, "新增文件成功") : CommonResult.failed("新增文件失败");
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw e;
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public CommonResult updateBonus(UpdatePatentBonusRequest request) {
        try {
//            Patent patent = this.findPatentByCode(request.getPatentCode());
//            List<PatentBonus> bonusList = this.findBonusByPatentId(patent.getId());
//            if (0 != bonusList.size()) {
//                bonusMapper.deleteBatchIds(bonusList.stream().map(PatentBonus::getId).collect(Collectors.toList()));
//            }
            PatentBonus bonus = bonusMapper.selectOne(new LambdaQueryWrapper<PatentBonus>().eq(PatentBonus::getId, Long.parseLong(request.getBonusId())));
            bonus.setActualRelease(request.getActualRelease());
            bonus.setReleaseStatus(request.getReleaseStatus());
            bonus.setBonusType(request.getBonusType());
            bonus.setInventorName(request.getInventorName());
            return bonusMapper.updateById(bonus) == 0 ? CommonResult.failed("编辑失败") : CommonResult.success(null, "编辑成功");
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw e;
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public CommonResult deleteBonus(String bonusId) {
        try {
            return bonusMapper.deleteById(Long.parseLong(bonusId)) == 0 ? CommonResult.failed("删除失败") : CommonResult.success(null, "删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public CommonResult getBonus(GetPatentBonusListRequest request) {
        try {
            List<PatentBonus> bonusList = null;
            List<Patent> patentList = null;
            LambdaQueryWrapper<Patent> patentWrapper = patentManager.getPatentWrapper(request);
            patentList = patentMapper.selectList(patentWrapper);
            if (patentList.size() == 0) {
                return CommonResult.failed("没有符合条件的专利");
            }
            LambdaQueryWrapper<PatentBonus> bonusWrapper = patentManager.getBonusWrapper(request);
            bonusList = bonusMapper.selectList(bonusWrapper);
            if (bonusList.size() == 0) {
                return CommonResult.failed("没有符合条件的专利奖金");
            }
            // 专利奖金对应的专利id
            Set<Long> patentIds = new HashSet<>();
            for (PatentBonus bonus : bonusList) {
                patentIds.add(bonus.getPatentId());
            }
            // 取交集
            patentList.removeIf(patent -> !patentIds.contains(patent.getId()));
            Map<Long, Patent> patentMap = new HashMap<>();
            for (Patent patent : patentList) {
                patentMap.put(patent.getId(), patent);
            }
            return CommonResult.success(PageInfoUtil.getPageInfo(
                    bonusList.stream().map(bonus -> {
                        GetPatentBonusResponse response = new GetPatentBonusResponse();
                        Patent patent = patentMap.get(bonus.getPatentId());
                        response.setPatentBonusId(String.valueOf(bonus.getId()));
                        response.setBonusAmount(bonus.getBonusAmount());
                        response.setRanking(bonus.getRanking());
                        response.setPatentCode(patent.getPatentCode());
                        response.setBonusType(bonus.getBonusType());
                        response.setPatentType(patent.getPatentType());
                        response.setActualRelease(bonus.getActualRelease());
                        response.setReleaseStatus(bonus.getReleaseStatus());
                        response.setInventorName(bonus.getInventorName());
                        response.setPatentName(patent.getPatentName());
                        return response;
                    }).collect(Collectors.toList()), request.getPageIndex(), request.getPageSize()), "查找成功");
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw e;
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public CommonResult newBonus(NewPatentBonusRequest request) {
        try {
            // 验重
            Patent patent = this.findPatentByCode(request.getPatentCode());
            if (null == patent) {
                return CommonResult.failed("不存在该专利，请核对专利编号是否正确");
            }
            List<PatentBonus> bonusList = this.findBonusByPatentId(patent.getId());
            List<NewPatentBonusRequest.inventor> inventorList = request.getListOfInventor();
            Set<String> inventorNames = inventorList.stream().map(NewPatentBonusRequest.inventor::getInventorName).collect(Collectors.toSet());
            if (bonusList.size() != 0) {
                StringBuilder repeatNames = new StringBuilder();
                boolean isRepeat = false;
                for (PatentBonus patentBonus : bonusList) {
                    if (inventorNames.contains(patentBonus.getInventorName())) {
                        isRepeat = true;
                        repeatNames.append(patentBonus.getInventorName() + ",");
                    }
                }
                if (isRepeat) {
                    return CommonResult.failed(repeatNames.toString() + "已存在奖金");
                }
            }
            bonusList = new ArrayList<>();
            int size = inventorList.size();
            for (int i = 0; i < size; i++) {
                NewPatentBonusRequest.inventor inventor = inventorList.get(i);
                PatentBonus patentBonus = new PatentBonus();
                patentBonus.setPatentId(patent.getId());
                patentBonus.setBonusAmount(request.getBonusAmount());
                patentBonus.setInventorName(inventor.getInventorName());
                patentBonus.setActualRelease(inventor.getActualRelease());
                patentBonus.setBonusType(request.getBonusType());
                patentBonus.setReleaseStatus(request.getReleaseStatus());
                patentBonus.setRanking(i + 1);
                bonusList.add(patentBonus);
            }
            return bonusMapper.insertBatchSomeColumn(bonusList) == 0 ?
                    CommonResult.failed("新增失败") :
                    CommonResult.success(null, "新增成功");
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw e;
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public CommonResult deleteAnnualFee(String annualFeeCode) {
        try {
            return annualFeeMapper.delete(new LambdaQueryWrapper<PatentAnnualFee>().eq(PatentAnnualFee::getAnnualFeeCode, annualFeeCode)) == 0 ?
                    CommonResult.failed("删除失败") :
                    CommonResult.success(null, "删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw e;
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public CommonResult updateAnnualFee(UpdateAnnualFeeRequest request) {
        try {
            PatentAnnualFee annualFee = this.findPatentAnnualFeeByCode(request.getAnnualFeeCode());
            annualFee = patentManager.getUpdatedAnnualFee(request, annualFee);
            LambdaUpdateWrapper<PatentAnnualFee> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(PatentAnnualFee::getAnnualFeeCode, request.getAnnualFeeCode());
            return annualFeeMapper.update(annualFee, updateWrapper) == 0 ?
                    CommonResult.failed("编辑失败") : CommonResult.success(null, "编辑成功");
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public CommonResult getAnnualFeeCode(String patentCode, String year) {
        try {
            Patent patent = this.findPatentByCode(patentCode);
            LambdaQueryWrapper<PatentAnnualFee> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(PatentAnnualFee::getPatentId, patent.getId());
            wrapper.eq(PatentAnnualFee::getYear, year);
            PatentAnnualFee annualFee = annualFeeMapper.selectOne(wrapper);
            return CommonResult.success(annualFee.getAnnualFeeCode(), "查找成功");
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public CommonResult getAnnualFee(String patentName, Integer pageIndex, Integer pageSize) {
        try {
            Patent patent = this.findPatentByName(patentName);
            List<PatentAnnualFee> annualFeeList = annualFeeMapper.selectList(new LambdaQueryWrapper<PatentAnnualFee>().eq(PatentAnnualFee::getPatentId, patent.getId()));
            return CommonResult.success(PageInfoUtil.getPageInfo(
                    annualFeeList.stream().map(annualFee -> {
                        GetPatentAnnualFeeResponse response = new GetPatentAnnualFeeResponse();
                        response.setActualAmount(annualFee.getActualAmount());
                        response.setDueAmount(annualFee.getDueAmount());
                        response.setDueDate(CommonUtil.getYmdbyTimeStamp(annualFee.getDueDate()));
                        response.setYear(annualFee.getYear());
                        response.setPayStatus(annualFee.getPayStatus());
                        response.setActualPayDate(CommonUtil.getYmdbyTimeStamp(annualFee.getActualPayDate()));
                        return response;
                    }).collect(Collectors.toList()), pageIndex, pageSize), "查找成功");
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public CommonResult newAnnualFee(NewPatenAnnualFeeRequest request) {
        try {
            // 验重
            PatentAnnualFee annualFee = annualFeeMapper.selectOne(new LambdaQueryWrapper<PatentAnnualFee>().eq(PatentAnnualFee::getYear, request.getYear()));
            if (null != annualFee) {
                return CommonResult.failed(request.getPatentName() + "专利" + request.getYear() + "年度年费已存在");
            }
            annualFee = new PatentAnnualFee();
            annualFee.setAnnualFeeCode(CommonUtil.generateCode("PatentAnnualFee"));
            annualFee.setAnnualFeeName(request.getPatentName() + "" + request.getYear() + "年度年费");
            annualFee.setPayStatus(request.getPayStatus());
            annualFee.setYear(request.getYear());
            annualFee.setDueAmount(request.getDueAmount());
            annualFee.setActualAmount(request.getActualAmount());
            annualFee.setDueDate(CommonUtil.stringDateToTimeStamp(request.getDueDate()));
            annualFee.setActualPayDate(CommonUtil.stringDateToTimeStamp(request.getActualPayDate()));
            annualFee.setRemark(request.getRemark());
            annualFee.setPatentId(this.findPatentByName(request.getPatentName()).getId());
            return annualFeeMapper.insert(annualFee) == 0 ?
                    CommonResult.failed("添加" + request.getYear() + "年度年费失败") :
                    CommonResult.success(null, "添加" + request.getPatentName() + "专利" + request.getYear() + "年度年费成功");
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw e;
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public CommonResult deleteOfficialFee(String officialFeeName) throws Exception {
        try {
            return patentOfficialFeeMapper.delete(new LambdaQueryWrapper<PatentOfficialFee>().eq(PatentOfficialFee::getOfficialFeeName, officialFeeName)) == 0 ?
                    CommonResult.failed("删除失败") : CommonResult.success(null, "删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public CommonResult updateOfficialFee(UpdatePatentOfficialFeeRequest request) throws Exception {
        try {
            PatentOfficialFee officialFee = patentOfficialFeeMapper.selectOne(new LambdaQueryWrapper<PatentOfficialFee>().eq(PatentOfficialFee::getOfficialFeeCode, request.getOfficialFeeCode()));
            officialFee = patentManager.getUpdatedOfficialFee(request, officialFee);
            LambdaUpdateWrapper<PatentOfficialFee> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(PatentOfficialFee::getOfficialFeeCode, request.getOfficialFeeCode());
            return patentOfficialFeeMapper.update(officialFee, updateWrapper) == 0 ? CommonResult.failed("修改失败") : CommonResult.success(null, "修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public CommonResult getOfficialFee(GetPatentOfficialFeeRequest request) throws Exception {
        try {
            LambdaQueryWrapper<Patent> wrapper = patentManager.getWrapper(request);
            List<Patent> patentList = patentMapper.selectList(wrapper);
            List<Criteria.KV> items = request.getCriteria().getItems();
            for (Criteria.KV kv : items) {
                if (kv.getKey().equals("proposerName")) {
                    Proposal proposal = proposalService.findProposalByProposerName(request.getProposerName());
                    patentList.stream().filter(patent -> patent.getProposalId().equals(proposal.getId())).collect(Collectors.toList());
                    break;
                }
            }
//            if (StringUtils.isNotBlank(request.getProposerName())) {
//                Proposal proposal = proposalService.findProposalByProposerName(request.getProposerName());
//                patentList.stream().filter(patent -> patent.getProposalId().equals(proposal.getId())).collect(Collectors.toList());
//            }
            return CommonResult.success(PageInfoUtil.getPageInfo(
                    patentList.stream().map(patent -> {
                        GetPatentOfficialFeeResponse response = new GetPatentOfficialFeeResponse();
                        response.setPatentCode(patent.getPatentCode());
                        response.setPatentName(patent.getPatentName());
                        response.setTotalFee(patent.getTotalFee());
                        Proposal proposal = proposalService.findProposalByProposalId(patent.getProposalId());
                        response.setProposerName(proposal == null ? null : proposal.getProposerName());
                        return response;
                    }).collect(Collectors.toList()), request.getPageIndex(), request.getPageSize()), "查找成功");
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public CommonResult newOfficialFee(NewPatentOfficialFeeRequest request) throws Exception {
        try {
            // 验重
            PatentOfficialFee officialFee = this.findPatentOfficialFeeByName(request.getOfficialFeeName());
            if (null != officialFee) {
                return CommonResult.failed("该官费已存在");
            }
            officialFee = new PatentOfficialFee();
            officialFee.setOfficialFeeName(request.getOfficialFeeName());
            officialFee.setOfficialFeeCode(CommonUtil.generateCode("POfficialFee"));
            officialFee.setPatentId(this.findPatentByName(request.getPatentName()).getId());
            officialFee.setDueAmount(request.getDueAmount());
            officialFee.setActualAmount(request.getActualAmount());
            officialFee.setDueDate(CommonUtil.stringDateToTimeStamp(request.getDueDate()));
            officialFee.setActualPayDate(CommonUtil.stringDateToTimeStamp(request.getActualPayDate()));
            officialFee.setRemark(request.getRemark());
            officialFee.setOfficialFeeStatus(request.getOfficialFeeStatus());
            patentOfficialFeeMapper.insert(officialFee);
            return CommonResult.success(null, "添加专利官费成功");
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public CommonResult myPatent(Integer pageIndex, Integer pageSize) throws Exception {
        try {
            User user = hostHolder.getUser();
            List<PatentInventor> patentInventorList = patentInventorMapper.selectList(new LambdaQueryWrapper<PatentInventor>().eq(PatentInventor::getInventorId, user.getId()));
            List<Long> patentIds = patentInventorList.stream().map(PatentInventor::getPatentId).collect(Collectors.toList());
            List<Patent> patentList = patentMapper.selectBatchIds(patentIds);
            List<GetPatentResponse> responseList = new ArrayList<>();
            for (Patent patent : patentList) {
                GetPatentResponse response = new GetPatentResponse();
                List<PatentInventor> inventorList = patentInventorMapper.selectList(new LambdaQueryWrapper<PatentInventor>().eq(PatentInventor::getPatentId, patent.getId()));
                response.setPatentCode(patent.getPatentCode());
                response.setPatentType(patent.getPatentType());
                response.setPatentName(patent.getPatentName());
                response.setApplicationCode(patent.getApplicationCode());
                response.setApplicationDate(patent.getApplicationDate().toString());
                response.setGrantCode(patent.getGrantCode());
                response.setGrantDate(patent.getGrantDate().toString());
                response.setRightStatus(patent.getRightStatus());
                response.setCurrentProgram(patent.getCurrentProgram());
                response.setInventorNameList(inventorList.stream().map(PatentInventor::getInventorName).collect(Collectors.toList()));
                responseList.add(response);
            }
            return CommonResult.success(PageInfoUtil.getPageInfo(responseList, pageIndex, pageSize), "查找成功!");
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public CommonResult departmentPatent(Integer pageIndex, Integer pageSize) throws Exception {
        try {
            User user = hostHolder.getUser();
            Department department = departmentService.findDepartmentById(user.getDepartmentId());
            List<Patent> patentList = patentMapper.selectList(new LambdaQueryWrapper<Patent>().eq(Patent::getDepartmentId, department.getId()));
            List<GetPatentResponse> responseList = new ArrayList<>();
            for (Patent patent : patentList) {
                GetPatentResponse response = new GetPatentResponse();
                List<PatentInventor> inventorList = patentInventorMapper.selectList(new LambdaQueryWrapper<PatentInventor>().eq(PatentInventor::getPatentId, patent.getId()));
                response.setPatentCode(patent.getPatentCode());
                response.setPatentType(patent.getPatentType());
                response.setPatentName(patent.getPatentName());
                response.setApplicationCode(patent.getApplicationCode());
                response.setApplicationDate(patent.getApplicationDate().toString());
                response.setGrantCode(patent.getGrantCode());
                response.setGrantDate(patent.getGrantDate().toString());
                response.setRightStatus(patent.getRightStatus());
                response.setCurrentProgram(patent.getCurrentProgram());
                response.setInventorNameList(inventorList.stream().map(PatentInventor::getInventorName).collect(Collectors.toList()));
                responseList.add(response);
            }
            return CommonResult.success(PageInfoUtil.getPageInfo(responseList, pageIndex, pageSize), "查找成功!");
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public CommonResult getPatent(GetPatentRequest request) throws Exception {
        try {
            Map<String, Object> map = patentManager.getWrapper(request);
            LambdaQueryWrapper<Patent> wrapper = (LambdaQueryWrapper<Patent>) map.get("patentWrapper");
            LambdaQueryWrapper<PatentInventor> patentInventorWrapper = (LambdaQueryWrapper<PatentInventor>) map.get("patentInventorWrapper");
            List<Patent> patentList = patentMapper.selectList(wrapper);
            if (patentList.size() == 0) {
                return CommonResult.failed("查找失败，没有查询到相关专利");
            }
            Set<Long> patentIds = new HashSet<>();
            for (Patent patent : patentList) {
                patentIds.add(patent.getId());
            }
            List<PatentInventor> patentInventors = patentInventorMapper.selectList(patentInventorWrapper);
            if (patentInventors.size() == 0) {
                return CommonResult.failed("查找失败，没有相关发明人");
            }
//            List<Long> ids = patentInventors.stream().map(PatentInventor::getPatentId).collect(Collectors.toList());
            for (PatentInventor patentInventor : patentInventors) {
                if (!patentIds.contains(patentInventor.getPatentId())) {
                    patentList.remove(patentInventor);
                }
            }
            List<GetPatentResponse> responseList = new ArrayList<>();
            for (Patent patent : patentList) {
                GetPatentResponse response = new GetPatentResponse();
                List<PatentInventor> inventorList = patentInventorMapper.selectList(new LambdaQueryWrapper<PatentInventor>().eq(PatentInventor::getPatentId, patent.getId()));
                response.setPatentCode(patent.getPatentCode());
                response.setPatentType(patent.getPatentType());
                response.setPatentName(patent.getPatentName());
                response.setApplicationCode(patent.getApplicationCode());
                response.setApplicationDate(patent.getApplicationDate().toString());
                response.setGrantCode(patent.getGrantCode());
                response.setGrantDate(patent.getGrantDate().toString());
                response.setRightStatus(patent.getRightStatus());
                response.setCurrentProgram(patent.getCurrentProgram());
                response.setInventorNameList(inventorList.stream().map(PatentInventor::getInventorName).collect(Collectors.toList()));
                responseList.add(response);
            }
            return CommonResult.success(PageInfoUtil.getPageInfo(responseList, request.getPageIndex(), request.getPageSize()), "查找成功!");
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public CommonResult newPatent(NewPatentRequest request) throws Exception {
        try {
            Patent patent = patentMapper.selectOne(new LambdaQueryWrapper<Patent>().eq(Patent::getPatentName, request.getPatentName()));
            if (null != patent) {
                return CommonResult.failed("该专利已存在");
            }
            Department department = departmentService.findDepartmentByDepartmentName(request.getDepartmentName());
            patent = new Patent();
            patent.setPatentCode(request.getPatentCode());
            patent.setPatentName(request.getPatentName());
            patent.setPatentType(request.getPatentType());
            patent.setAgency(request.getAgency());
            patent.setApplicationCode(request.getApplicationCode());
            patent.setApplicationDate(CommonUtil.stringDateToTimeStamp(request.getApplicationDate()));
//            patent.setApplicationDate(new Timestamp(System.currentTimeMillis()));
            patent.setGrantCode(request.getGrantCode() == null ? null : request.getGrantCode());
            patent.setGrantDate(request.getGrantDate() == null ? null : CommonUtil.stringDateToTimeStamp(request.getGrantDate()));
//            patent.setGrantDate(new Timestamp(System.currentTimeMillis()));
            patent.setCurrentProgram(request.getCurrentProgram());
            patent.setDepartmentId(department.getId());
            patentMapper.insert(patent);
            List<NewPatentRequest.Inventor> inventorList = request.getListOfInventor();
//            Collections.sort(inventorList, new Comparator<NewPatentRequest.Inventor>() {
//                @Override
//                public int compare(NewPatentRequest.Inventor o1, NewPatentRequest.Inventor o2) {
//                    return Integer.valueOf(o1.getRate()) - Integer.valueOf(o2.getRate());
//                }
//            });
            int size = inventorList.size();
            for (int i = 0; i < size; i++) {
                PatentInventor patentInventor = new PatentInventor();
                NewPatentRequest.Inventor inventor = inventorList.get(i);
                User user = userService.findUserByUserName(inventor.getInventorName());
                patentInventor.setPatentId(patent.getId());
                patentInventor.setInventorName(inventor.getInventorName());
                patentInventor.setInventorCode(user.getUserCode());
                patentInventor.setContribute(inventor.getRate() == 100 ? new BigDecimal("1.00") : new BigDecimal("0." + inventor.getRate()));
                patentInventor.setRate(i + 1);
                patentInventor.setCreateTime(new Timestamp(System.currentTimeMillis()));
                patentInventor.setCreateUser(hostHolder.getUser().getId());
                patentInventor.setInventorId(user.getId());
                patentInventorMapper.insert(patentInventor);
            }
            return CommonResult.success(null, "新增专利成功");
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public PatentOfficialFee findPatentOfficialFeeByName(String officialFeeName) {
        return patentOfficialFeeMapper.selectOne(new LambdaQueryWrapper<PatentOfficialFee>().eq(PatentOfficialFee::getOfficialFeeName, officialFeeName));
    }

    @Override
    public Patent findPatentByName(String patentName) {
        return patentMapper.selectOne(new LambdaQueryWrapper<Patent>().eq(Patent::getPatentName, patentName));
    }

    @Override
    public Patent findPatentByCode(String patentCode) {
        return patentMapper.selectOne(new LambdaQueryWrapper<Patent>().eq(Patent::getPatentCode, patentCode));
    }

    @Override
    public PatentAnnualFee findPatentAnnualFeeByCode(String annualFeeCode) {
        return annualFeeMapper.selectOne(new LambdaQueryWrapper<PatentAnnualFee>().eq(PatentAnnualFee::getAnnualFeeCode, annualFeeCode));
    }

    @Override
    public List<PatentBonus> findBonusByPatentId(Long patentId) {
        return bonusMapper.selectList(new LambdaQueryWrapper<PatentBonus>().eq(PatentBonus::getPatentId, patentId));
    }

    @Override
    public Patent findPatentById(Long patentId) {
        return patentMapper.selectById(patentId);
    }

    @Override
    public PatentFile findFileByName(String fileName) {
        return fileMapper.selectOne(new LambdaQueryWrapper<PatentFile>().eq(PatentFile::getFileName, fileName));
    }

    @Override
    public PatentFile findFileByPatentId(Long patentId) {
        return fileMapper.selectOne(new LambdaQueryWrapper<PatentFile>().eq(PatentFile::getPatentId, patentId));
    }

    @Override
    public List<Patent> findPatentListByIds(List<Long> ids) {
        return patentMapper.selectBatchIds(ids);
    }
}
