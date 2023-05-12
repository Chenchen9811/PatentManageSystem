package com.example.demo.controller;

import com.example.demo.common.CommonResult;
import com.example.demo.request.NewAgencyRequest;
import com.example.demo.service.AgencyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@RequestMapping("/agency")
@Slf4j
public class AgencyController {
    @Resource
    private AgencyService agencyService;

    @ResponseBody
    @PostMapping("/newAgency")
    public CommonResult newAgency(@Valid @RequestBody NewAgencyRequest request, BindingResult bindingResult) {
        CommonResult result = null;
        try {
            result = agencyService.newAgency(request);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return CommonResult.failed(e.getMessage());
        }
    }

    @ResponseBody
    @GetMapping("/getAgency")
    public CommonResult getAgency(@RequestParam("pageIndex") Integer pageIndex, @RequestParam("pageSize") Integer pageSize) {
        CommonResult result = null;
        try {
            result = agencyService.getAgency(pageIndex, pageSize);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return CommonResult.failed(e.getMessage());
        }
    }
}
