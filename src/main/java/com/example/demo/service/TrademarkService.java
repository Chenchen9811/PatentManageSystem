package com.example.demo.service;

import com.example.demo.common.CommonResult;
import com.example.demo.entity.Trademark;
import com.example.demo.request.GetTrademarkRequest;
import com.example.demo.request.NewTrademarkRequest;

public interface TrademarkService {
    CommonResult newTrademark(NewTrademarkRequest request) throws Exception;

    Trademark findTrademarkByName(String trademarkName);

    CommonResult getTrademark(GetTrademarkRequest request) throws Exception;

    CommonResult getDepartmentTrademark(Integer pageIndex, Integer pageSize, Integer isDepartment) throws Exception;
}
