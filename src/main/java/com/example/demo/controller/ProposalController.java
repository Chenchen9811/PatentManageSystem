package com.example.demo.controller;

import com.example.demo.common.CommonResult;
import com.example.demo.request.NewProposalRequest;
import com.example.demo.service.ProposalService;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@RequestMapping("/proposal")
public class ProposalController {

    @Resource
    private ProposalService proposalService;

    @ResponseBody
    @PostMapping(path = "/post")
    public CommonResult newProposal(@Valid @RequestBody NewProposalRequest request, BindingResult bindingResult) {
        CommonResult result = null;
        try {
            result = proposalService.newProposal(request);
        } catch (Exception e) {
            e.printStackTrace();
            return CommonResult.failed(e.getMessage());
        }
        return result;
    }
}
