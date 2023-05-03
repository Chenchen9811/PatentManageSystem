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

@Service
public class ProposalManager {

    @Resource
    private DepartmentService departmentService;

    public LambdaQueryWrapper<Proposal> buildWrapperByRequest1(GetProposalRequest1 request) {
        LambdaQueryWrapper<Proposal> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(request.getProposalCode())) {
            wrapper.eq(Proposal::getProposalCode, request.getProposalCode());
        }
        if (StringUtils.isNotBlank(request.getProposalName())) {
            wrapper.eq(Proposal::getProposalName, request.getProposerName());
        }
        if (ObjectUtils.isNotEmpty(request.getProposalType())) {
            wrapper.eq(Proposal::getProposalType, request.getProposalType());
        }
        if (null != (request.getProposalState())) {
            wrapper.eq(Proposal::getProposalState, request.getProposalState());
        }
        if (StringUtils.isNotBlank(request.getProposerCode())) {
            wrapper.eq(Proposal::getProposerCode, request.getProposerCode());
        }
        if (StringUtils.isNotBlank(request.getProposerName())) {
            wrapper.eq(Proposal::getProposerName, request.getProposerName());
        }
        if (StringUtils.isNotBlank(request.getDepartmentName())) {
            Department department = departmentService.findDepartmentByDepartmentName(request.getDepartmentName());
            wrapper.eq(Proposal::getDepartmentId, department.getId());
        }
        if (StringUtils.isNotBlank(request.getStartDate()) && StringUtils.isNotBlank(request.getEndDate())) {
            wrapper.between(Proposal::getProposalDate, request.getStartDate(), request.getEndDate());
        }
        return wrapper;
    }


}
