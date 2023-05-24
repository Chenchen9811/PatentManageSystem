package com.example.demo.controller;

import com.example.demo.common.CommonResult;
import com.example.demo.request.*;
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

    @ResponseBody
    @PostMapping("/newOfficialFee")
    public CommonResult newOfficialFee(@Valid @RequestBody NewPatentOfficialFeeRequest request, BindingResult bindingResult) {
        CommonResult result = null;
        try {
            result = patentService.newOfficialFee(request);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return CommonResult.failed(e.getMessage());
        }
    }

    @ResponseBody
    @GetMapping("/getOfficialFee")
    public CommonResult getOfficialFee(@RequestBody GetPatentOfficialFeeRequest request) {
        CommonResult result = null;
        try {
            result = patentService.getOfficialFee(request);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return CommonResult.failed(e.getMessage());
        }
    }


    @ResponseBody
    @PostMapping("/updateOfficialFee")
    public CommonResult updateOfficialFee(@Valid @RequestBody UpdatePatentOfficialFeeRequest request, BindingResult bindingResult) {
        CommonResult result = null;
        try {
            result = patentService.updateOfficialFee(request);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return CommonResult.failed(e.getMessage());
        }
    }

    @ResponseBody
    @DeleteMapping("/deleteOfficialFee")
    public CommonResult deleteOfficialFee(@RequestParam("officialFeeName") String officialFeeName) {
        CommonResult result = null;
        try {
            result = patentService.deleteOfficialFee(officialFeeName);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return CommonResult.failed(e.getMessage());
        }
    }

    @ResponseBody
    @PostMapping("/newAnnualFee")
    public CommonResult newAnnualFee(@Valid @RequestBody NewPatenAnnualFeeRequest request, BindingResult bindingResult) {
        CommonResult result = null;
        try {
            result = patentService.newAnnualFee(request);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return CommonResult.failed(e.getMessage());
        }
    }

    @ResponseBody
    @GetMapping("/getAnnualFee")
    public CommonResult getAnnualFee(@RequestParam("patentName") String patentName,
                                     @RequestParam("pageIndex") Integer pageIndex,
                                     @RequestParam("pageSize") Integer pageSize) {
        CommonResult result = null;
        try {
            result = patentService.getAnnualFee(patentName, pageIndex, pageSize);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return CommonResult.failed(e.getMessage());
        }
    }

    @ResponseBody
    @GetMapping("/getAnnualFeeCode")
    public CommonResult getAnnualFeeCode(@RequestParam("patentCode") String patentCode,
                                         @RequestParam("year") String year) {
        CommonResult result = null;
        try {
            result = patentService.getAnnualFeeCode(patentCode, year);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return CommonResult.failed(e.getMessage());
        }
    }

    @ResponseBody
    @PostMapping("/updateAnnualFee")
    public CommonResult updateAnnualFee(@Valid @RequestBody UpdateAnnualFeeRequest request, BindingResult bindingResult) {
        CommonResult result = null;
        try {
            result = patentService.updateAnnualFee(request);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return CommonResult.failed(e.getMessage());
        }
    }


    @ResponseBody
    @DeleteMapping("/deleteAnnualFee")
    public CommonResult deleteAnnualFee(@RequestParam("annualFeeCode") String annualFeeCode) {
        CommonResult result = null;
        try {
            result = patentService.deleteAnnualFee(annualFeeCode);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return CommonResult.failed(e.getMessage());
        }
    }


    @ResponseBody
    @PostMapping("/newBonus")
    public CommonResult newBonus(@Valid @RequestBody NewPatentBonusRequest request, BindingResult bindingResult) {
        CommonResult result = null;
        try {
            result = patentService.newBonus(request);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return CommonResult.failed(e.getMessage());
        }
    }

    @ResponseBody
    @GetMapping("/getBonus")
    public CommonResult getBonus(@RequestParam("patentCode") String patentCode,
                                 @RequestParam("pageIndex") Integer pageIndex,
                                 @RequestParam("pageSize") Integer pageSize) {
        CommonResult result = null;
        try {
            result = patentService.getBonus(patentCode, pageIndex, pageSize);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return CommonResult.failed(e.getMessage());
        }
    }

    @ResponseBody
    @DeleteMapping("/deleteBonus")
    public CommonResult deleteBonus(@RequestParam("patentCode") String patentCode,
                                    @RequestParam("inventorName") String inventorName) {
        CommonResult result = null;
        try {
            result = patentService.deleteBonus(patentCode, inventorName);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return CommonResult.failed(e.getMessage());
        }
    }

    @ResponseBody
    @PostMapping("/updateBonus")
    public CommonResult updateBonus(@Valid @RequestBody NewPatentBonusRequest request, BindingResult bindingResult) {
        CommonResult result = null;
        try {
            result = patentService.updateBonus(request);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return CommonResult.failed(e.getMessage());
        }
    }

    @ResponseBody
    @PostMapping("/newFileInfo")
    public CommonResult newFileInfo(@Valid @RequestBody NewPatentFileInfoRequest request, BindingResult bindingResult) {
        try {
            return patentService.newFileInfo(request);
        } catch (Exception e) {
            e.printStackTrace();
            return CommonResult.failed(e.getMessage());
        }
    }

    @ResponseBody
    @GetMapping("/getFileInfo")
    public CommonResult getFileInfo(@Valid @RequestBody GetPatentFileInfoRequest request, BindingResult bindingResult) {
        try {
            return patentService.getFileInfo(request);
        } catch (Exception e) {
            e.printStackTrace();
            return CommonResult.failed(e.getMessage());
        }
    }
}
