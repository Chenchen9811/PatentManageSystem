package com.example.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.demo.Utils.CommonUtil;
import com.example.demo.Utils.HostHolder;
import com.example.demo.Utils.PageInfoUtil;
import com.example.demo.common.CommonResult;
import com.example.demo.entity.Department;
import com.example.demo.entity.Trademark;
import com.example.demo.entity.User;
import com.example.demo.mapper.TrademarkMapper;
import com.example.demo.request.GetTrademarkRequest;
import com.example.demo.request.NewTrademarkRequest;
import com.example.demo.response.GetTrademarkResponse;
import com.example.demo.service.DepartmentService;
import com.example.demo.service.TrademarkService;
import com.example.demo.service.UserService;
import com.example.demo.service.manager.TrademarkManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TrademarkServiceImpl implements TrademarkService {
    @Resource
    private TrademarkMapper trademarkMapper;

    @Resource
    private UserService userService;

    @Resource
    private DepartmentService departmentService;

    @Resource
    private TrademarkManager trademarkManager;

    @Resource
    private HostHolder hostHolder;


    @Override
    public Trademark findTrademarkByName(String trademarkName) {
        return trademarkMapper.selectOne(new LambdaQueryWrapper<Trademark>().eq(Trademark::getTrademarkName, trademarkName));
    }

    @Override
    public CommonResult getDepartmentTrademark(Integer pageIndex, Integer pageSize, Integer isDepartment) throws Exception {
        try {
            User user = hostHolder.getUser();
            List<Trademark> trademarkList = null;
            if (isDepartment == 0) {
                Department department = departmentService.findDepartmentById(user.getDepartmentId());
                trademarkList = trademarkMapper.selectList(new LambdaQueryWrapper<Trademark>().eq(Trademark::getDepartmentId, department.getId()));
            } else {
                trademarkList = trademarkMapper.selectList(new LambdaQueryWrapper<Trademark>().eq(Trademark::getInventorId, user.getId()));
            }
            return CommonResult.success(PageInfoUtil.getPageInfo(trademarkList.stream().map(trademark -> {
                GetTrademarkResponse response = new GetTrademarkResponse();
                response.setTrademarkCode(trademark.getTrademarkCode());
                response.setTrademarkType(trademark.getTrademarkType());
                response.setTrademarkName(trademark.getTrademarkName());
                response.setCurrentStatus(trademark.getCurrentStatus());
                response.setCopyRightCode(trademark.getCopyRightCode());
                response.setRightStatus(trademark.getRightStatus());
                response.setInventorName(userService.findUserByUserId(trademark.getInventorId()).getUserName());
                return response;
            }).collect(Collectors.toList()), pageIndex, pageSize), "查找成功");
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public CommonResult getTrademark(GetTrademarkRequest request) throws Exception {
        try {
            LambdaQueryWrapper<Trademark> wrapper = trademarkManager.getWrapperByGetTrademarkRequest(request);
            List<Trademark> trademarkList = trademarkMapper.selectList(wrapper);
            return CommonResult.success(PageInfoUtil.getPageInfo(trademarkList.stream().map(trademark -> {
                GetTrademarkResponse response = new GetTrademarkResponse();
                response.setTrademarkCode(trademark.getTrademarkCode());
                response.setTrademarkType(trademark.getTrademarkType());
                response.setTrademarkName(trademark.getTrademarkName());
                response.setCurrentStatus(trademark.getCurrentStatus());
                response.setCopyRightCode(trademark.getCopyRightCode());
                response.setRightStatus(trademark.getRightStatus());
                response.setInventorName(userService.findUserByUserId(trademark.getInventorId()).getUserName());
                return response;
            }).collect(Collectors.toList()), request.getPageIndex(), request.getPageSize()), "查找成功");
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public CommonResult newTrademark(NewTrademarkRequest request) throws Exception {
        try {
            Trademark trademark = this.findTrademarkByName(request.getTrademarkName());
            if (null != trademark) {
                return CommonResult.failed("该商标已存在");
            }
            User user = userService.findUserByUserName(request.getInventorName());
            Department department = departmentService.findDepartmentByDepartmentName(request.getDepartmentName());
            trademark = new Trademark();
            trademark.setTrademarkCode(request.getTrademarkCode());
            trademark.setTrademarkOwner(request.getTrademarkOwner());
            trademark.setTrademarkType(request.getTrademarkType());
            trademark.setTrademarkName(request.getTrademarkName());
            trademark.setInventorId(user.getId());
            trademark.setRightStatus(request.getRightStatus());
            trademark.setCurrentStatus(request.getCurrentStatus());
            trademark.setCopyRightCode(request.getCopyRightCode());
            trademark.setAgency(request.getAgency());
            trademark.setDepartmentId(department.getId());
            trademark.setTrademarkDesign("xxx");
            if (StringUtils.isNotBlank(request.getApplyDate())) {
                trademark.setApplyDate(CommonUtil.stringDateToTimeStamp(request.getApplyDate()));
            }
            if (StringUtils.isNotBlank(request.getGrantDate())) {
                trademark.setGrantDate(CommonUtil.stringDateToTimeStamp(request.getGrantDate()));
            }
            trademarkMapper.insert(trademark);
            return CommonResult.success(null, "添加商标成功");
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw new Exception(e.getMessage());
        }
    }
}
