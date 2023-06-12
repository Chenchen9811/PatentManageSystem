package com.example.demo.controller;

import com.alibaba.excel.EasyExcel;
import com.example.demo.Utils.PageInfoUtil;
import com.example.demo.common.CommonResult;
import com.example.demo.request.*;
import com.example.demo.response.GetSoftwareResponse;
import com.example.demo.response.GetTrademarkResponse;
import com.example.demo.service.SoftwareService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/software")
@Slf4j
@CrossOrigin(origins = "*")
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
    @PostMapping("/getList")
    public CommonResult getSoftware(@Valid @RequestBody GetSoftwareRequest request, BindingResult bindingResult) {
        CommonResult result = null;
        try {
            List<GetSoftwareResponse> responseList = softwareService.getSoftware(request);
            return CommonResult.success(PageInfoUtil.getPageInfo(responseList, request.getPageIndex(), request.getPageSize()));
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
    @PostMapping("/getOfficialFeeList")
    public CommonResult getOfficialFee(@Valid @RequestBody GetSoftwareOfficialFeeRequest request, BindingResult bindingResult) {
        try {
            return softwareService.getOfficialFee(request);
        } catch (Exception e) {
            e.printStackTrace();
            return CommonResult.failed(e.getMessage());
        }
    }

    @ResponseBody
    @DeleteMapping("/deleteOfficialFee/{id}")
    public CommonResult deleteOfficialFee(@PathVariable("id") String id) {
        try {
            return softwareService.deleteOfficialFee(id);
        } catch (Exception e) {
            e.printStackTrace();
            return CommonResult.failed(e.getMessage());
        }
    }

    @ResponseBody
    @PostMapping("/updateOfficialFee")
    public CommonResult updateOfficialFee(@RequestBody UpdateSoftwareOfficialFeeRequest request) {
        try {
            return softwareService.updateOfficialFee(request);
        } catch (Exception e) {
//            e.printStackTrace();
            return CommonResult.failed(e.getMessage());
        }
    }


    @ResponseBody
    @PostMapping("/newFileInfo")
    public CommonResult newFileInfo(@Valid @RequestBody NewSoftwareFileInfoRequest request, BindingResult bindingResult) {
        try {
            return softwareService.newFileInfo(request);
        } catch (Exception e) {
            e.printStackTrace();
            return CommonResult.failed(e.getMessage());
        }
    }

    @ResponseBody
    @PostMapping("/getFileList")
    public CommonResult getFileInfo(@Valid @RequestBody GetSoftwareFileInfoRequest request, BindingResult bindingResult) {
        try {
            return softwareService.getFileInfo(request);
        } catch (Exception e) {
            e.printStackTrace();
            return CommonResult.failed(e.getMessage());
        }
    }

    @ResponseBody
    @PostMapping("/getSoftwareBonusList")
    public CommonResult getList(@Valid @RequestBody GetSoftwareBonusRequest request, BindingResult bindingResult) {
        try {
            return softwareService.getList(request);
        } catch (Exception e) {
//            e.printStackTrace();
            return CommonResult.failed(e.getMessage());
        }
    }

    @ResponseBody
    @PostMapping("/newBonus")
    public CommonResult newBonus(@RequestBody NewSoftwareBonusRequest request, BindingResult bindingResult) {
        try {
            return softwareService.newBonus(request);
        } catch (Exception e) {
//            e.printStackTrace();
            return CommonResult.failed(e.getMessage());
        }
    }

    @ResponseBody
    @DeleteMapping("/deleteBonus/{id}")
    public CommonResult deleteBonus(@PathVariable("id") String id) {
        try {
            return softwareService.deleteBonus(id);
        } catch (Exception e) {
//            e.printStackTrace();
            return CommonResult.failed(e.getMessage());
        }
    }

    @ResponseBody
    @PostMapping("/updateBonus")
    public CommonResult updateBonus(@RequestBody UpdateSoftwareBonusRequest request) {
        try {
            return softwareService.updateBonus(request);
        } catch (Exception e) {
//            e.printStackTrace();
            return CommonResult.failed(e.getMessage());
        }
    }

    @ResponseBody
    @DeleteMapping("/deleteFile/{id}")
    public CommonResult deleteFile(@PathVariable("id") String fileId) {
        try {
            return softwareService.deleteFile(fileId);
        } catch (Exception e) {
//            e.printStackTrace();
            return CommonResult.failed(e.getMessage());
        }
    }

    @ResponseBody
    @PostMapping("/export")
    public CommonResult export(@RequestBody GetSoftwareRequest request, HttpServletResponse response) {
        try {
            List<GetSoftwareResponse> responseList = softwareService.getSoftware(request);
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-disposition", "attachment;filename=商标信息.xlsx");
            EasyExcel.write(response.getOutputStream(), GetSoftwareResponse.class).sheet("商标信息").doWrite(responseList);
            return CommonResult.success(null, "导出成功");
        } catch (Exception e) {
            e.printStackTrace();
            return CommonResult.failed(e.getMessage());
        }
    }
}
