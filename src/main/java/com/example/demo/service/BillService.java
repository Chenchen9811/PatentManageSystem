package com.example.demo.service;

import com.example.demo.common.CommonResult;
import com.example.demo.request.NewBillRequest;

public interface BillService {
    CommonResult newBill(NewBillRequest request);
}
