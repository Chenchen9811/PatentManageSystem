package com.example.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.demo.Utils.CommonUtil;
import com.example.demo.Utils.PageInfoUtil;
import com.example.demo.common.CommonResult;
import com.example.demo.entity.Agency;
import com.example.demo.entity.Bill;
import com.example.demo.entity.Proposal;
import com.example.demo.mapper.BillMapper;
import com.example.demo.request.GetBillListRequest;
import com.example.demo.request.NewBillRequest;
import com.example.demo.request.UpdateBillRequest;
import com.example.demo.response.GetBillListResponse;
import com.example.demo.service.AgencyService;
import com.example.demo.service.BillService;
import com.example.demo.service.ProposalService;
import com.example.demo.service.manager.BillManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class BillServiceImpl implements BillService {

    @Resource
    private BillMapper billMapper;

    @Resource
    private ProposalService proposalService;

    @Resource
    private AgencyService agencyService;

    @Resource
    private BillManager billManager;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public CommonResult updateBill(UpdateBillRequest request) {
        try {
            Bill bill = this.findBillByBillCode(request.getBillCode());
            bill.setDueAmount(request.getDueAmount());
            bill.setPayStatus(request.getPayStatus());
            bill.setProposalId(StringUtils.isNotBlank(request.getProposalName()) ?
                    proposalService.findProposalByProposalName(request.getProposalName()).getId() : bill.getProposalId());
            bill.setAgencyId(StringUtils.isNotBlank(request.getAgency()) ?
                    agencyService.findAgencyByName(request.getAgency()).getId() : bill.getAgencyId());
            bill.setActualPayAmount(StringUtils.isNotBlank(request.getActualPay()) ? request.getActualPay() : bill.getActualPayAmount());
            bill.setPayDate(StringUtils.isNotBlank(request.getActualPayDate()) ? CommonUtil.stringToDate(request.getActualPayDate()) : bill.getPayDate());
            bill.setRemark(StringUtils.isNotBlank(request.getRemark()) ? request.getRemark() : bill.getRemark());
            return billMapper.updateById(bill) == 0 ? CommonResult.failed("修改失败") : CommonResult.success(null, "修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw e;
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public CommonResult deleteBill(String billCode) {
        try {
            return billMapper.delete(new LambdaQueryWrapper<Bill>().eq(Bill::getBillCode, billCode)) == 0 ?
                    CommonResult.failed("删除失败") : CommonResult.success(null, "删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw e;
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public CommonResult getBill(GetBillListRequest request) {
        try {
            LambdaQueryWrapper<Bill> wrapper = billManager.getWrapper(request);
            List<Bill> billList = billMapper.selectList(wrapper);
            if (billList.size() == 0) {
                return CommonResult.failed("查找失败，没有找到相关账单信息");
            }
            Map<Long, Proposal> proposalMap = proposalService.findProposalListByIds(billList.stream().map(Bill::getProposalId).collect(Collectors.toList()))
                    .stream().collect(Collectors.toMap(Proposal::getId, proposal -> proposal));
            Map<Long, Agency> agencyMap = agencyService.findAgencyListByIds(billList.stream().map(Bill::getAgencyId).collect(Collectors.toList()))
                    .stream().collect(Collectors.toMap(Agency::getId, agency -> agency));
            List<GetBillListResponse> responseList = billList.stream().map(bill -> {
                GetBillListResponse response = new GetBillListResponse();
                Proposal proposal = proposalMap.get(bill.getProposalId());
                Agency agency = agencyMap.get(bill.getAgencyId());
                response.setProposalCode(proposal.getProposalCode());
                response.setProposalName(proposal.getProposalName());
                response.setAgency(agency.getAgencyName());
                response.setBillCode(bill.getBillCode());
                response.setDueAmount(bill.getDueAmount());
                response.setPayStatus(bill.getPayStatus());
                response.setActualPay(bill.getActualPayAmount());
                response.setRemark(bill.getRemark());
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
    public CommonResult newBill(NewBillRequest request) {
        try {
            // 验重
            Bill bill = this.findBillByBillCode(request.getBillCode());
            if (null != bill) {
                return CommonResult.failed("该账单已存在");
            }
            Proposal proposal = proposalService.findProposalByProposalName(request.getProposalName());
            if (null == proposal) {
                return CommonResult.failed("不存在该提案");
            }
            Agency agency = agencyService.findAgencyByName(request.getAgency());
            if (null == agency) {
                return CommonResult.failed("不存在该代理机构");
            }
            bill = new Bill();
            bill.setBillCode(request.getBillCode());
            bill.setActualPayAmount(request.getActualPay());
            bill.setProposalId(proposal.getId());
            bill.setAgencyId(agency.getId());
            bill.setPayStatus(request.getPayStatus());
            bill.setPayDate(CommonUtil.stringToDate(request.getActualPayDate()));
            bill.setRemark(request.getRemark());
            bill.setDueAmount(request.getDueAmount());
            return billMapper.insert(bill) == 0 ? CommonResult.failed("添加失败") : CommonResult.success(null, "添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public Bill findBillByBillCode(String billCode) {
        return billMapper.selectOne(new LambdaQueryWrapper<Bill>().eq(Bill::getBillCode, billCode));
    }
}
