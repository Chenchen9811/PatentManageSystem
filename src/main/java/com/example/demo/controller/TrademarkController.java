package com.example.demo.controller;

import com.example.demo.common.CommonResult;
import com.example.demo.request.*;
import com.example.demo.service.TrademarkService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@RequestMapping("/trademark")
@Slf4j
@CrossOrigin(origins = "*")
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
    @PostMapping("/getList")
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

    @ResponseBody
    @PostMapping("/newBonus")
    public CommonResult newBonus(@Valid @RequestBody NewTrademarkBonusRequest request, BindingResult bindingResult) {
        CommonResult result = null;
        try {
            result = trademarkService.newBonus(request);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return CommonResult.failed(e.getMessage());
        }
    }

    @ResponseBody
    @PostMapping("/getBonusList")
    public CommonResult getBonus(@Valid @RequestBody GetTrademarkBonusRequest request, BindingResult bindingResult) {
        CommonResult result = null;
        try {
            result = trademarkService.getBonus(request);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return CommonResult.failed(e.getMessage());
        }
    }

    @ResponseBody
    @DeleteMapping("/deleteBonus/{id}")
    public CommonResult deleteBonus(@PathVariable("id") String id) {
        try {
            return trademarkService.deleteBonus(id);
        } catch (Exception e) {
            e.printStackTrace();
            return CommonResult.failed(e.getMessage());
        }
    }

    @ResponseBody
    @PostMapping("/newOfficialFee")
    public CommonResult newOfficialFee(@Valid @RequestBody NewTrademarkOfficialFeeRequest request, BindingResult bindingResult) {
        try {
            return trademarkService.newOfficialFee(request);
        } catch (Exception e) {
            e.printStackTrace();
            return CommonResult.failed(e.getMessage());
        }
    }

    @ResponseBody
    @PostMapping("/getOfficialFeeList")
    public CommonResult getOfficialFee(@Valid @RequestBody GetTrademarkOfficialFeeRequest request, BindingResult bindingResult) {
        try {
            return trademarkService.getOfficialFee(request);
        } catch (Exception e) {
            e.printStackTrace();
            return CommonResult.failed(e.getMessage());
        }
    }

    @ResponseBody
    @DeleteMapping("/deleteOfficialFee")
    public CommonResult deleteOfficialFee(@RequestParam("officialFeeId") Long officialFeeId) {
        try {
            return trademarkService.deleteOfficialFee(officialFeeId);
        } catch (Exception e) {
            e.printStackTrace();
            return CommonResult.failed(e.getMessage());
        }
    }

    @ResponseBody
    @PostMapping("/newFileInfo")
    public CommonResult newFileInfo(@Valid @RequestBody NewTrademarkFileInfoRequest request, BindingResult bindingResult) {
        try {
            return trademarkService.newFileInfo(request);
        } catch (Exception e) {
//            e.printStackTrace();
            return CommonResult.failed(e.getMessage());
        }
    }

    @ResponseBody
    @PostMapping("/getFileList")
    public CommonResult getFileInfo(@Valid @RequestBody GetTrademarkFileInfoRequest request, BindingResult bindingResult) {
        try {
            return trademarkService.getFileInfo(request);
        } catch (Exception e) {
//            e.printStackTrace();
            return CommonResult.failed(e.getMessage());
        }
    }


    @ResponseBody
    @PostMapping("/updateBonus")
    public CommonResult updateBonus(@RequestBody UpdateTrademarkBonusRequest request) {
        try {
            return trademarkService.updateBonus(request);
        } catch (Exception e) {
//            e.printStackTrace();
            return CommonResult.failed(e.getMessage());
        }
    }

}
