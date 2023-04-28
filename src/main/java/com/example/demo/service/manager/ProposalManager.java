package com.example.demo.service.manager;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.demo.entity.Proposal;
import com.example.demo.request.GetProposalRequest1;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
public class ProposalManager {

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
        if (ObjectUtils.isNotEmpty(request.getProposalType())) {
            wrapper.eq(Proposal::getProposalState, request.getProposalState());
        }
        if (StringUtils.isNotBlank(request.getProposerCode())) {
            wrapper.eq(Proposal::getProposerCode, request.getProposerCode());
        }
        if (StringUtils.isNotBlank(request.getProposerName())) {
            wrapper.eq(Proposal::getProposerName, request.getProposerName());
        }
        if (StringUtils.isNotBlank(request.getStartDate()) && StringUtils.isNotBlank(request.getEndDate())) {
            wrapper.between(Proposal::getProposalDate, request.getStartDate(), request.getEndDate());
        }
        return wrapper;
    }


}
