package com.example.demo;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.demo.Utils.CommonUtil;
import com.example.demo.entity.*;
import com.example.demo.mapper.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class test {

    @Resource
    private UserMapper userMapper;

    @Resource
    private ProposalMapper proposalMapper;

    @Resource
    private PatentBonusMapper bonusMapper;

    @Resource
    private TrademarkBonusMapper trademarkBonusMapper;

    @Resource
    private TrademarkMapper trademarkMapper;

    @Resource
    private SoftwareBonusMapper softwareBonusMapper;




    @Test
    public void testMapper() {
//        Proposal proposal = new Proposal();
//        proposal.setProposalDate(new Timestamp(System.currentTimeMillis()));
//        proposal.setProposalType(1);
//        proposal.setProposalName("test");
//        proposal.setProposerId(111L);
//        proposal.setProposalCode("test");
//        proposal.setProposerName("test");
//        proposal.setReferenceBook("test");
//        proposalMapper.insert(proposal);
//        System.out.println(proposal.getId());
//        Review review = proposalMapper.findReviewByProposalCode("1");
//        System.out.println(review);
//        System.out.println(trademarkMapper.findTrademarkByInventorName("admin").toString());
//        System.out.println(softwareBonusMapper.selectList(new LambdaQueryWrapper<SoftwareBonus>()));
        System.out.println(proposalMapper.findReviewByProposalCode("aaaa").toString());
    }


    @Test
    public void testDate() {
        Timestamp timestamp = CommonUtil.stringDateToTimeStamp(new String("2023-05-10"));
        System.out.println(timestamp.toString());
        System.out.println(CommonUtil.generateCode("POfficialFee"));
    }

}
