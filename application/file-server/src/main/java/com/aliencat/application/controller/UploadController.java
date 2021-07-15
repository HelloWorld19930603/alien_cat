package com.aliencat.application.controller;


import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@Controller
public class UploadController {

    private final static String UTF_8 = "utf-8";
    private final static String UPLOAD_PATH;

    static {
        UPLOAD_PATH = UploadController.class.getClassLoader().getResource("").getPath() + "static/file";
        File dic = new File(UPLOAD_PATH);
        if (!dic.exists()) {
            dic.mkdir();
        }
    }

    @RequestMapping("/upload")
    @ResponseBody
    public void upload(HttpServletRequest request, HttpServletResponse response) throws Exception {

        //分片
        response.setCharacterEncoding(UTF_8);
        Integer schunk = null;
        Integer schunks = null;
        String name = null;

        DiskFileItemFactory factory = new DiskFileItemFactory();
        factory.setSizeThreshold(1024);
        factory.setRepository(new File(UPLOAD_PATH));
        ServletFileUpload upload = new ServletFileUpload(factory);
        upload.setFileSizeMax(5l * 1024l * 1024l * 1024l);
        upload.setSizeMax(10l * 1024l * 1024l * 1024l);

        List<FileItem> items = null;
        try {
            items = upload.parseRequest(request);
        } catch (FileUploadException e) {
            e.printStackTrace();
        }

        for (FileItem item : items) {
            if (item.isFormField()) {
                if ("chunk".equals(item.getFieldName())) {
                    schunk = Integer.parseInt(item.getString(UTF_8));
                }
                if ("chunks".equals(item.getFieldName())) {
                    schunks = Integer.parseInt(item.getString(UTF_8));
                }
                if ("name".equals(item.getFieldName())) {
                    name = item.getString(UTF_8);
                }
            }
        }
        for (FileItem item : items) {
            if (!item.isFormField()) {
                String temFileName = name;
                if (name != null) {
                    if (schunk != null) {
                        temFileName = schunk + "_" + name;
                    }
                    File temFile = new File(UPLOAD_PATH, temFileName);
                    if (!temFile.exists()) {//断点续传
                        item.write(temFile);
                    }
                }
            }
        }
        //文件合并
        if (schunk != null && schunk.intValue() == schunks.intValue() - 1) {
            File tempFile = new File(UPLOAD_PATH, name);
            try (BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(tempFile))) {
                for (int i = 0; i < schunks; i++) {
                    File file = new File(UPLOAD_PATH, i + "_" + name);
                    while (!file.exists()) {
                        Thread.sleep(100);
                    }
                    byte[] bytes = FileUtils.readFileToByteArray(file);
                    os.write(bytes);
                    os.flush();
                    file.delete();
                }
                os.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        response.getWriter().write("上传成功" + name);

    }
}
