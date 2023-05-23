package com.example.demo.service;

import com.example.demo.common.CommonResult;
import com.example.demo.entity.Agency;
import com.example.demo.request.NewAgencyRequest;

public interface AgencyService {
    CommonResult newAgency(NewAgencyRequest request) throws Exception;

    Agency findAgencyByName(String agencyName);

    CommonResult getAgency(Integer pageIndex, Integer pageSize) throws Exception;
}