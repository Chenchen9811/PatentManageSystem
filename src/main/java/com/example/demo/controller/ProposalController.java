package com.example.demo.controller;

import com.example.demo.common.CommonResult;
import com.example.demo.request.AddReviewRequest;
import com.example.demo.request.GetProposalFileRequest;
import com.example.demo.request.GetProposalRequest1;
import com.example.demo.request.NewProposalRequest;
import com.example.demo.service.ProposalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/proposal")
@CrossOrigin(origins = "*")
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

    @ResponseBody
    @PostMapping("/getProposalList1")
    public CommonResult getProposalList1(@Valid @RequestBody GetProposalRequest1 request, BindingResult bindingResult) {
        CommonResult result = null;
        try {
            result = proposalService.getProposalList1(request);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return CommonResult.failed(e.getMessage());
        }
    }

    @ResponseBody
    @GetMapping("/getAllDepartments")
    public CommonResult getAllDepartments() {
        CommonResult result = null;
        try {
            result = proposalService.getAllDepartments();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return CommonResult.failed(e.getMessage());
        }
    }

    @ResponseBody
    @PostMapping("/review")
    public CommonResult review(@Valid @RequestBody AddReviewRequest request, BindingResult bindingResult) {
        CommonResult result = null;
        try {
            result = proposalService.review(request);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return CommonResult.failed(e.getMessage());
        }
    }

    @ResponseBody
    @GetMapping("/getReview/{proposalCode}")
    public CommonResult getReview(@PathVariable("proposalCode") String proposalCode) {
        try {
            return proposalService.getReview(proposalCode);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return CommonResult.failed(e.getMessage());
        }
    }

    @ResponseBody
    @GetMapping("/getCode")
    public CommonResult getCode(@RequestParam("typeName") String typeName) {
        CommonResult result = null;
        try {
            result = proposalService.getCode(typeName);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return CommonResult.failed(e.getMessage());
        }
    }

    @ResponseBody
    @PostMapping("/getFileList")
    public CommonResult getFileList(@RequestBody GetProposalFileRequest request) {
        try {
            return proposalService.getFileList(request);
        } catch (Exception e) {
//            e.printStackTrace();
            return CommonResult.failed(e.getMessage());
        }

    }

}
