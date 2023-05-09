package com.example.demo.controller;

import com.example.demo.common.CommonResult;
import com.example.demo.request.GetPatentRequest;
import com.example.demo.request.NewPatentRequest;
import com.example.demo.service.PatentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@RequestMapping("/patent")
@Slf4j
public class PatentController {
    @Resource
    private PatentService patentService;

    @ResponseBody
    @PostMapping("/newPatent")
    public CommonResult newPatent(@Valid @RequestBody NewPatentRequest request, BindingResult bindingResult) {
        CommonResult result = null;
        try {
            result = patentService.newPatent(request);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return CommonResult.failed(e.getMessage());
        }
    }

    @ResponseBody
    @GetMapping("/getPatent")
    public CommonResult getPatent(@RequestBody GetPatentRequest request) {
        CommonResult result = null;
        try {
            result = patentService.getPatent(request);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return CommonResult.failed(e.getMessage());
        }
    }

    @ResponseBody
    @GetMapping("/departmentPatent")
    public CommonResult departmentPatent(@RequestParam("pageIndex") Integer pageIndex, @RequestParam("pageSize") Integer pageSize) {
        CommonResult result = null;
        try {
            result = patentService.departmentPatent(pageIndex, pageSize);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return CommonResult.failed(e.getMessage());
        }
    }

    @ResponseBody
    @GetMapping("/myPatent")
    public CommonResult myPatent(@RequestParam("pageIndex") Integer pageIndex, @RequestParam("pageSize") Integer pageSize) {
        CommonResult result = null;
        try {
            result = patentService.myPatent(pageIndex, pageSize);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return CommonResult.failed(e.getMessage());
        }
    }
}
