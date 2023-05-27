package com.example.demo.service.impl;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.demo.Utils.PageInfoUtil;
import com.example.demo.common.CommonResult;
import com.example.demo.entity.Agency;
import com.example.demo.mapper.AgencyMapper;
import com.example.demo.request.GetAgencyRequest;
import com.example.demo.request.NewAgencyRequest;
import com.example.demo.response.GetAgencyResponse;
import com.example.demo.service.AgencyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AgencyServiceImpl implements AgencyService {
    @Resource
    private AgencyMapper agencyMapper;

    @Override
    public Agency findAgencyByName(String agencyName) {
        return agencyMapper.selectOne(new LambdaQueryWrapper<Agency>().eq(Agency::getAgencyName, agencyName));
    }

    @Override
    public CommonResult getAgency(GetAgencyRequest request) throws Exception {
        try {
            LambdaQueryWrapper<Agency> wrapper = new LambdaQueryWrapper<>();
            if (request.getCriteria().getItems().size() != 0) {
                wrapper.eq(Agency::getAgencyName, request.getCriteria().getItems().get(0).getValue());
            }
            return CommonResult.success(PageInfoUtil.getPageInfo(
                    agencyMapper.selectList(wrapper).stream().map(agency -> {
                        GetAgencyResponse response = new GetAgencyResponse();
                        response.setAgencyName(agency.getAgencyName());
                        response.setAgencyCode(agency.getAgencyCode());
                        response.setAgentName(agency.getAgentName());
                        response.setAgencyHolder(agency.getAgencyHolder());
                        response.setAgencyPhone(agency.getAgencyPhone());
                        response.setAgencyEmail(agency.getAgencyEmail());
                        response.setAgencyAddress(agency.getAgencyAddress());
                        response.setAgencyRemark(agency.getAgencyRemark());
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
    public CommonResult newAgency(NewAgencyRequest request) throws Exception {
        try {
            // 验重
            Agency agency = this.findAgencyByName(request.getAgencyName());
            if (null != agency) {
                return CommonResult.failed("该代理机构已存在");
            }
            agency = new Agency();
            agency.setAgencyCode(request.getAgencyCode());
            agency.setAgencyName(request.getAgencyName());
            agency.setAgentName(request.getAgentName());
            agency.setAgencyHolder(request.getAgencyHolder());
            agency.setAgencyPhone(request.getAgencyPhone());
            agency.setAgencyEmail(request.getAgencyEmail());
            agency.setAgencyAddress(request.getAgencyAddress());
            agency.setAgencyRemark(request.getAgencyRemark());

            return agencyMapper.insert(agency) != 0 ?
                    CommonResult.success(null, "添加代理机构成功") : CommonResult.failed("添加失败");
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw new Exception(e.getMessage());
        }
    }
}
