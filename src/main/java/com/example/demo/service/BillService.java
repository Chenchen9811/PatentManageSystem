package com.example.demo.service;

import com.example.demo.common.CommonResult;
import com.example.demo.entity.Bill;
import com.example.demo.request.GetBillListRequest;
import com.example.demo.request.NewBillRequest;
import com.example.demo.request.UpdateBillRequest;

public interface BillService {
    CommonResult newBill(NewBillRequest request);

    Bill findBillByBillCode(String billCode);

    CommonResult getBill(GetBillListRequest request);

    CommonResult deleteBill(String billCode);

    CommonResult updateBill(UpdateBillRequest request);
}
