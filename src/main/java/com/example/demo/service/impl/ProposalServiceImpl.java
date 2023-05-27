package com.example.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.demo.Utils.CommonUtil;
import com.example.demo.Utils.HostHolder;
import com.example.demo.Utils.PageInfoUtil;
import com.example.demo.common.CommonResult;
import com.example.demo.common.ReviewStatus;
import com.example.demo.entity.*;
import com.example.demo.response.ProposalVo1;
import com.example.demo.response.ReviewVo;
import com.example.demo.mapper.InventorMapper;
import com.example.demo.mapper.ProposalMapper;
import com.example.demo.mapper.ReviewMapper;
import com.example.demo.request.AddReviewRequest;
import com.example.demo.request.GetProposalRequest1;
import com.example.demo.request.NewProposalRequest;
import com.example.demo.service.DepartmentService;
import com.example.demo.service.ProposalService;
import com.example.demo.service.UserService;
import com.example.demo.service.manager.ProposalManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

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

    @Resource
    private DepartmentService departmentService;

    @Resource
    private ReviewMapper reviewMapper;

    @Override
    public Proposal findProposalByProposerName(String proposerName) {
        return proposalMapper.selectOne(new LambdaQueryWrapper<Proposal>().eq(Proposal::getProposerName, proposerName));
    }

    @Override
    public Proposal findProposalByProposalId(Long proposalId) {
        return proposalMapper.selectById(proposalId);
    }

    @Override
    public CommonResult getCode(String typeName) {
        try {
            return CommonResult.success(CommonUtil.generateCode(typeName), "生成编号成功");
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw e;
        }
    }

    @Transactional(rollbackFor = Exception.class)
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
            List<GetProposalRequest1.Criteria.KV> items = request.getCriteria().getItems();
            for (GetProposalRequest1.Criteria.KV kv : items) {
                String key = kv.getKey();
                switch (key) {
                    case "inventorCode" : {
                        inventorWrapper.eq(Inventor::getInventorCode, kv.getValue());
                        break;
                    }
                    case "inventorName" : {
                        inventorWrapper.eq(Inventor::getInventorName, kv.getValue());
                        break;
                    }
                }
            }
//            if (StringUtils.isNotBlank(request.getCriteria().getItems().getInventorCode())) {
//                inventorWrapper.eq(Inventor::getInventorCode, request.getCriteria().getItems().getInventorCode());
//            }
//            if (StringUtils.isNotBlank(request.getCriteria().getItems().getInventorName())) {
//                inventorWrapper.eq(Inventor::getInventorName, request.getCriteria().getItems().getInventorName());
//            }
            List<Inventor> inventors = inventorMapper.selectList(inventorWrapper);
            for (Inventor inventor : inventors) {
                if (!proposalIds.contains(inventor.getProposalId())) {
                    proposalList.add(proposalMapper.selectOne(new LambdaQueryWrapper<Proposal>().eq(Proposal::getId, inventor.getProposalId())));
                }
            }
//            PageInfo<Proposal> pageInfo = PageInfoUtil.getPageInfo(proposalList, request.getPageIndex(), request.getPageSize());
            log.info("pageIndex:{}, pageSize:{}", request.getPageNum(), request.getPageSize());
            // Proposal和Inventor转ProposalVo
            List<ProposalVo1> vo1List = new ArrayList<>();
            for (Proposal proposal : proposalList) {
                ProposalVo1 vo = new ProposalVo1();
                List<Inventor> inventorList = inventorMapper.selectList(new LambdaQueryWrapper<Inventor>().eq(Inventor::getProposalId, proposal.getId()));
                vo.setProposalCode(proposal.getProposalCode());
                vo.setProposalName(proposal.getProposalName());
                vo.setProposalType(proposal.getProposalType());
                vo.setProposerName(proposal.getProposerName());
                vo.setProposalDate(proposal.getProposalDate().toString());
                vo.setInventorNameList(inventorList.stream().map(Inventor::getInventorName).collect(Collectors.toList()));
                vo.setDepartmentName(departmentService.findDepartmentById(proposal.getDepartmentId()).getDepartmentName());
                vo1List.add(vo);
            }
            return CommonResult.success(PageInfoUtil.getPageInfo(vo1List, request.getPageNum(), request.getPageSize()), "查找成功!");
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public CommonResult getAllDepartments() throws Exception {
        try {
            List<Department> departmentList = departmentService.getAllDepartments();
            return CommonResult.success(departmentList.stream()
                    .map(Department::getDepartmentName)
                    .collect(Collectors.toList()),"查找成功!");
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public CommonResult review(AddReviewRequest request) throws Exception {
        try {
            Review review = new Review();
            Proposal proposal = proposalMapper.selectOne(new LambdaQueryWrapper<Proposal>().eq(Proposal::getProposalCode, request.getProposalCode()));
            review.setProposalId(proposal.getId());
            review.setCurrentReviewState(request.getResult());
            review.setSuggestion(request.getSuggestion());
            review.setReviewerId(hostHolder.getUser().getId());
            review.setResult(request.getResult().equals(ReviewStatus.FAILED.getCode())? ReviewStatus.FAILED.getMessage() : ReviewStatus.PASSED.getMessage());
            review.setReviewDate(new Timestamp(System.currentTimeMillis()));
            reviewMapper.insert(review);
            return CommonResult.success(null, "添加审批结果成功");
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public CommonResult getReview(String proposalCode) throws Exception {

        try {
            Review review = proposalMapper.findReviewByProposalCode(proposalCode);
            User reviewer = userService.findUserByUserId(review.getReviewerId());
            Role reviewerRole = userService.findRoleByUserId(reviewer.getId());
            return CommonResult.success(new ReviewVo(reviewer.getUserName(), reviewerRole.getRoleName(),
                    review.getReviewDate().toString(), review.getResult()), "查找成功");
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public List<Proposal> findProposalListByIds(List<Long> proposalIds) {
        return proposalMapper.selectBatchIds(proposalIds);
    }
}
