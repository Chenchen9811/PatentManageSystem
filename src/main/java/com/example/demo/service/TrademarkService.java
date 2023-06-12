package com.example.demo.service;

import com.example.demo.common.CommonResult;
import com.example.demo.entity.Trademark;
import com.example.demo.entity.TrademarkBonus;
import com.example.demo.entity.TrademarkFile;
import com.example.demo.request.*;
import com.example.demo.response.GetTrademarkResponse;

import java.util.List;

public interface TrademarkService {
    CommonResult newTrademark(NewTrademarkRequest request) throws Exception;

    Trademark findTrademarkByName(String trademarkName);

    Trademark findTrademarkByCode(String trademarkCode);

    Trademark findTrademarkByInventorName(String inventorName);

    List<Trademark> findTrademarkListByIds(List<Long> ids);

    Trademark findTrademarkById(Long trademarkId);

    List<TrademarkBonus> findBonusListByTrademarkCode(String trademarkCode);

    List<TrademarkBonus> findBonusListByTrademarkId(Long trademarkId);

    TrademarkFile findFileByName(String fileName);

    List<GetTrademarkResponse> getTrademark(GetTrademarkRequest request) throws Exception;

    CommonResult getDepartmentTrademark(Integer pageIndex, Integer pageSize, Integer isDepartment) throws Exception;

    CommonResult newBonus(NewTrademarkBonusRequest request);

    CommonResult getBonus(GetTrademarkBonusRequest request);

    CommonResult deleteBonus(String bonusId);

    CommonResult newOfficialFee(NewTrademarkOfficialFeeRequest request);

    CommonResult getOfficialFee(GetTrademarkOfficialFeeRequest request);

    CommonResult deleteOfficialFee(String id);

    CommonResult newFileInfo(NewTrademarkFileInfoRequest request);

    CommonResult getFileInfo(GetTrademarkFileInfoRequest request);

    CommonResult updateBonus(UpdateTrademarkBonusRequest request);

    CommonResult deleteFile(String fileId);

    CommonResult updateOfficialFee(UpdateTrademarkOfficialFeeRequest request);
}
