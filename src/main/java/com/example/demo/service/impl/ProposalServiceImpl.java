package com.example.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.demo.Utils.HostHolder;
import com.example.demo.common.CommonResult;
import com.example.demo.entity.Inventor;
import com.example.demo.entity.Proposal;
import com.example.demo.mapper.InventorMapper;
import com.example.demo.mapper.ProposalMapper;
import com.example.demo.request.NewProposalRequest;
import com.example.demo.service.ProposalService;
import com.example.demo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class ProposalServiceImpl implements ProposalService {
    @Resource
    private ProposalMapper proposalMapper;

    @Resource
    private InventorMapper inventorMapper;

    @Resource
    private HostHolder hostHolder;

    @Resource
    private UserService userService;

    @Override
    @Transactional
    public CommonResult newProposal(NewProposalRequest request) throws Exception{
        try {
            // 校验重复
            Proposal proposal = proposalMapper.selectOne(new QueryWrapper<Proposal>().eq("proposal_name", request.getProposerName()));
            if (!ObjectUtils.isEmpty(proposal)) {
                return CommonResult.failed("提案名称重复");
            }
            proposal = new Proposal();
            proposal.setProposalCode(request.getProposalCode());
            proposal.setProposalName(request.getProposalName());
            proposal.setProposalType(request.getPatentType());
            proposal.setProposerName(request.getProposerName());
            proposal.setProposalDate(new Date(System.currentTimeMillis()));
            proposal.setSubstance(request.getDetailText());
            proposal.setProposerId(userService.findUserByUserName(request.getProposerName()).getId());
            proposalMapper.insert(proposal);
            List<NewProposalRequest.InventorVo> inventorList = request.getListOfInventor();
            Collections.sort(inventorList, new Comparator<NewProposalRequest.InventorVo>() {
                @Override
                public int compare(NewProposalRequest.InventorVo o1, NewProposalRequest.InventorVo o2) {
                    return Integer.valueOf(o1.getRate()) - Integer.valueOf(o2.getRate());
                }
            });
            int len = inventorList.size();
            for (int i = 0; i < len; i ++) {
                NewProposalRequest.InventorVo inventorVo = inventorList.get(i);
                Inventor inventor = new Inventor();
                inventor.setProposalId(proposal.getId());
                inventor.setContribute(new BigDecimal("0." + inventorVo.getRate()));
                inventor.setRate(i + 1);
                inventor.setCreateTime(new Timestamp(System.currentTimeMillis()));
                inventor.setCreateUser(hostHolder.getUser().getId());
                inventor.setUserId(hostHolder.getUser().getId());
                inventorMapper.insert(inventor);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }

        return CommonResult.success(null, "新增提案成功！");
    }
}
