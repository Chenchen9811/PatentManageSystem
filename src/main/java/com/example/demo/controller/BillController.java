package com.example.demo.controller;


import com.example.demo.common.CommonResult;
import com.example.demo.request.GetBillListRequest;
import com.example.demo.request.NewBillRequest;
import com.example.demo.request.UpdateBillRequest;
import com.example.demo.service.BillService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@RequestMapping("/bill")
@Slf4j
@CrossOrigin(origins = "*")
public class BillController {
    @Resource
    private BillService billService;

    @ResponseBody
    @PostMapping("/newBill")
    public CommonResult newBill(@Valid @RequestBody NewBillRequest request, BindingResult bindingResult) {
        try {
            return billService.newBill(request);
        } catch (Exception e) {
//            e.printStackTrace();
            return CommonResult.failed(e.getMessage());
        }
    }

    @ResponseBody
    @PostMapping("/getBill")
    public CommonResult getBill(@RequestBody GetBillListRequest request) {
        try {
            return billService.getBill(request);
        } catch (Exception e) {
//            e.printStackTrace();
            return CommonResult.failed(e.getMessage());
        }
    }

    @ResponseBody
    @DeleteMapping("/deleteBill/{billCode}")
    public CommonResult deleteBill(@PathVariable("billCode") String billCode) {
        try {
            return billService.deleteBill(billCode);
        } catch (Exception e) {
//            e.printStackTrace();
            return CommonResult.failed(e.getMessage());
        }
    }

    @ResponseBody
    @PostMapping("/updateBill")
    public CommonResult updateBill(@RequestBody UpdateBillRequest request) {
        try {
            return billService.updateBill(request);
        } catch (Exception e) {
//            e.printStackTrace();
            return CommonResult.failed(e.getMessage());
        }
    }
}
