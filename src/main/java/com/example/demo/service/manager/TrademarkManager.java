package com.example.demo.service.manager;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.demo.entity.Trademark;
import com.example.demo.entity.TrademarkFile;
import com.example.demo.entity.TrademarkOfficialFee;
import com.example.demo.entity.User;
import com.example.demo.request.Criteria;
import com.example.demo.request.GetTrademarkFileInfoRequest;
import com.example.demo.request.GetTrademarkOfficialFeeRequest;
import com.example.demo.request.GetTrademarkRequest;
import com.example.demo.service.TrademarkService;
import com.example.demo.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class TrademarkManager {

    @Resource
    private UserService userService;

    @Resource
    private TrademarkService trademarkService;

    public LambdaQueryWrapper<TrademarkFile> getFileWrapper(GetTrademarkFileInfoRequest request) {
        LambdaQueryWrapper<TrademarkFile> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(request.getFileName())) {
            wrapper.eq(TrademarkFile::getFileName, request.getFileName());
        }
        if (StringUtils.isNotBlank(request.getUploadDateBegin()) && StringUtils.isNotBlank(request.getUploadDateEnd())) {
            wrapper.between(TrademarkFile::getUploadDate, request.getUploadDateBegin(), request.getUploadDateEnd());
        }
        wrapper.eq(TrademarkFile::getFileType, request.getFileType());
        return wrapper;
    }

    public LambdaQueryWrapper<Trademark> getWrapper(GetTrademarkFileInfoRequest request) {
        LambdaQueryWrapper<Trademark> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(request.getTrademarkCode()))
            wrapper.eq(Trademark::getTrademarkCode, request.getTrademarkCode());
        return wrapper;
    }

    public LambdaQueryWrapper<TrademarkOfficialFee> getWrapper(GetTrademarkOfficialFeeRequest request) {
        LambdaQueryWrapper<TrademarkOfficialFee> wrapper = new LambdaQueryWrapper<>();
        boolean trademarkCode, trademarkName, inventorName = false;
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
                case "trademarkCode" : {
                    wrapper.eq(Trademark::getTrademarkCode, kv.getValue());
                    break;
                }
                case "trademarkName" : {
                    wrapper.eq(Trademark::getTrademarkName, kv.getValue());
                    break;
                }
                case "inventorName" : {
                    User user = userService.findUserByUserName(kv.getValue());
                    wrapper.eq(Trademark::getInventorId, user.getId());
                    break;
                }
                case "trademarkOwner" : {
                    wrapper.eq(Trademark::getTrademarkOwner, kv.getValue());
                    break;
                }
                case "copyRightCode" : {
                    wrapper.eq(Trademark::getCopyRightCode, kv.getValue());
                    break;
                }
                case "currentStatus" : {
                    wrapper.eq(Trademark::getCurrentStatus, kv.getValue());
                    break;
                }
                case "rightStatus" : {
                    wrapper.eq(Trademark::getRightStatus, kv.getValue());
                    break;
                }
                case "agency" : {
                    wrapper.eq(Trademark::getAgency, kv.getValue());
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
