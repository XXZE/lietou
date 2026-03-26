package com.admin.crm.controller;

import com.admin.common.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/file")
public class FileController {

    @Value("${file.upload.path:uploads}")
    private String uploadPath;

    @Value("${file.upload.url-prefix:/files}")
    private String urlPrefix;

    @PostMapping("/upload")
    public Result<Map<String, String>> upload(@RequestParam("file") MultipartFile file) {
        System.out.println("=== 文件上传开始 ===");
        System.out.println("文件名: " + file.getOriginalFilename());
        System.out.println("文件大小: " + file.getSize());
        System.out.println("文件类型: " + file.getContentType());

        if (file.isEmpty()) {
            return Result.error("文件不能为空");
        }

        // 检查文件大小 (5MB)
        long maxSize = 5 * 1024 * 1024;
        if (file.getSize() > maxSize) {
            return Result.error("文件大小不能超过5MB");
        }

        // 检查文件类型
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            return Result.error("文件名不能为空");
        }

        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
        System.out.println("文件扩展名: " + fileExtension);
        if (!fileExtension.equals(".pdf") && !fileExtension.equals(".doc") && !fileExtension.equals(".docx")) {
            return Result.error("只支持PDF、Word格式的文件");
        }

        try {
            // 创建上传目录
            String datePath = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
            String relativePath = uploadPath + "/" + datePath;
            File uploadDir = new File(relativePath);
            System.out.println("上传目录: " + uploadDir.getAbsolutePath());

            // 确保使用绝对路径
            if (!uploadDir.isAbsolute()) {
                uploadDir = new File(System.getProperty("user.dir"), relativePath);
                System.out.println("使用绝对路径: " + uploadDir.getAbsolutePath());
            }

            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
                System.out.println("创建目录成功");
            }

            // 生成唯一文件名
            String uuid = UUID.randomUUID().toString().replace("-", "");
            String newFilename = uuid + fileExtension;
            File destFile = new File(uploadDir, newFilename);
            System.out.println("目标文件: " + destFile.getAbsolutePath());

            // 保存文件
            file.transferTo(destFile);
            System.out.println("文件保存成功");

            // 构建访问URL
            String fileUrl = urlPrefix + "/" + datePath + "/" + newFilename;
            System.out.println("文件URL: " + fileUrl);

            Map<String, String> data = new HashMap<>();
            data.put("url", fileUrl);
            data.put("filename", newFilename);
            data.put("originalFilename", originalFilename);
            data.put("size", String.valueOf(file.getSize()));

            return Result.success("上传成功", data);

        } catch (IOException e) {
            e.printStackTrace();
            return Result.error("文件上传失败: " + e.getMessage());
        }
    }
}
