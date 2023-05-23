package com.example.demo.controller;

import com.example.demo.common.CommonResult;
import com.example.demo.request.GetSoftwareOfficialFeeRequest;
import com.example.demo.request.GetSoftwareRequest;
import com.example.demo.request.NewSoftwareOfficialFeeRequest;
import com.example.demo.request.NewSoftwareRequest;
import com.example.demo.service.SoftwareService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@RequestMapping("/software")
@Slf4j
public class SoftwareController {
    @Resource
    private SoftwareService softwareService;


    @ResponseBody
    @PostMapping("/newSoftware")
    public CommonResult newSoftware(@Valid @RequestBody NewSoftwareRequest request, BindingResult bindingResult) {
        CommonResult result = null;
        try {
            result = softwareService.newSoftware(request);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return CommonResult.failed(e.getMessage());
        }
    }


    @ResponseBody
    @GetMapping("/getSoftware")
    public CommonResult getSoftware(@Valid @RequestBody GetSoftwareRequest request, BindingResult bindingResult) {
        CommonResult result = null;
        try {
            result = softwareService.getSoftware(request);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return CommonResult.failed(e.getMessage());
        }
    }

    @ResponseBody
    @GetMapping("/software")
    public CommonResult software(@RequestParam("pageIndex") Integer pageIndex,
                                 @RequestParam("pageSize") Integer pageSize,
                                 @RequestParam("isDepartment") Integer isDepartment) {
        CommonResult result = null;
        try {
            result = softwareService.software(pageIndex, pageSize, isDepartment);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return CommonResult.failed(e.getMessage());
        }
    }

    @ResponseBody
    @PostMapping("/newOfficialFee")
    public CommonResult newOfficialFee(@Valid @RequestBody NewSoftwareOfficialFeeRequest request, BindingResult bindingResult) {
        try {
            return softwareService.newOfficialFee(request);
        } catch (Exception e) {
            e.printStackTrace();
            return CommonResult.failed(e.getMessage());
        }
    }

    @ResponseBody
    @GetMapping("/getOfficialFee")
    public CommonResult getOfficialFee(@Valid @RequestBody GetSoftwareOfficialFeeRequest request, BindingResult bindingResult) {
        try {
            return softwareService.getOfficialFee(request);
        } catch (Exception e) {
            e.printStackTrace();
            return CommonResult.failed(e.getMessage());
        }
    }

    @ResponseBody
    @DeleteMapping("/deleteOfficialFee")
    public CommonResult deleteOfficialFee(@RequestParam("officialFeeCode") String officialFeeCode) {
        try {
            return softwareService.deleteOfficialFee(officialFeeCode);
        } catch (Exception e) {
            e.printStackTrace();
            return CommonResult.failed(e.getMessage());
        }
    }
}
