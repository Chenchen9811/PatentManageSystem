package com.example.demo;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.demo.entity.Proposal;
import com.example.demo.entity.User;
import com.example.demo.mapper.ProposalMapper;
import com.example.demo.mapper.UserMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.Date;

@SpringBootTest
@RunWith(SpringRunner.class)
public class test {

    @Resource
    private UserMapper userMapper;

    @Resource
    private ProposalMapper proposalMapper;

    @Test
    public void testMapper() {
        Proposal proposal = new Proposal();
        proposal.setProposalDate(new Timestamp(System.currentTimeMillis()));
        proposal.setProposalType(1);
        proposal.setProposalName("test");
        proposal.setProposerId(111L);
        proposal.setProposalCode("test");
        proposal.setProposerName("test");
        proposal.setReferenceBook("test");
        proposalMapper.insert(proposal);
        System.out.println(proposal.getId());
    }

}
