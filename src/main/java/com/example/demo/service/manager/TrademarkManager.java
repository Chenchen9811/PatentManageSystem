package com.example.demo.service.manager;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.demo.entity.*;
import com.example.demo.request.Criteria;
import com.example.demo.request.GetTrademarkFileInfoRequest;
import com.example.demo.request.GetTrademarkOfficialFeeRequest;
import com.example.demo.request.GetTrademarkRequest;
import com.example.demo.service.DepartmentService;
import com.example.demo.service.TrademarkService;
import com.example.demo.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class TrademarkManager {

    @Resource
    private UserService userService;

    @Resource
    private TrademarkService trademarkService;

    @Resource
    private DepartmentService departmentService;

    public LambdaQueryWrapper<TrademarkFile> getFileWrapper(GetTrademarkFileInfoRequest request) {
        LambdaQueryWrapper<TrademarkFile> wrapper = new LambdaQueryWrapper<>();
        List<Criteria.KV> items = request.getCriteria().getItems();
        Set<Long> trademarkIds = new HashSet<>();
        for (Criteria.KV kv : items) {
            switch (kv.getKey()) {
                case "fileType": {
                    if (kv.getValue().equals("0")) break;
                    wrapper.eq(TrademarkFile::getFileType, kv.getValue());
                    break;
                }
                case "fileName": {
                    wrapper.eq(TrademarkFile::getFileName, kv.getValue());
                    break;
                }
                case "trademarkCode": {
                    Trademark trademark = trademarkService.findTrademarkByCode(kv.getValue());
                    trademarkIds.add(trademark.getId());
//                    wrapper.eq(TrademarkFile::getTrademarkId, trademark.getId());
                    break;
                }
                case "trademarkName" : {
                    Trademark trademark = trademarkService.findTrademarkByName(kv.getValue());
                    trademarkIds.add(trademark.getId());
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
                    wrapper.between(TrademarkFile::getUploadDate, kv.getValue(), endDate);
                    break;
                }
                case "uploaderName" : {
                    User user = userService.findUserByUserName(kv.getValue());
                    wrapper.eq(TrademarkFile::getUploaderId, user.getId());
                    break;
                }
            }
        }
        if (!trademarkIds.isEmpty()) {
            wrapper.in(TrademarkFile::getTrademarkId, trademarkIds);
        }
//        if (StringUtils.isNotBlank(request.getFileName())) {
//            wrapper.eq(TrademarkFile::getFileName, request.getFileName());
//        }
//        if (StringUtils.isNotBlank(request.getUploadDateBegin()) && StringUtils.isNotBlank(request.getUploadDateEnd())) {
//            wrapper.between(TrademarkFile::getUploadDate, request.getUploadDateBegin(), request.getUploadDateEnd());
//        }
//        wrapper.eq(TrademarkFile::getFileType, request.getFileType());
        return wrapper;
    }

    public LambdaQueryWrapper<Trademark> getWrapper(GetTrademarkFileInfoRequest request) {
        LambdaQueryWrapper<Trademark> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(request.getTrademarkCode()))
            wrapper.eq(Trademark::getTrademarkCode, request.getTrademarkCode());
        return wrapper;
    }

    public LambdaQueryWrapper<TrademarkOfficialFee> getCriteriaWrapper(GetTrademarkOfficialFeeRequest request) {
        LambdaQueryWrapper<TrademarkOfficialFee> wrapper = new LambdaQueryWrapper<>();
        boolean trademarkCode = false, trademarkName = false, inventorName = false;
        Trademark trademark = null;
        List<Long> trademarkIds = new ArrayList<>();
        List<Criteria.KV> items = request.getCriteria().getItems();
        for (Criteria.KV kv : items) {
            switch (kv.getKey()) {
                case "trademarkCode": {
                    trademark = trademarkService.findTrademarkByCode(request.getTrademarkCode());
                    trademarkIds.add(trademark.getId());
                    trademarkCode = true;
                    break;
                }
                case "trademarkName": {
                    trademark = trademarkService.findTrademarkByName(request.getTrademarkName());
                    trademarkIds.add(trademark.getId());
                    trademarkName = true;
                    break;
                }
                case "inventorName": {
                    trademark = trademarkService.findTrademarkByInventorName(request.getInventorName());
                    trademarkIds.add(trademark.getId());
                    inventorName = true;
                    break;
                }
                case "actualPayBeginDate": {
                    String endDate = null;
                    for (Criteria.KV kV : items) {
                        if (kV.getKey().equals("actualPayEndDate")) {
                            endDate = kV.getValue();
                        }
                        break;
                    }
                    wrapper.between(TrademarkOfficialFee::getActualPayDate, kv.getValue(), endDate);
                    break;
                }
                case "dueAmount": {
                    wrapper.eq(TrademarkOfficialFee::getDueAmount, kv.getValue());
                    break;
                }
            }
        }
        if (trademarkCode || trademarkName || inventorName) {
            wrapper.in(TrademarkOfficialFee::getTrademarkId, trademarkIds);
        }
        return wrapper;
    }

    public LambdaQueryWrapper<TrademarkOfficialFee> getWrapper(GetTrademarkOfficialFeeRequest request) {
        LambdaQueryWrapper<TrademarkOfficialFee> wrapper = new LambdaQueryWrapper<>();
        boolean trademarkCode = false, trademarkName = false, inventorName = false;
        Trademark trademark = null;
        List<Long> trademarkIds = new ArrayList<>();
        if (trademarkCode = StringUtils.isNotBlank(request.getTrademarkCode())) {
            trademark = trademarkService.findTrademarkByCode(request.getTrademarkCode());
            trademarkIds.add(trademark.getId());
        }
        if (trademarkName = StringUtils.isNotBlank(request.getTrademarkName())) {
            trademark = trademarkService.findTrademarkByName(request.getTrademarkName());
            trademarkIds.add(trademark.getId());
        }
        if (inventorName = StringUtils.isNotBlank(request.getInventorName())) {
            trademark = trademarkService.findTrademarkByInventorName(request.getInventorName());
            trademarkIds.add(trademark.getId());
        }
        if (trademarkCode || trademarkName || inventorName) {
            wrapper.in(TrademarkOfficialFee::getTrademarkId, trademarkIds);
        }
        if (StringUtils.isNotBlank(request.getActualPayBeginDate()) && StringUtils.isNotBlank(request.getActualPayEndDate())) {
            wrapper.between(TrademarkOfficialFee::getActualPayDate, request.getActualPayBeginDate(), request.getActualPayEndDate());
        }
        if (StringUtils.isNotBlank(request.getDueAmount())) {
            wrapper.eq(TrademarkOfficialFee::getDueAmount, request.getDueAmount());
        }
        return wrapper;
    }

    public LambdaQueryWrapper<Trademark> getWrapper(GetTrademarkRequest request) {
        LambdaQueryWrapper<Trademark> wrapper = new LambdaQueryWrapper<>();
        List<Criteria.KV> items = request.getCriteria().getItems();
        for (Criteria.KV kv : items) {
            switch (kv.getKey()) {
                case "trademarkCode": {
                    wrapper.eq(Trademark::getTrademarkCode, kv.getValue());
                    break;
                }
                case "trademarkName": {
                    wrapper.eq(Trademark::getTrademarkName, kv.getValue());
                    break;
                }
                case "inventorName": {
                    User user = userService.findUserByUserName(kv.getValue());
                    wrapper.eq(Trademark::getInventorId, user.getId());
                    break;
                }
                case "trademarkOwner": {
                    wrapper.eq(Trademark::getTrademarkOwner, kv.getValue());
                    break;
                }
                case "copyRightCode": {
                    wrapper.eq(Trademark::getCopyRightCode, kv.getValue());
                    break;
                }
                case "currentStatus": {
                    if (kv.getValue().equals("0")) break;
                    wrapper.eq(Trademark::getCurrentStatus, kv.getValue());
                    break;
                }
                case "rightStatus": {
                    wrapper.eq(Trademark::getRightStatus, kv.getValue());
                    break;
                }
                case "agency": {
                    if (kv.getValue().equals("0")) break;
                    wrapper.eq(Trademark::getAgency, kv.getValue());
                    break;
                }
                case "departmentName" : {
                    if (kv.getValue().equals("0")) break;
                    Department department = departmentService.findDepartmentByDepartmentName(kv.getValue());
                    wrapper.eq(Trademark::getDepartmentId, department.getId());
                    break;
                }
            }
        }
        return wrapper;
    }

    public LambdaQueryWrapper<Trademark> getWrapperByGetTrademarkRequest(GetTrademarkRequest request) {
        LambdaQueryWrapper<Trademark> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(request.getTrademarkCode())) {
            wrapper.eq(Trademark::getTrademarkCode, request.getTrademarkCode());
        }
        if (StringUtils.isNotBlank(request.getTrademarkName())) {
            wrapper.eq(Trademark::getTrademarkName, request.getTrademarkName());
        }
        if (StringUtils.isNotBlank(request.getInventorName())) {
            User user = userService.findUserByUserName(request.getInventorName());
            wrapper.eq(Trademark::getInventorId, user.getId());
        }
        if (StringUtils.isNotBlank(request.getTrademarkOwner())) {
            wrapper.eq(Trademark::getTrademarkOwner, request.getTrademarkOwner());
        }
        if (StringUtils.isNotBlank(request.getCopyRightCode())) {
            wrapper.eq(Trademark::getCopyRightCode, request.getCopyRightCode());
        }
        if (StringUtils.isNotBlank(request.getTrademarkType())) {
            wrapper.eq(Trademark::getTrademarkType, request.getTrademarkType());
        }
        if (StringUtils.isNotBlank(request.getCurrentStatus())) {
            wrapper.eq(Trademark::getCurrentStatus, request.getCurrentStatus());
        }
        if (StringUtils.isNotBlank(request.getRightStatus())) {
            wrapper.eq(Trademark::getRightStatus, request.getRightStatus());
        }
        if (StringUtils.isNotBlank(request.getAgency())) {
            wrapper.eq(Trademark::getAgency, request.getAgency());
        }
        return wrapper;
    }
}
