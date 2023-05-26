package com.example.demo.service.impl;

import com.example.demo.common.CommonResult;
import com.example.demo.request.NewBillRequest;
import com.example.demo.service.BillService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BillServiceImpl implements BillService {
    @Override
    public CommonResult newBill(NewBillRequest request) {
        try {
            // 验重

            return null;
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw e;
        }
    }
}
