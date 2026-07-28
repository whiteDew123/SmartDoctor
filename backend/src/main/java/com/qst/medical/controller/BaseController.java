package com.qst.medical.controller;

import com.qst.medical.common.UploadResult;
import com.qst.medical.service.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/base")
public class BaseController {

    @Autowired
    private FileUploadService fileUploadService;

    @PostMapping("/upload")
    public UploadResult upload(@RequestParam("file") MultipartFile file) {
        try {
            if (file == null || file.isEmpty()) {
                return UploadResult.error("上传文件不能为空");
            }
            String url = fileUploadService.upload(file);
            return UploadResult.success(url);
        } catch (Exception e) {
            return UploadResult.error("上传失败：" + e.getMessage());
        }
    }
}
