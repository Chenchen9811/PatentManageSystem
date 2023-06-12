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
import java.sql.SQLOutput;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;

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
        Map<Character, Integer> map = new HashMap<>();
        for (Map.Entry<Character, Integer> entry : map.entrySet())
            System.out.println(proposalMapper.findReviewByProposalCode("aaaa").toString());
    }


    @Test
    public void testDate() throws ParseException {
//        Timestamp timestamp = CommonUtil.stringDateToTimeStamp(new String("2023-05-10"));
//        System.out.println(timestamp.toString());
//        System.out.println(CommonUtil.generateCode("POfficialFee"));
        Timestamp currentTimeStamp = new Timestamp(System.currentTimeMillis());
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
        java.util.Date cur = formatter.parse(CommonUtil.getYmdbyTimeStamp(currentTimeStamp), new ParsePosition(0));
        java.util.Date pre = formatter.parse("2022-6-12", new ParsePosition(0));
        System.out.println((cur.getTime() - pre.getTime()) / (1000 * 60 * 60 * 24));
    }

}
