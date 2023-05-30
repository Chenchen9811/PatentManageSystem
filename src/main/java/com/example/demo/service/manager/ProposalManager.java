package com.example.demo.service.manager;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.demo.entity.Department;
import com.example.demo.entity.Proposal;
import com.example.demo.mapper.DepartmentMapper;
import com.example.demo.request.GetProposalRequest1;
import com.example.demo.service.DepartmentService;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProposalManager {

    @Resource
    private DepartmentService departmentService;

    public LambdaQueryWrapper<Proposal> buildWrapperByRequest1(GetProposalRequest1 request) {
        LambdaQueryWrapper<Proposal> wrapper = new LambdaQueryWrapper<>();
        List<GetProposalRequest1.Criteria.KV> items = request.getCriteria().getItems();
        for (GetProposalRequest1.Criteria.KV kv : items) {
            String key = kv.getKey();
            switch (key) {
                case "proposalCode" : {
                    wrapper.eq(Proposal::getProposalCode, kv.getValue());
                    break;
                }
                case "proposalName" : {
                    wrapper.eq(Proposal::getProposalName, kv.getValue());
                    break;
                }
                case "proposalType": {
                    if (Integer.valueOf(kv.getValue()).equals(0)) break;
                    wrapper.eq(Proposal::getProposalType, kv.getValue());
                    break;
                }
                case "proposalState": {
                    if (Integer.valueOf(kv.getValue()).equals(0)) break;
                    wrapper.eq(Proposal::getProposalState, kv.getValue());
                    break;
                }
                case "proposerCode": {
                    wrapper.eq(Proposal::getProposerCode, kv.getValue());
                    break;
                }
                case "proposerName": {
                    wrapper.eq(Proposal::getProposerName, kv.getValue());
                    break;
                }
                case "departmentName": {
                    if (Integer.valueOf(kv.getValue()).equals(0)) break;
                    Department department = departmentService.findDepartmentByDepartmentName(kv.getValue());
                    wrapper.eq(Proposal::getDepartmentId, department.getId());
                    break;
                }
                case "startDate": {
                    String endDate = null;
                    for (GetProposalRequest1.Criteria.KV kV : items) {
                        if (kV.getKey().equals("endDate")) {
                            endDate = kV.getValue();
                            break;
                        }
                    }
                    wrapper.between(Proposal::getProposalDate, kv.getValue(), endDate);
                    break;
                }
            }
        }
        
//        if (StringUtils.isNotBlank(request.getCriteria().getItems().getProposalCode()) {
//            wrapper.eq(Proposal::getProposalCode, request.getCriteria().getItems().getProposalCode());
//        }
//        if (StringUtils.isNotBlank(request.getCriteria().getItems().getProposalName())) {
//            wrapper.eq(Proposal::getProposalName, request.getCriteria().getItems().getProposerName());
//        }
//        if (ObjectUtils.isNotEmpty(request.getCriteria().getItems().getProposalType())) {
//            wrapper.eq(Proposal::getProposalType, request.getCriteria().getItems().getProposalType());
//        }
//        if (null != (request.getCriteria().getItems().getProposalState())) {
//            wrapper.eq(Proposal::getProposalState, request.getCriteria().getItems().getProposalState());
//        }
//        if (StringUtils.isNotBlank(request.getCriteria().getItems().getProposerCode())) {
//            wrapper.eq(Proposal::getProposerCode, request.getCriteria().getItems().getProposerCode());
//        }
//        if (StringUtils.isNotBlank(request.getCriteria().getItems().getProposerName())) {
//            wrapper.eq(Proposal::getProposerName, request.getCriteria().getItems().getProposerName());
//        }
//        if (StringUtils.isNotBlank(request.getCriteria().getItems().getDepartmentName())) {
//            Department department = departmentService.findDepartmentByDepartmentName(request.getCriteria().getItems().getDepartmentName());
//            wrapper.eq(Proposal::getDepartmentId, department.getId());
//        }
//        if (StringUtils.isNotBlank(request.getCriteria().getItems().getStartDate()) && StringUtils.isNotBlank(request.getCriteria().getItems().getEndDate())) {
//            wrapper.between(Proposal::getProposalDate, request.getCriteria().getItems().getStartDate(), request.getCriteria().getItems().getEndDate());
//        }
        return wrapper;
    }


}
