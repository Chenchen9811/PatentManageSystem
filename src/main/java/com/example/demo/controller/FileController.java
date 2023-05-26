package com.example.demo.controller;

import com.example.demo.common.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@RestController
@RequestMapping("/file")
@Slf4j
public class FileController {
    @Value("${file.upload.url}")
    private String uploadFilePath;

    @PostMapping("/upload")
    public CommonResult upload(@RequestParam(value = "file", required = true) MultipartFile file) {
        try {
            // 本地文件保存位置
            String uploadPath = uploadFilePath; // 改这里
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }
            String filePath = uploadPath + File.separator + file.getOriginalFilename();
            log.info("FilePath:{}",filePath);
            // 本地文件
            File localFile = new File(filePath);

            // transfer to local
            file.transferTo(localFile);
            return CommonResult.success(file.getOriginalFilename(),"文件上传成功");
        } catch (IOException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return CommonResult.failed(e.getMessage());
        }
    }

    @GetMapping("/download")
    public void download(HttpServletResponse response, @RequestParam("fileName") String fileName) {
        String filePath = uploadFilePath + File.separator + fileName;
        File file = new File(filePath);
        response.reset();
        response.setContentType("application/octet-stream");
        response.setCharacterEncoding("utf-8");
        response.setContentLength((int) file.length());
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName );

        try (FileInputStream inputStream = new FileInputStream(file);) { // try-with-resources
            byte[] b = new byte[1024];
            int len;
            while ((len = inputStream.read(b)) > 0) {
                response.getOutputStream().write(b, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }

    }
}
