package com.example.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.demo.Utils.HostHolder;
import com.example.demo.Utils.PageInfoUtil;
import com.example.demo.common.CommonResult;
import com.example.demo.entity.Department;
import com.example.demo.entity.Patent;
import com.example.demo.entity.PatentInventor;
import com.example.demo.entity.User;
import com.example.demo.entity.vo.GetPatentResponse;
import com.example.demo.mapper.PatentInventorMapper;
import com.example.demo.mapper.PatentMapper;
import com.example.demo.request.GetPatentRequest;
import com.example.demo.request.NewPatentRequest;
import com.example.demo.request.NewProposalRequest;
import com.example.demo.service.DepartmentService;
import com.example.demo.service.PatentService;
import com.example.demo.service.UserService;
import com.example.demo.service.manager.PatentManager;
import lombok.extern.slf4j.Slf4j;
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
public class PatentServiceImpl implements PatentService {
    @Resource
    private PatentMapper patentMapper;

    @Resource
    private PatentInventorMapper patentInventorMapper;

    @Resource
    private UserService userService;

    @Resource
    private HostHolder hostHolder;

    @Resource
    private PatentManager patentManager;

    @Resource
    private DepartmentService departmentService;

    @Override
    public CommonResult myPatent(Integer pageIndex, Integer pageSize) throws Exception {
        try {
            User user = hostHolder.getUser();
            List<PatentInventor> patentInventorList = patentInventorMapper.selectList(new LambdaQueryWrapper<PatentInventor>().eq(PatentInventor::getInventorId, user.getId()));
            List<Long> patentIds = patentInventorList.stream().map(PatentInventor::getPatentId).collect(Collectors.toList());
            List<Patent> patentList = patentMapper.selectBatchIds(patentIds);
            List<GetPatentResponse> responseList = new ArrayList<>();
            for (Patent patent : patentList) {
                GetPatentResponse response = new GetPatentResponse();
                List<PatentInventor> inventorList = patentInventorMapper.selectList(new LambdaQueryWrapper<PatentInventor>().eq(PatentInventor::getPatentId, patent.getId()));
                response.setPatentCode(patent.getPatentCode());
                response.setPatentType(patent.getPatentType());
                response.setPatentName(patent.getPatentName());
                response.setApplicationCode(patent.getApplicationCode());
                response.setApplicationDate(patent.getApplicationDate().toString());
                response.setGrantCode(patent.getGrantCode());
                response.setGrantDate(patent.getGrantDate().toString());
                response.setRightStatus(patent.getRightStatus());
                response.setCurrentProgram(patent.getCurrentProgram());
                response.setInventorNameList(inventorList.stream().map(PatentInventor::getInventorName).collect(Collectors.toList()));
                responseList.add(response);
            }
            return CommonResult.success(PageInfoUtil.getPageInfo(responseList, pageIndex, pageSize), "查找成功!");
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public CommonResult departmentPatent(Integer pageIndex, Integer pageSize) throws Exception {
        try {
            User user = hostHolder.getUser();
            Department department = departmentService.findDepartmentById(user.getDepartmentId());
            List<Patent> patentList = patentMapper.selectList(new LambdaQueryWrapper<Patent>().eq(Patent::getDepartmentId, department.getId()));
            List<GetPatentResponse> responseList = new ArrayList<>();
            for (Patent patent : patentList) {
                GetPatentResponse response = new GetPatentResponse();
                List<PatentInventor> inventorList = patentInventorMapper.selectList(new LambdaQueryWrapper<PatentInventor>().eq(PatentInventor::getPatentId, patent.getId()));
                response.setPatentCode(patent.getPatentCode());
                response.setPatentType(patent.getPatentType());
                response.setPatentName(patent.getPatentName());
                response.setApplicationCode(patent.getApplicationCode());
                response.setApplicationDate(patent.getApplicationDate().toString());
                response.setGrantCode(patent.getGrantCode());
                response.setGrantDate(patent.getGrantDate().toString());
                response.setRightStatus(patent.getRightStatus());
                response.setCurrentProgram(patent.getCurrentProgram());
                response.setInventorNameList(inventorList.stream().map(PatentInventor::getInventorName).collect(Collectors.toList()));
                responseList.add(response);
            }
            return CommonResult.success(PageInfoUtil.getPageInfo(responseList, pageIndex, pageSize), "查找成功!");
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public CommonResult getPatent(GetPatentRequest request) throws Exception {
        try {
            LambdaQueryWrapper<Patent> wrapper = patentManager.getWrapperByGetPatentRequest(request);
            List<Patent> patentList = patentMapper.selectList(wrapper);
            Set<Long> patentIds = new HashSet<>();
            for (Patent patent : patentList) {
                patentIds.add(patent.getId());
            }
            LambdaQueryWrapper<PatentInventor> patentInventorWrapper = new LambdaQueryWrapper<>();
            if (StringUtils.isNotBlank(request.getInventorName())) {
                patentInventorWrapper.eq(PatentInventor::getInventorName, request.getInventorName());
            }
            List<PatentInventor> patentInventors = patentInventorMapper.selectList(patentInventorWrapper);
            for (PatentInventor patentInventor : patentInventors) {
                if (!patentIds.contains(patentInventor.getPatentId())) {
                    patentList.add(patentMapper.selectOne(new LambdaQueryWrapper<Patent>().eq(Patent::getId, patentInventor.getPatentId())));
                }
            }
            List<GetPatentResponse> responseList = new ArrayList<>();
            for (Patent patent : patentList) {
                GetPatentResponse response = new GetPatentResponse();
                List<PatentInventor> inventorList = patentInventorMapper.selectList(new LambdaQueryWrapper<PatentInventor>().eq(PatentInventor::getPatentId, patent.getId()));
                response.setPatentCode(patent.getPatentCode());
                response.setPatentType(patent.getPatentType());
                response.setPatentName(patent.getPatentName());
                response.setApplicationCode(patent.getApplicationCode());
                response.setApplicationDate(patent.getApplicationDate().toString());
                response.setGrantCode(patent.getGrantCode());
                response.setGrantDate(patent.getGrantDate().toString());
                response.setRightStatus(patent.getRightStatus());
                response.setCurrentProgram(patent.getCurrentProgram());
                response.setInventorNameList(inventorList.stream().map(PatentInventor::getInventorName).collect(Collectors.toList()));
                responseList.add(response);
            }
            return CommonResult.success(PageInfoUtil.getPageInfo(responseList, request.getPageIndex(), request.getPageSize()), "查找成功!");
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public CommonResult newPatent(NewPatentRequest request) throws Exception {
        try {
            Patent patent = patentMapper.selectOne(new LambdaQueryWrapper<Patent>().eq(Patent::getPatentName, request.getPatentName()));
            if (null != patent) {
                return CommonResult.failed("该专利已存在");
            }
            Department department = departmentService.findDepartmentByDepartmentName(request.getDepartmentName());
            patent = new Patent();
            patent.setPatentCode(request.getPatentCode());
            patent.setPatentName(request.getPatentName());
            patent.setPatentType(request.getPatentType());
            patent.setAgency(request.getAgency());
            patent.setApplicationCode(request.getApplicationCode());
//            patent.setApplicationDate(Timestamp.valueOf(request.getApplicationDate()));
            patent.setApplicationDate(new Timestamp(System.currentTimeMillis()));
            patent.setGrantCode(request.getGrantCode());
//            patent.setGrantDate(Timestamp.valueOf(request.getGrantDate()))
            patent.setGrantDate(new Timestamp(System.currentTimeMillis()));
            patent.setCurrentProgram(request.getCurrentProgram());
            patent.setDepartmentId(department.getId());
            patentMapper.insert(patent);
            List<NewPatentRequest.Inventor> inventorList = request.getInventorList();
            Collections.sort(inventorList, new Comparator<NewPatentRequest.Inventor>() {
                @Override
                public int compare(NewPatentRequest.Inventor o1, NewPatentRequest.Inventor o2) {
                    return Integer.valueOf(o1.getRate()) - Integer.valueOf(o2.getRate());
                }
            });
            int size = inventorList.size();
            for (int i = 0; i < size; i ++) {
                PatentInventor patentInventor = new PatentInventor();
                NewPatentRequest.Inventor inventor = inventorList.get(i);
                User user = userService.findUserByUserName(inventor.getInventorName());
                patentInventor.setPatentId(patent.getId());
                patentInventor.setInventorName(inventor.getInventorName());
                patentInventor.setInventorCode(user.getUserCode());
                patentInventor.setContribute(inventor.getRate() == 100? new BigDecimal("1.00") : new BigDecimal("0."+ inventor.getRate()));
                patentInventor.setRate(i + 1);
                patentInventor.setCreateTime(new Timestamp(System.currentTimeMillis()));
                patentInventor.setCreateUser(hostHolder.getUser().getId());
                patentInventor.setInventorId(user.getId());
                patentInventorMapper.insert(patentInventor);
            }
            return CommonResult.success(null, "新增专利成功");
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw new Exception(e.getMessage());
        }
    }


}
