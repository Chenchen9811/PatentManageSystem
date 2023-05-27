package com.example.demo.controller;


import com.example.demo.common.CommonResult;
import com.example.demo.request.NewBillRequest;
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
}
