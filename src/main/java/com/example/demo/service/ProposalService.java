package com.example.demo.service;

import com.example.demo.common.CommonResult;
import com.example.demo.entity.Proposal;
import com.example.demo.request.AddReviewRequest;
import com.example.demo.request.GetProposalRequest1;
import com.example.demo.request.NewProposalRequest;

public interface ProposalService {
    CommonResult newProposal(NewProposalRequest request) throws Exception;

    CommonResult getProposalList1(GetProposalRequest1 request) throws Exception;

    CommonResult getAllDepartments() throws Exception;

    CommonResult review(AddReviewRequest request) throws Exception;

    CommonResult getReview(String reviewCode) throws Exception;

    Proposal findProposalByProposerName(String proposerName);

    Proposal findProposalByProposalId(Long proposalId);

    CommonResult getCode(String typeName);
}
