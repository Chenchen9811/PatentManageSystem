package com.example.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.demo.Utils.CommonUtil;
import com.example.demo.Utils.HostHolder;
import com.example.demo.Utils.PageInfoUtil;
import com.example.demo.common.CommonResult;
import com.example.demo.entity.*;
import com.example.demo.mapper.TrademarkBonusMapper;
import com.example.demo.mapper.TrademarkFileMapper;
import com.example.demo.mapper.TrademarkMapper;
import com.example.demo.mapper.TrademarkOfficialFeeMapper;
import com.example.demo.request.*;
import com.example.demo.response.GetPatentOfficialFeeResponse;
import com.example.demo.response.GetTrademarkBonusResponse;
import com.example.demo.response.GetTrademarkOfficialFeeResponse;
import com.example.demo.response.GetTrademarkResponse;
import com.example.demo.service.DepartmentService;
import com.example.demo.service.ProposalService;
import com.example.demo.service.TrademarkService;
import com.example.demo.service.UserService;
import com.example.demo.service.manager.TrademarkManager;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TrademarkServiceImpl implements TrademarkService {
    @Resource
    private TrademarkMapper trademarkMapper;

    @Resource
    private UserService userService;

    @Resource
    private DepartmentService departmentService;

    @Resource
    private TrademarkManager trademarkManager;

    @Resource
    private HostHolder hostHolder;

    @Resource
    private TrademarkBonusMapper bonusMapper;

    @Resource
    private TrademarkOfficialFeeMapper officialFeeMapper;

    @Resource
    private TrademarkFileMapper fileMapper;

    @Resource
    private ProposalService proposalService;


    @Transactional(rollbackFor = Exception.class)
    @Override
    public CommonResult updateBonus(UpdateTrademarkBonusRequest request) {
        try {
            TrademarkBonus bonus = bonusMapper.selectOne(new LambdaQueryWrapper<TrademarkBonus>().eq(TrademarkBonus::getId, Long.parseLong(request.getBonusId())));
            bonus.setBonusType(request.getBonusType());
            bonus.setActualRelease(request.getActualRelease());
            bonus.setInventorName(request.getInventorName());
            bonus.setActualRelease(request.getActualRelease());
            return bonusMapper.updateById(bonus) == 0 ? CommonResult.failed("编辑失败") : CommonResult.success(null, "编辑成功");
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public CommonResult getFileInfo(GetTrademarkFileInfoRequest request) {
        try {
            List<Trademark> trademarkList = null;
            List<TrademarkFile> fileList = null;
            List<Proposal> proposalList = null;
            List<User> uploaderList = null;
            // 查找符合条件的商标文件
            LambdaQueryWrapper<TrademarkFile> fileWrapper = trademarkManager.getFileWrapper(request);
            fileList = fileMapper.selectList(fileWrapper);
            if (fileList.size() == 0) {
                return CommonResult.failed("没有找到符合条件的商标文件，查找失败");
            }
            // 获取商标文件对应的商标信息，用于构造返回值
            List<Long> fileTrademarkIds = fileList.stream().map(TrademarkFile::getTrademarkId).distinct().collect(Collectors.toList());
            trademarkList = this.findTrademarkListByIds(fileTrademarkIds);
            if (trademarkList.size() == 0) {
                return CommonResult.failed("查找的商标没有对应的商标文件，查找失败");
            }
            // 获取符合条件商标的提案
            proposalList = proposalService.findProposalListByIds(trademarkList.stream().map(Trademark::getProposalId).collect(Collectors.toList()));
            if (proposalList.size() == 0) {
                return CommonResult.failed("查找的商标没有对应的提案，查找失败");
            }
            uploaderList = userService.findUserListByIds(fileList.stream().map(TrademarkFile::getUploaderId).distinct().collect(Collectors.toList()));
            Map<Long, Proposal> proposalMap = new HashMap<>();
            Map<Long, Trademark> trademarkMap = new HashMap<>();
            Map<Long, User> uploaderMap = new HashMap<>();
            for (Proposal proposal : proposalList) {
                proposalMap.put(proposal.getId(), proposal);
            }
            for (Trademark trademark : trademarkList) {
                trademarkMap.put(trademark.getId(), trademark);
            }
            for (User uploader : uploaderList) {
                uploaderMap.put(uploader.getId(), uploader);
            }
            List<GetTrademarkFileInfoResponse> responseList = fileList.stream().map(file -> {
                GetTrademarkFileInfoResponse response = new GetTrademarkFileInfoResponse();
                Trademark trademark = trademarkMap.get(file.getTrademarkId());
                Proposal proposal = proposalMap.get(trademark.getProposalId());
                User uploader = uploaderMap.get(file.getUploaderId());
                response.setFileName(file.getFileName());
                response.setTrademarkCode(trademark.getTrademarkCode());
                response.setTrademarkOwnerName(trademark.getTrademarkOwner());
                response.setTrademarkType(trademark.getTrademarkType());
                response.setUploadDate(file.getUploadDate().toString());
                response.setCopyRightCode(trademark.getCopyRightCode());
                response.setProposalDate(CommonUtil.getYmdbyTimeStamp(proposal.getProposalDate()));
                response.setProposerName(proposal.getProposerName());
                response.setUploadDate(file.getUploadDate().toString());
                response.setUploaderName(uploader.getUserName());
                response.setFileType(file.getFileType());
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
    public CommonResult newFileInfo(NewTrademarkFileInfoRequest request) {
        try {
            // 验重
            TrademarkFile file = this.findFileByName(request.getFileName());
            if (null != file) {
                return CommonResult.failed("该文件已存在");
            }
            Trademark trademark = this.findTrademarkByCode(request.getTrademarkCode());
            file = new TrademarkFile();
            file.setFileName(request.getFileName());
            file.setTrademarkId(trademark.getId());
            file.setFileUrl(CommonUtil.getFileUrl(request.getFileName()));
            file.setFileType(request.getFileType());
            file.setFileStatus(request.getFileStatus() == null ? null : request.getFileStatus());
            file.setUploaderId(hostHolder.getUser().getId());
            file.setUploadDate(new Date(System.currentTimeMillis()));
            return fileMapper.insert(file) != 0 ? CommonResult.success(null, "添加商标文件成功") : CommonResult.failed("添加商标文件失败");
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw e;
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public CommonResult deleteOfficialFee(Long officialFeeId) {
        try {
            return officialFeeMapper.deleteById(officialFeeId) == 0 ?
                    CommonResult.failed("删除失败") : CommonResult.success(null, "删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public CommonResult getOfficialFee(GetTrademarkOfficialFeeRequest request) {
        try {
            LambdaQueryWrapper<TrademarkOfficialFee> wrapper = trademarkManager.getCriteriaWrapper(request);
            List<TrademarkOfficialFee> officialFeeList = officialFeeMapper.selectList(wrapper);
            List<Trademark> trademarkList = trademarkMapper.selectBatchIds(officialFeeList.stream().map(TrademarkOfficialFee::getTrademarkId).distinct().collect(Collectors.toList()));
            Map<Long, Trademark> map = new HashMap<>();
            for (Trademark trademark : trademarkList) {
                map.put(trademark.getId(), trademark);
            }
            Map<Long, Long> totalFeeMap = new HashMap<>();
            for (TrademarkOfficialFee fee : officialFeeList) {
                totalFeeMap.merge(fee.getTrademarkId(), Long.parseLong(fee.getDueAmount()), Long::sum);
            }
            PageInfo<GetTrademarkOfficialFeeResponse> pageInfo = PageInfoUtil.getPageInfo(officialFeeList.stream().map(fee -> {
                GetTrademarkOfficialFeeResponse response = new GetTrademarkOfficialFeeResponse();
                Trademark trademark = map.get(fee.getTrademarkId());
                response.setOfficialFeeStatus(fee.getOfficialFeeStatus());
                response.setDueAmount(fee.getDueAmount());
                response.setDueDate(fee.getDueDate().toString());
                response.setActualPayDate(fee.getActualPayDate().toString());
                response.setTrademarkCode(trademark.getTrademarkCode());
                response.setTrademarkName(trademark.getTrademarkName());
                response.setActualPay(fee.getActualPay());
                response.setId(fee.getId());
                response.setTotalAmount(String.valueOf(totalFeeMap.get(fee.getTrademarkId())));
                response.setFeeName(fee.getOfficialFeeName());
                return response;
            }).collect(Collectors.toList()), request.getPageIndex(), request.getPageSize());
            return CommonResult.success(pageInfo, "查找成功");
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw e;
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public CommonResult newOfficialFee(NewTrademarkOfficialFeeRequest request) {
        try {
            Trademark trademark = this.findTrademarkByCode(request.getTrademarkCode());
            TrademarkOfficialFee officialFee = new TrademarkOfficialFee();
            officialFee.setOfficialFeeCode(request.getOfficialFeeCode());
            officialFee.setTrademarkId(trademark.getId());
            officialFee.setActualPay(request.getActualPay());
            officialFee.setDueAmount(request.getDueAmount());
            officialFee.setDueDate(CommonUtil.stringToDate(request.getDueDate()));
            officialFee.setActualPay(request.getActualPay());
            officialFee.setActualPayDate(CommonUtil.stringToDate(request.getActualPayDate()));
            officialFee.setOfficialFeeStatus(request.getOfficialFeeStatus());
            officialFee.setOfficialFeeName(request.getOfficialFeeName());
            if (StringUtils.isNotBlank(request.getRemark())) {
                officialFee.setRemark(request.getRemark());
            }
            return officialFeeMapper.insert(officialFee) != 0 ?
                    CommonResult.success(null, "添加成功") : CommonResult.failed("添加失败");
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
            return bonusMapper.deleteById(Long.parseLong(bonusId)) == 0 ?
                    CommonResult.failed("删除失败") : CommonResult.success(null, "删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public CommonResult getBonus(GetTrademarkBonusRequest request) {
        try {
            LambdaQueryWrapper<TrademarkBonus> wrapper = new LambdaQueryWrapper<>();
            List<Criteria.KV> items = request.getCriteria().getItems();
            for (Criteria.KV kv : items) {
                switch (kv.getKey()) {
                    case "trademarkCode": {
                        Trademark trademark = this.findTrademarkByCode(request.getTrademarkCode());
                        wrapper.eq(TrademarkBonus::getTrademarkId, trademark.getId());
                        break;
                    }
                    case "inventorName": {
                        wrapper.eq(TrademarkBonus::getInventorName, kv.getValue());
                        break;
                    }
                }
            }
//            if (StringUtils.isNotBlank(request.getTrademarkCode())) {
//                Trademark trademark = this.findTrademarkByCode(request.getTrademarkCode());
//                wrapper.eq(TrademarkBonus::getTrademarkId, trademark.getId());
//            }
//            if (StringUtils.isNotBlank(request.getInventorName())) {
//                wrapper.eq(TrademarkBonus::getInventorName, request.getInventorName());
//            }
            List<TrademarkBonus> bonusList = bonusMapper.selectList(wrapper);
            Map<Long, Trademark> map = new HashMap<>();
            List<Trademark> trademarks = trademarkMapper.selectBatchIds(bonusList.stream().map(TrademarkBonus::getTrademarkId).distinct().collect(Collectors.toList()));
            for (Trademark trademark : trademarks) {
                map.put(trademark.getId(), trademark);
            }
            return CommonResult.success(PageInfoUtil.getPageInfo(
                    bonusList.stream().map(bonus -> {
                        GetTrademarkBonusResponse response = new GetTrademarkBonusResponse();
                        Trademark trademark = map.get(bonus.getTrademarkId());
                        response.setBonusType(bonus.getBonusType());
                        response.setTrademarkCode(trademark.getTrademarkCode());
                        response.setRanking(bonus.getRanking());
                        response.setTrademarkType(trademark.getTrademarkType());
                        response.setInventorName(bonus.getInventorName());
                        response.setBonusAmount(bonus.getBonusAmount());
                        response.setActualRelease(bonus.getActualRelease());
                        response.setReleaseStatus(bonus.getReleaseStatus());
                        response.setTrademarkName(trademark.getTrademarkName());
                        response.setTrademarkId(String.valueOf(trademark.getId()));
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
    public CommonResult newBonus(NewTrademarkBonusRequest request) {
        try {
            Trademark trademark = this.findTrademarkByCode(request.getTrademarkCode());
            List<TrademarkBonus> bonusList = this.findBonusListByTrademarkId(trademark.getId());
            if (0 != bonusList.size()) {
                return CommonResult.failed("该商标奖金已存在");
            }
            bonusList = new ArrayList<>();
            List<NewTrademarkBonusRequest.inventor> inventorList = request.getInventorList();
            inventorList.sort((Comparator.comparingInt(o -> Integer.parseInt(o.getActualRelease()))));
            int size = inventorList.size();
            for (int i = 0; i < size; i++) {
                TrademarkBonus bonus = new TrademarkBonus();
                NewTrademarkBonusRequest.inventor inventor = inventorList.get(i);
                bonus.setBonusType(request.getBonusType());
                bonus.setBonusAmount(request.getBonusAmount());
                bonus.setInventorName(inventor.getInventorName());
                bonus.setActualRelease(inventor.getActualRelease());
                bonus.setReleaseStatus(request.getReleaseStatus());
                bonus.setRanking(i + 1);
                bonus.setTrademarkId(trademark.getId());
                bonusList.add(bonus);
            }

            return bonusMapper.insertBatchSomeColumn(bonusList) == 0 ?
                    CommonResult.failed("添加奖金失败") : CommonResult.success(null, "添加奖金成功");
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public CommonResult getDepartmentTrademark(Integer pageIndex, Integer pageSize, Integer isDepartment) throws Exception {
        try {
            User user = hostHolder.getUser();
            List<Trademark> trademarkList = null;
            if (isDepartment == 0) {
                Department department = departmentService.findDepartmentById(user.getDepartmentId());
                trademarkList = trademarkMapper.selectList(new LambdaQueryWrapper<Trademark>().eq(Trademark::getDepartmentId, department.getId()));
            } else {
                trademarkList = trademarkMapper.selectList(new LambdaQueryWrapper<Trademark>().eq(Trademark::getInventorId, user.getId()));
            }
            return CommonResult.success(PageInfoUtil.getPageInfo(trademarkList.stream().map(trademark -> {
                GetTrademarkResponse response = new GetTrademarkResponse();
                response.setTrademarkCode(trademark.getTrademarkCode());
                response.setTrademarkType(trademark.getTrademarkType());
                response.setTrademarkName(trademark.getTrademarkName());
                response.setCurrentStatus(trademark.getCurrentStatus());
                response.setCopyRightCode(trademark.getCopyRightCode());
                response.setRightStatus(trademark.getRightStatus());
                response.setInventorName(userService.findUserByUserId(trademark.getInventorId()).getUserName());
                return response;
            }).collect(Collectors.toList()), pageIndex, pageSize), "查找成功");
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public CommonResult getTrademark(GetTrademarkRequest request) throws Exception {
        try {
            LambdaQueryWrapper<Trademark> wrapper = trademarkManager.getWrapper(request);
            List<Trademark> trademarkList = trademarkMapper.selectList(wrapper);
            return CommonResult.success(PageInfoUtil.getPageInfo(trademarkList.stream().map(trademark -> {
                GetTrademarkResponse response = new GetTrademarkResponse();
                response.setTrademarkCode(trademark.getTrademarkCode());
                response.setTrademarkType(trademark.getTrademarkType());
                response.setTrademarkName(trademark.getTrademarkName());
                response.setCurrentStatus(trademark.getCurrentStatus());
                response.setCopyRightCode(trademark.getCopyRightCode());
                response.setRightStatus(trademark.getRightStatus());
                response.setInventorName(userService.findUserByUserId(trademark.getInventorId()).getUserName());
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
    public CommonResult newTrademark(NewTrademarkRequest request) throws Exception {
        try {
            Trademark trademark = this.findTrademarkByName(request.getTrademarkName());
            if (null != trademark) {
                return CommonResult.failed("该商标已存在");
            }
            User user = userService.findUserByUserName(request.getInventorName());
            Department department = departmentService.findDepartmentByDepartmentName(request.getDepartmentName());
            trademark = new Trademark();
            trademark.setTrademarkCode(request.getTrademarkCode());
            trademark.setTrademarkOwner(request.getTrademarkOwner());
            trademark.setTrademarkType(request.getTrademarkType());
            trademark.setTrademarkName(request.getTrademarkName());
            trademark.setInventorId(user.getId());
            trademark.setRightStatus(request.getRightStatus());
            trademark.setCurrentStatus(request.getCurrentStatus());
            trademark.setCopyRightCode(request.getCopyRightCode());
            trademark.setAgency(request.getAgency());
            trademark.setDepartmentId(department.getId());
            trademark.setTrademarkDesign("xxx");
            if (StringUtils.isNotBlank(request.getApplyDate())) {
                trademark.setApplyDate(CommonUtil.stringDateToTimeStamp(request.getApplyDate()));
            }
            if (StringUtils.isNotBlank(request.getGrantDate())) {
                trademark.setGrantDate(CommonUtil.stringDateToTimeStamp(request.getGrantDate()));
            }
            trademarkMapper.insert(trademark);
            return CommonResult.success(null, "添加商标成功");
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw new Exception(e.getMessage());
        }
    }


    @Override
    public List<TrademarkBonus> findBonusListByTrademarkId(Long trademarkId) {
        return bonusMapper.selectList(new LambdaQueryWrapper<TrademarkBonus>().eq(TrademarkBonus::getTrademarkId, trademarkId));
    }

    @Override
    public Trademark findTrademarkByName(String trademarkName) {
        return trademarkMapper.selectOne(new LambdaQueryWrapper<Trademark>().eq(Trademark::getTrademarkName, trademarkName));
    }

    @Override
    public Trademark findTrademarkByCode(String trademarkCode) {
        return trademarkMapper.selectOne(new LambdaQueryWrapper<Trademark>().eq(Trademark::getTrademarkCode, trademarkCode));
    }

    @Override
    public Trademark findTrademarkById(Long trademarkId) {
        return trademarkMapper.selectById(trademarkId);
    }

    @Override
    public List<TrademarkBonus> findBonusListByTrademarkCode(String trademarkCode) {
        return bonusMapper.findBonusListByTrademarkCode(trademarkCode);
    }

    @Override
    public Trademark findTrademarkByInventorName(String inventorName) {
        return trademarkMapper.findTrademarkByInventorName(inventorName);
    }

    @Override
    public TrademarkFile findFileByName(String fileName) {
        return fileMapper.selectOne(new LambdaQueryWrapper<TrademarkFile>().eq(TrademarkFile::getFileName, fileName));
    }

    @Override
    public List<Trademark> findTrademarkListByIds(List<Long> ids) {
        return trademarkMapper.selectBatchIds(ids);
    }
}
