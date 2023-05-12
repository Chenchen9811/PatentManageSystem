package com.example.demo.controller;

import com.example.demo.common.CommonResult;
import com.example.demo.request.GetTrademarkRequest;
import com.example.demo.request.NewTrademarkRequest;
import com.example.demo.service.TrademarkService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@RequestMapping("/trademark")
@Slf4j
public class TrademarkController {
    @Resource
    private TrademarkService trademarkService;

    @ResponseBody
    @PostMapping("/newTrademark")
    public CommonResult newTrademark(@Valid @RequestBody NewTrademarkRequest request, BindingResult bindingResult) {
        CommonResult result = null;
        try {
            result = trademarkService.newTrademark(request);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return CommonResult.failed(e.getMessage());
        }
    }

    @ResponseBody
    @GetMapping("/getTrademark")
    public CommonResult getTrademark(@RequestBody GetTrademarkRequest request) {
        CommonResult result = null;
        try {
            result = trademarkService.getTrademark(request);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return CommonResult.failed(e.getMessage());
        }
    }

    @ResponseBody
    @GetMapping("/departmentTrademark")
    public CommonResult getDepartmentTrademark(@RequestParam("pageIndex") Integer pageIndex,
                                               @RequestParam("pageSize") Integer pageSize,
                                               @RequestParam("isDepartment") Integer isDepartment) {
        CommonResult result = null;
        try {
            result = trademarkService.getDepartmentTrademark(pageIndex, pageSize, isDepartment);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return CommonResult.failed(e.getMessage());
        }
    }
}
