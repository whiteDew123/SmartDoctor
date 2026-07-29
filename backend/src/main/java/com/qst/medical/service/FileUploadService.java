package com.qst.medical.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.UUID;

@Service
public class FileUploadService {

    private static final String UPLOAD_PATH = "C:/Users/何少升/Pictures/medical/";

    public String upload(MultipartFile file) {
        try {
            InputStream inputStream = file.getInputStream();
            String realName = file.getOriginalFilename();
            String extension = realName.substring(realName.lastIndexOf("."));
            String fileName = UUID.randomUUID().toString().replace("-", "") + extension;

            File pathDir = new File(UPLOAD_PATH);
            if (!pathDir.exists()) {
                pathDir.mkdirs();
            }

            File tmpFile = new File(pathDir.getPath() + File.separator + fileName);
            try (FileOutputStream os = new FileOutputStream(tmpFile)) {
                byte[] buffer = new byte[1024];
                int len;
                while ((len = inputStream.read(buffer)) != -1) {
                    os.write(buffer, 0, len);
                }
            }

            String url = "http://localhost:8080/image/" + fileName;
            return url;
        } catch (Exception e) {
            throw new RuntimeException("文件上传失败：" + e.getMessage());
        }
    }
}
