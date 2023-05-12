package com.example.demo.service.manager;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.demo.entity.Trademark;
import com.example.demo.entity.User;
import com.example.demo.request.GetTrademarkRequest;
import com.example.demo.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class TrademarkManager {

    @Resource
    private UserService userService;

    public LambdaQueryWrapper<Trademark> getWrapperByGetTrademarkRequest(GetTrademarkRequest request) {
        LambdaQueryWrapper<Trademark> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(request.getTrademarkCode())) {
            wrapper.eq(Trademark::getTrademarkCode, request.getTrademarkCode());
        }
        if (StringUtils.isNotBlank(request.getTrademarkName())) {
            wrapper.eq(Trademark::getTrademarkName, request.getTrademarkName());
        }
        if (StringUtils.isNotBlank(request.getInventorName())) {
            User user = userService.findUserByUserName(request.getInventorName());
            wrapper.eq(Trademark::getInventorId, user.getId());
        }
        if (StringUtils.isNotBlank(request.getTrademarkOwner())) {
            wrapper.eq(Trademark::getTrademarkOwner, request.getTrademarkOwner());
        }
        if (StringUtils.isNotBlank(request.getCopyRightCode())) {
            wrapper.eq(Trademark::getCopyRightCode, request.getCopyRightCode());
        }
        if (StringUtils.isNotBlank(request.getTrademarkType())) {
            wrapper.eq(Trademark::getTrademarkType, request.getTrademarkType());
        }
        if (StringUtils.isNotBlank(request.getCurrentStatus())) {
            wrapper.eq(Trademark::getCurrentStatus, request.getCurrentStatus());
        }
        if (StringUtils.isNotBlank(request.getRightStatus())) {
            wrapper.eq(Trademark::getRightStatus, request.getRightStatus());
        }
        if (StringUtils.isNotBlank(request.getAgency())) {
            wrapper.eq(Trademark::getAgency, request.getAgency());
        }
        return wrapper;
    }
}
