package com.example.demo.service;

import com.example.demo.common.CommonResult;
import com.example.demo.entity.Agency;
import com.example.demo.request.GetAgencyRequest;
import com.example.demo.request.NewAgencyRequest;
import com.example.demo.request.UpdateAgencyRequest;

public interface AgencyService {
    CommonResult newAgency(NewAgencyRequest request) throws Exception;

    Agency findAgencyByName(String agencyName);

    Agency findAgencyByCode(String agencyCode);

    CommonResult getAgency(GetAgencyRequest request) throws Exception;

    CommonResult getSingleAgency(String agencyCode);

    CommonResult updateAgency(UpdateAgencyRequest request);

    CommonResult deleteAgency(String agencyCode);

    CommonResult getAgencyList();

}
