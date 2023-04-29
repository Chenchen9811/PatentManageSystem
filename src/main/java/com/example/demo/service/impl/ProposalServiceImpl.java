package com.example.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.demo.Utils.CommonUtil;
import com.example.demo.Utils.HostHolder;
import com.example.demo.Utils.PageInfoUtil;
import com.example.demo.common.CommonResult;
import com.example.demo.entity.Inventor;
import com.example.demo.entity.Proposal;
import com.example.demo.entity.User;
import com.example.demo.entity.vo.ProposalVo1;
import com.example.demo.mapper.InventorMapper;
import com.example.demo.mapper.ProposalMapper;
import com.example.demo.request.GetProposalRequest1;
import com.example.demo.request.NewProposalRequest;
import com.example.demo.service.ProposalService;
import com.example.demo.service.UserService;
import com.example.demo.service.manager.ProposalManager;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Stream;

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

    @Resource
    private ProposalManager proposalManager;



    @Transactional
    @Override
    public CommonResult newProposal(NewProposalRequest request) throws Exception{
        try {
            // 校验重复
            Proposal proposal = proposalMapper.selectOne(new QueryWrapper<Proposal>().eq("proposal_name", request.getProposerName()));
            if (!ObjectUtils.isEmpty(proposal)) {
                return CommonResult.failed("提案已存在");
            }
            proposal = new Proposal();
            proposal.setProposalCode(request.getProposalCode());
            proposal.setProposalName(request.getProposalName());
            proposal.setProposalType(request.getPatentType());
            proposal.setProposerName(request.getProposerName());
            proposal.setProposalDate(new Timestamp(System.currentTimeMillis()));
            proposal.setSubstance(request.getDetailText());
            // 获取提案人信息
            User proposer = userService.findUserByUserName(request.getProposerName());
            proposal.setProposerId(proposer.getId());
            proposal.setDepartmentId(proposer.getDepartmentId());
            proposal.setProposerCode(proposer.getUserCode());
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
                User user = userService.findUserByUserName(inventorVo.getInventorName());
                Inventor inventor = new Inventor();
                inventor.setProposalId(proposal.getId());
                inventor.setContribute(new BigDecimal("0." + inventorVo.getRate()));
                inventor.setRate(i + 1);
                inventor.setCreateTime(new Timestamp(System.currentTimeMillis()));
                inventor.setCreateUser(hostHolder.getUser().getId());
                inventor.setInventorId(user.getId());
                inventor.setInventorCode(user.getUserCode());
                inventor.setInventorName(user.getUserName());
                inventorMapper.insert(inventor);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
        return CommonResult.success(null, "新增提案成功！");
    }

    @Override
    public CommonResult getProposalList1(GetProposalRequest1 request) throws Exception {
        try {
            LambdaQueryWrapper<Proposal> wrapper = proposalManager.buildWrapperByRequest1(request);
            List<Proposal> proposalList = proposalMapper.selectList(wrapper);
            Set<Long> proposalIds = new HashSet<>();
            for (Proposal proposal : proposalList) {
                proposalIds.add(proposal.getId());
            }
            LambdaQueryWrapper<Inventor> inventorWrapper = new LambdaQueryWrapper<>();
            if (StringUtils.isNotBlank(request.getInventorCode())) {
                inventorWrapper.eq(Inventor::getInventorCode, request.getInventorCode());
            }
            if (StringUtils.isNotBlank(request.getInventorName())) {
                inventorWrapper.eq(Inventor::getInventorName, request.getInventorName());
            }
            List<Inventor> inventors = inventorMapper.selectList(inventorWrapper);
            for (Inventor inventor : inventors) {
                if (!proposalIds.contains(inventor.getProposalId())) {
                    proposalList.add(proposalMapper.selectOne(new LambdaQueryWrapper<Proposal>().eq(Proposal::getId, inventor.getProposalId())));
                }
            }
//            PageInfo<Proposal> pageInfo = PageInfoUtil.getPageInfo(proposalList, request.getPageIndex(), request.getPageSize());
            log.info("pageIndex:{}, pageSize:{}", request.getPageIndex(), request.getPageSize());
            // Proposal和Inventor转ProposalVo
            List<ProposalVo1> vo1List = new ArrayList<>();
            for (Proposal proposal : proposalList) {
                ProposalVo1 vo = new ProposalVo1();
                Inventor i = inventorMapper.selectOne(new LambdaQueryWrapper<Inventor>().eq(Inventor::getProposalId, proposal.getId()));
                vo.setProposalCode(proposal.getProposalCode());
                vo.setProposalName(proposal.getProposalName());
                vo.setProposalType(proposal.getProposalType());
                vo.setProposerName(proposal.getProposerName());
                vo.setProposalDate(proposal.getProposalDate().toString());
                vo.setInventorName(i.getInventorName());
                vo1List.add(vo);
            }
            return CommonResult.success(PageInfoUtil.getPageInfo(vo1List, request.getPageIndex(), request.getPageSize()), "查找成功!");
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw new Exception(e.getMessage());
        }
    }
}
