package com.example.demo.service;

import com.example.demo.common.CommonResult;
import com.example.demo.request.NewProposalRequest;

public interface ProposalService {
    CommonResult newProposal(NewProposalRequest request) throws Exception;
}
