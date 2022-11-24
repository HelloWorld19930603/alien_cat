package com.aliencat.springboot.component.controller;

import com.aliencat.springboot.component.base.code.ResultCode;
import com.aliencat.springboot.component.base.response.Result;
import com.aliencat.springboot.component.config.ConfigData;
import com.aliencat.springboot.component.util.ZipExpUtil;
import com.sun.istack.internal.NotNull;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author chengcheng
 * @Date 2022-11-24
 **/
@RestController
@RequestMapping("/file")
public class FileToolController {

    @Autowired
    ConfigData configData;

    @PostMapping("/uploadFile")
    public Result userHelp(@NotNull MultipartFile file) {

        String originalFilename = file.getOriginalFilename();//原始名称
        if (StringUtils.isBlank(originalFilename)) {
            return Result.fail(ResultCode.FILE_ERROR);
        }
        //返回结果
        Map<String, Object> pathMap = new HashMap<>();
        InputStream inputStream = null;//文件流
        try {
            inputStream = file.getInputStream();
            //检测创建文件夹
            Path directory = Paths.get(configData.getUploadFilePath());
            if (!Files.exists(directory)) {
                Files.createDirectories(directory);
            }
            Long size = Files.copy(inputStream, directory.resolve(originalFilename), StandardCopyOption.REPLACE_EXISTING);//上传文件，返回值是文件大小
            pathMap.put("zip_size", size);
        } catch (Exception e) {
            pathMap.put("zip_size", 0);
            pathMap.put("error", e.getMessage());
            return Result.fail(pathMap);
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {

            }
        }
        String filePath = configData.getUploadFilePath() + File.separator + originalFilename;
        if (originalFilename.endsWith(".zip")) {
            ZipExpUtil.zipUncompress(filePath, false);
        }
        pathMap.put("folder_path", filePath);
        pathMap.put("zip_size_cent", "kb");
        return Result.success(pathMap);
    }

    @RequestMapping("/downloadFile")
    public void userHelp(@RequestParam String fileName, HttpServletResponse response) {
        response.setContentType("application/pdf;charset=UTF-8");
        File file = new File(configData.getUploadFilePath() + File.separator + fileName);
        byte[] data;
        FileInputStream input = null;
        try {
            input = new FileInputStream(file);
            data = new byte[input.available()];
            input.read(data);
            response.getOutputStream().write(data);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (input != null) {
                    input.close();
                }
            } catch (IOException e) {

            }
        }
    }

}
