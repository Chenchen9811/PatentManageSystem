package com.example.demo.controller;

import com.alibaba.excel.EasyExcel;
import com.example.demo.Utils.CommonUtil;
import com.example.demo.Utils.PageInfoUtil;
import com.example.demo.common.CommonResult;
import com.example.demo.request.AddReviewRequest;
import com.example.demo.request.GetProposalFileRequest;
import com.example.demo.request.GetProposalRequest1;
import com.example.demo.request.NewProposalRequest;
import com.example.demo.response.GetSoftwareResponse;
import com.example.demo.response.ProposalExport;
import com.example.demo.response.ProposalVo1;
import com.example.demo.service.ProposalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.parameters.P;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

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
            List<ProposalVo1> responseList = proposalService.getProposalList1(request);
            if (responseList == null || responseList.size() == 0) {
                return CommonResult.failed("查找失败");
            }
            return CommonResult.success(PageInfoUtil.getPageInfo(responseList, request.getPageIndex(), request.getPageSize()), "查找成功");
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

    @ResponseBody
    @PostMapping("/export")
    public CommonResult export(@Valid @RequestBody GetProposalRequest1 request, HttpServletResponse response) {
        try {
            List<ProposalVo1> vo1List = proposalService.getProposalList1(request);
            if (vo1List == null || vo1List.size() == 0) {
                return CommonResult.failed("导出失败");
            }
            List<ProposalExport> responseList = vo1List.stream().map(vo1 -> {
                ProposalExport export = new ProposalExport();
                List<String> nameList = vo1.getInventorNameList();
                if (nameList.size() > 1) {
                    StringBuilder sb = new StringBuilder();
                    for (String name : nameList) {
                        sb.append(name + ",");
                    }
                    sb.deleteCharAt(sb.length() - 1);
                    export.setInventorNames(sb.toString());
                } else export.setInventorNames(nameList.get(0));
                export.setProposalCode(vo1.getProposalCode());
                export.setProposalName(vo1.getProposalName());
                export.setProposalDate(vo1.getProposalDate());
                export.setProposalState(vo1.getProposalState());
                export.setProposalType(vo1.getProposalType());
                export.setDepartmentName(vo1.getDepartmentName());
                export.setReviewState(CommonUtil.getProposalStatusString(vo1.getReviewState()));
                export.setProposerName(vo1.getProposerName());
                return export;
            }).collect(Collectors.toList());
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-disposition", "attachment;filename=提案信息.xlsx");
            EasyExcel.write(response.getOutputStream(), ProposalExport.class).sheet("提案信息").doWrite(responseList);
            return CommonResult.success(null, "导出成功");
        } catch (Exception e) {
//            e.printStackTrace();
            return CommonResult.failed(e.getMessage());
        }
    }

}
