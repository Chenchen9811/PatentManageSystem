package com.example.demo.service;

import com.example.demo.common.CommonResult;
import com.example.demo.entity.Proposal;
import com.example.demo.request.AddReviewRequest;
import com.example.demo.request.GetProposalFileRequest;
import com.example.demo.request.GetProposalRequest1;
import com.example.demo.request.NewProposalRequest;
import com.example.demo.response.ProposalVo1;

import java.util.List;

public interface ProposalService {
    CommonResult newProposal(NewProposalRequest request) throws Exception;

    List<ProposalVo1> getProposalList1(GetProposalRequest1 request) throws Exception;

    CommonResult getAllDepartments() throws Exception;

    CommonResult review(AddReviewRequest request) throws Exception;

    CommonResult getReview(String reviewCode) throws Exception;

    Proposal findProposalByProposerName(String proposerName);

    Proposal findProposalByProposalName(String proposalName);

    Proposal findProposalByProposalId(Long proposalId);

    Proposal findProposalByProposalCode(String proposalCode);

    CommonResult getCode(String typeName);

    List<Proposal> findProposalListByIds(List<Long> proposalIds);

    CommonResult getFileList(GetProposalFileRequest request);
}
