package com.example.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.demo.Utils.CommonUtil;
import com.example.demo.Utils.HostHolder;
import com.example.demo.Utils.PageInfoUtil;
import com.example.demo.common.CommonResult;
import com.example.demo.common.ProposalStatus;
import com.example.demo.common.ProposalType;
import com.example.demo.common.ReviewStatus;
import com.example.demo.entity.*;
import com.example.demo.mapper.ProposalFileMapper;
import com.example.demo.request.GetProposalFileRequest;
import com.example.demo.response.GetProposalFileResponse;
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
import java.sql.Date;
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

    @Resource
    private ProposalFileMapper fileMapper;

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

    @Override
    public CommonResult getFileList(GetProposalFileRequest request) {
        try {
            LambdaQueryWrapper<Proposal> proposalWrapper = proposalManager.getWrapper(request);
            LambdaQueryWrapper<ProposalFile> fileWrapper = proposalManager.getFileWrapper(request);
            List<Proposal> proposalList = proposalMapper.selectList(proposalWrapper);
            if (proposalList.size() == 0) {
                return CommonResult.failed("没找到相关提案");
            }
            List<ProposalFile> fileList = fileMapper.selectList(fileWrapper);
            if (fileList.size() == 0) {
                return CommonResult.failed("没找到提案文件");
            }
            Set<Long> proposalIds = proposalList.stream().map(Proposal::getId).collect(Collectors.toSet());
            fileList.removeIf(file -> !proposalIds.contains(file.getProposalId()));
            if (fileList.size() == 0) {
                return CommonResult.failed("没找到与所查提案相关的提案文件");
            }
            Map<Long, Proposal> proposalMap = proposalList.stream().collect(Collectors.toMap(Proposal::getId, proposal -> proposal));
            Map<Long, User> userMap = userService.findUserListByIds(fileList.stream().map(ProposalFile::getUploaderId).distinct().collect(Collectors.toList()))
                    .stream().collect(Collectors.toMap(User::getId, user -> user));
            List<GetProposalFileResponse> responseList = fileList.stream().map(file -> {
                GetProposalFileResponse response = new GetProposalFileResponse();
                Proposal proposal = proposalMap.get(file.getProposalId());
                User uploader = userMap.get(file.getUploaderId());
                response.setProposalCode(proposal.getProposalCode());
                response.setProposalState(CommonUtil.getProposalStatusString(proposal.getProposalState()));
                response.setProposalName(proposal.getProposalName());
                response.setProposalType(CommonUtil.getProposalTypeString(proposal.getProposalType()));
                response.setUploadDate(file.getUploadDate().toString());
                response.setUploaderName(uploader.getUserName());
                response.setFileName(file.getFileName());
                return response;
            }).collect(Collectors.toList());
            return CommonResult.success(PageInfoUtil.getPageInfo(responseList, request.getPageIndex(), request.getPageSize()), "查找成功");
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw e;
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public CommonResult newProposal(NewProposalRequest request) throws Exception {
        try {
            // 校验重复
            Proposal proposal = proposalMapper.selectOne(new QueryWrapper<Proposal>().eq("proposal_name", request.getProposerName()));
            if (!ObjectUtils.isEmpty(proposal)) {
                return CommonResult.failed("提案已存在");
            }
            proposal = new Proposal();
            Department department = departmentService.findDepartmentByDepartmentName(request.getDepartmentName());
            proposal.setProposalCode(request.getProposalCode());
            proposal.setProposalName(request.getProposalName());
            proposal.setProposalType(CommonUtil.getProposalTypeCode(request.getPatentType()));
            proposal.setProposerName(request.getProposerName());
            proposal.setProposalDate(new Timestamp(System.currentTimeMillis()));
            proposal.setSubstance(request.getDetailText());
            proposal.setDepartmentId(department.getId());
            // 获取提案人信息
            User proposer = userService.findUserByUserName(request.getProposerName());
            proposal.setProposerId(proposer.getId());
            proposal.setProposerCode(proposer.getUserCode());
            proposal.setDepartmentId(proposer.getDepartmentId());
            proposal.setProposalState(CommonUtil.getProposalStatusCode("在审"));
            proposalMapper.insert(proposal);
            // 提案文件
            ProposalFile proposalFile = new ProposalFile();
            proposalFile.setProposalId(proposal.getId());
            proposalFile.setUploaderId(hostHolder.getUser().getId());
            proposalFile.setUploadDate(new Date(System.currentTimeMillis()));
            proposalFile.setFileUrl(CommonUtil.getFileUrl(request.getFileName()));
            proposalFile.setFileName(request.getFileName());
            fileMapper.insert(proposalFile);
            List<NewProposalRequest.InventorVo> inventorList = request.getListOfInventor();
//            inventorList.sort(Comparator.comparingInt(o -> Integer.parseInt(o.getRate())));
            Map<String, User> userMap = userService.findUserListByNames(inventorList.stream()
                            .map(NewProposalRequest.InventorVo::getInventorName).collect(Collectors.toList()))
                    .stream().collect(Collectors.toMap(User::getUserName, user -> user));
            int len = inventorList.size();
            List<Inventor> inventors = new ArrayList<>();
            for (int i = 0; i < len; i++) {
                NewProposalRequest.InventorVo inventorVo = inventorList.get(i);
                User user = userMap.get(inventorVo.getInventorName());
                Inventor inventor = new Inventor();
                inventor.setProposalId(proposal.getId());
                inventor.setContribute(new BigDecimal("0." + inventorVo.getRate()));
                inventor.setRate(i + 1);
                inventor.setCreateTime(new Timestamp(System.currentTimeMillis()));
                inventor.setCreateUser(hostHolder.getUser().getId());
                inventor.setInventorId(user.getId());
                inventor.setInventorCode(user.getUserCode());
                inventor.setInventorName(user.getUserName());
                inventors.add(inventor);
            }
            inventorMapper.insertBatchSomeColumn(inventors);
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
                    case "inventorCode": {
                        inventorWrapper.eq(Inventor::getInventorCode, kv.getValue());
                        break;
                    }
                    case "inventorName": {
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
            log.info("pageIndex:{}, pageSize:{}", request.getPageIndex(), request.getPageSize());
            // Proposal和Inventor转ProposalVo
            List<ProposalVo1> vo1List = new ArrayList<>();
            for (Proposal proposal : proposalList) {
                ProposalVo1 vo = new ProposalVo1();
                List<Inventor> inventorList = inventorMapper.selectList(new LambdaQueryWrapper<Inventor>().eq(Inventor::getProposalId, proposal.getId()));
                vo.setProposalCode(proposal.getProposalCode());
                vo.setProposalName(proposal.getProposalName());
                vo.setProposalType(CommonUtil.getProposalTypeString(proposal.getProposalType()));
                vo.setProposerName(proposal.getProposerName());
                vo.setProposalState(CommonUtil.getProposalStatusString(proposal.getProposalState()));
                vo.setProposalDate(proposal.getProposalDate().toString());
                vo.setInventorNameList(inventorList.stream().map(Inventor::getInventorName).collect(Collectors.toList()));
                vo.setDepartmentName(departmentService.findDepartmentById(proposal.getDepartmentId()).getDepartmentName());
                vo1List.add(vo);
            }
            return CommonResult.success(PageInfoUtil.getPageInfo(vo1List, request.getPageIndex(), request.getPageSize()), "查找成功!");
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
                    .collect(Collectors.toList()), "查找成功!");
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
            review.setSuggestion(request.getSuggestion());
            review.setReviewerId(hostHolder.getUser().getId());
            review.setResult(request.getResult().equals(ReviewStatus.FAILED.getCode()) ? ReviewStatus.FAILED.getMessage() : ReviewStatus.PASSED.getMessage());
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
            List<Review> reviewList = proposalMapper.findReviewListByProposalCode(proposalCode);
            if (reviewList.size() == 0) {
                return CommonResult.failed("没有相关审批结果");
            }
            Map<Long, User> reviewerMap = userService.findUserListByIds(reviewList.stream().distinct().map(Review::getReviewerId).collect(Collectors.toList()))
                    .stream().collect(Collectors.toMap(User::getId, user -> user));
            List<ReviewVo> reviewVoList = reviewList.stream().map(review -> {
                User reviewer = reviewerMap.get(review.getReviewerId());
                Role reviewerRole = userService.findRoleByUserId(reviewer.getId());
                ReviewVo reviewVo = new ReviewVo();
                reviewVo.setReviewDate(CommonUtil.getYmdbyTimeStamp(review.getReviewDate()));
                reviewVo.setReviewResult(review.getResult());
                reviewVo.setReviewerRoleName(reviewerRole.getRoleName());
                reviewVo.setReviewerName(reviewer.getUserName());
                return reviewVo;
            }).collect(Collectors.toList());
            Map<String, List<ReviewVo>> map = new HashMap<>();
            map.put("list", reviewVoList);
            return CommonResult.success(map, "查找成功");
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

    @Override
    public Proposal findProposalByProposalCode(String proposalCode) {
        return proposalMapper.selectOne(new LambdaQueryWrapper<Proposal>().eq(Proposal::getProposalCode, proposalCode));
    }

    @Override
    public Proposal findProposalByProposalName(String proposalName) {
        return proposalMapper.selectOne(new LambdaQueryWrapper<Proposal>().eq(Proposal::getProposalName, proposalName));
    }
}
