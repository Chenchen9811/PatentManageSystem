package com.example.demo.controller;

import com.alibaba.excel.EasyExcel;
import com.example.demo.Utils.PageInfoUtil;
import com.example.demo.common.CommonResult;
import com.example.demo.entity.PatentExport;
import com.example.demo.request.*;
import com.example.demo.response.GetTrademarkResponse;
import com.example.demo.service.TrademarkService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.parameters.P;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

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
            List<GetTrademarkResponse> responseList = trademarkService.getTrademark(request);
            if (responseList == null || responseList.size() == 0) {
                return CommonResult.failed("查找失败");
            }
            return CommonResult.success(PageInfoUtil.getPageInfo(responseList, request.getPageIndex(), request.getPageSize()));
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
    @PostMapping("/updateOfficialFee")
    public CommonResult updateOfficialFee(@RequestBody UpdateTrademarkOfficialFeeRequest request) {
        try {
            return trademarkService.updateOfficialFee(request);
        } catch (Exception e) {
//            e.printStackTrace();
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
    @DeleteMapping("/deleteOfficialFee/{id}")
    public CommonResult deleteOfficialFee(@PathVariable("id") String id) {
        try {
            return trademarkService.deleteOfficialFee(id);
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

    @ResponseBody
    @DeleteMapping("/deleteFile/{id}")
    public CommonResult deleteFile(@PathVariable("id") String fileId) {
        try {
            return trademarkService.deleteFile(fileId);
        } catch (Exception e) {
//            e.printStackTrace();
            return CommonResult.failed(e.getMessage());
        }
    }


    @ResponseBody
    @PostMapping("/export")
    public CommonResult export(@RequestBody GetTrademarkRequest request, HttpServletResponse response) throws Exception {
        try {
            List<GetTrademarkResponse> responseList = trademarkService.getTrademark(request);
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-disposition", "attachment;filename=软著信息.xlsx");
            EasyExcel.write(response.getOutputStream(), GetTrademarkResponse.class).sheet("软著信息").doWrite(responseList);
            return CommonResult.success(null, "导出成功");
        } catch (Exception e) {
            e.printStackTrace();
            return CommonResult.failed(e.getMessage());
        }
    }
}
