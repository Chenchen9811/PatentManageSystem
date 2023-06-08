package com.example.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.entity.Proposal;
import com.example.demo.entity.Review;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProposalMapper extends BaseMapper<Proposal> {

    Review findReviewByProposalCode(String proposalCode);
    List<Review> findReviewListByProposalCode(String proposalCode);
}
