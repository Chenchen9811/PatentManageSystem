package com.example.demo.controller;

import com.alibaba.excel.EasyExcel;
import com.example.demo.Utils.PageInfoUtil;
import com.example.demo.common.CommonResult;
import com.example.demo.entity.PatentExport;
import com.example.demo.request.*;
import com.example.demo.response.GetPatentResponse;
import com.example.demo.service.PatentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/patent")
@Slf4j
@CrossOrigin(origins = "*")
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
    @PostMapping("/getList")
    public CommonResult getPatent(@RequestBody GetPatentRequest request) {
        CommonResult result = null;
        try {
            List<GetPatentResponse> responseList = patentService.getPatent(request);
            if (responseList == null || responseList.size() == 0) {
                return CommonResult.failed("查找失败");
            }
            return CommonResult.success(PageInfoUtil.getPageInfo(responseList, request.getPageIndex(), request.getPageSize()), "查找成功");
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
    @PostMapping("/getOfficialFeeList")
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
    @DeleteMapping("/deleteOfficialFee/{id}")
    public CommonResult deleteOfficialFee(@PathVariable("id") String id) {
        CommonResult result = null;
        try {
            result = patentService.deleteOfficialFee(id);
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
    @PostMapping("/getAnnualFee")
    public CommonResult getAnnualFee(@RequestBody GetPatentAnnualFeeRequest request) {
        CommonResult result = null;
        try {
            result = patentService.getAnnualFee(request);
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
    @PostMapping("/getBonusList")
    public CommonResult getBonus(@Valid @RequestBody GetPatentBonusListRequest request, BindingResult bindingResult) {
        CommonResult result = null;
        try {
            result = patentService.getBonus(request);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return CommonResult.failed(e.getMessage());
        }
    }

    @ResponseBody
    @DeleteMapping("/deleteBonus/{id}")
    public CommonResult deleteBonus(@PathVariable("id") String id) {
        CommonResult result = null;
        try {
            result = patentService.deleteBonus(id);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return CommonResult.failed(e.getMessage());
        }
    }

    @ResponseBody
    @PostMapping("/updateBonus")
    public CommonResult updateBonus(@Valid @RequestBody UpdatePatentBonusRequest request, BindingResult bindingResult) {
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
    @PostMapping("/getFileInfo")
    public CommonResult getFileInfo(@Valid @RequestBody GetPatentFileInfoRequest request, BindingResult bindingResult) {
        try {
            return patentService.getFileInfo(request);
        } catch (Exception e) {
            e.printStackTrace();
            return CommonResult.failed(e.getMessage());
        }
    }

    @ResponseBody
    @DeleteMapping("/deleteFile/{id}")
    public CommonResult deleteFile(@PathVariable("id") String fileId) {
        try {
            return patentService.deleteFile(fileId);
        } catch (Exception e) {
//            e.printStackTrace();
            return CommonResult.failed(e.getMessage());
        }
    }

    @ResponseBody
    @PostMapping("/export")
    public CommonResult exportPatent(@RequestBody GetPatentRequest request, HttpServletResponse response) {
        try {
            List<GetPatentResponse> responseList = patentService.getPatent(request);
            List<PatentExport> exportList = responseList.stream().map(resopnse -> {
                PatentExport patentExport = new PatentExport();
                patentExport.setPatentCode(resopnse.getPatentCode());
                patentExport.setPatentName(resopnse.getPatentName());
                patentExport.setPatentType(resopnse.getPatentType());
                patentExport.setApplicationCode(resopnse.getApplicationCode());
                patentExport.setApplicationDate(resopnse.getApplicationDate());
                List<String> inventorNameList = resopnse.getInventorNameList();
                if (inventorNameList.size() > 1) {
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < inventorNameList.size(); i++) {
                        sb.append(inventorNameList.get(i) + ",");
                    }
                    sb.deleteCharAt(sb.length() - 1);
                    patentExport.setInventorNames(sb.toString());
                } else {
                    patentExport.setInventorNames(inventorNameList.get(0));
                }
                patentExport.setCurrentProgram(resopnse.getCurrentProgram());
                patentExport.setGrantCode(resopnse.getGrantCode());
                patentExport.setGrantDate(resopnse.getGrantDate());
                patentExport.setRightStatus(resopnse.getRightStatus());
                return patentExport;
            }).collect(Collectors.toList());
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-disposition", "attachment;filename=专利信息.xlsx");
            EasyExcel.write(response.getOutputStream(), PatentExport.class).sheet("成员列表").doWrite(exportList);
            return CommonResult.success(null, "导出成功");
        } catch (Exception e) {
            e.printStackTrace();
            return CommonResult.failed(e.getMessage());
        }

    }


    @ResponseBody
    @GetMapping("/setting")
    public CommonResult setting(@RequestParam("level1") Integer level1,
                                @RequestParam("level2") Integer level2,
                                @RequestParam("level3") Integer level3) {
        try {
            return patentService.setting(level1, level2, level3);
        } catch (Exception e) {
//            e.printStackTrace();
            return CommonResult.failed(e.getMessage());
        }
    }

    @ResponseBody
    @GetMapping("/getWarning")
    public CommonResult getWarning(@RequestParam("pageIndex") Integer pageIndex,
                                   @RequestParam("pageSize") Integer pageSize) {
        try {
            return patentService.getWarning(pageIndex, pageSize);
        } catch (Exception e) {
//            e.printStackTrace();
            return CommonResult.failed(e.getMessage());
        }
    }

}
