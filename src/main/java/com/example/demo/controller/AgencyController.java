package com.example.demo.controller;

import com.example.demo.common.CommonResult;
import com.example.demo.request.GetAgencyRequest;
import com.example.demo.request.NewAgencyRequest;
import com.example.demo.request.UpdateAgencyRequest;
import com.example.demo.service.AgencyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@RequestMapping("/agency")
@Slf4j
@CrossOrigin(origins = "*")
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
    @PostMapping("/getList")
    public CommonResult getAgency(@RequestBody GetAgencyRequest request) {
        CommonResult result = null;
        try {
            result = agencyService.getAgency(request);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return CommonResult.failed(e.getMessage());
        }
    }

    @ResponseBody
    @GetMapping("/getAgency")
    public CommonResult getSingleAgency(@RequestParam("agencyCode") String agencyCode) {
        try {
            return agencyService.getSingleAgency(agencyCode);
        } catch (Exception e) {
//            e.printStackTrace();
            return CommonResult.failed(e.getMessage());
        }
    }

    @ResponseBody
    @PostMapping("/updateAgency")
    public CommonResult updateAgency(@Valid @RequestBody UpdateAgencyRequest request) {
        try {
            return agencyService.updateAgency(request);
        } catch (Exception e) {
//            e.printStackTrace();
            return CommonResult.failed(e.getMessage());
        }
    }

    @ResponseBody
    @DeleteMapping("/deleteAgency/{agencyCode}")
    public CommonResult deleteAgency(@PathVariable("agencyCode") String agencyCode) {
        try {
            return agencyService.deleteAgency(agencyCode);
        } catch (Exception e) {
//            e.printStackTrace();
            return CommonResult.failed(e.getMessage());
        }
    }
}
