package com.example.demo.service.manager;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.demo.entity.Agency;
import com.example.demo.entity.Bill;
import com.example.demo.entity.Proposal;
import com.example.demo.request.Criteria;
import com.example.demo.request.GetBillListRequest;
import com.example.demo.service.AgencyService;
import com.example.demo.service.ProposalService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class BillManager {

    @Resource
    private ProposalService proposalService;

    @Resource
    private AgencyService agencyService;

    public LambdaQueryWrapper<Bill> getWrapper(GetBillListRequest request) {
        LambdaQueryWrapper<Bill> wrapper  = new LambdaQueryWrapper<>();
        List<Criteria.KV> items = request.getCriteria().getItems();
        for (Criteria.KV kv : items) {
            switch (kv.getKey()) {
                case "proposalName" : {
                    Proposal proposal = proposalService.findProposalByProposalName(kv.getValue());
                    wrapper.eq(Bill::getProposalId, proposal.getId());
                    break;
                }
                case "agency" : {
                    if (kv.getValue().equals("0")) break;
                    Agency agency = agencyService.findAgencyByName(kv.getValue());
                    wrapper.eq(Bill::getAgencyId, agency.getId());
                    break;
                }
                case "billCode" : {
                    wrapper.eq(Bill::getBillCode, kv.getValue());
                    break;
                }
                case "payStatus" : {
                    if (kv.getValue().equals("0")) break;
                    wrapper.eq(Bill::getPayStatus, kv.getValue());
                    break;
                }
                case "payDateBegin" : {
                    String endDate = null;
                    for (Criteria.KV kV : items) {
                        if (kV.getKey().equals("payDateEnd")) {
                            endDate = kV.getValue();
                            break;
                        }
                    }
                    wrapper.between(Bill::getPayDate, kv.getValue(), endDate);
                    break;
                }
            }
        }
        return wrapper;
    }
}
