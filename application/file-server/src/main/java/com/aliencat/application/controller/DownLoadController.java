package com.aliencat.application.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;

@Controller

public class DownLoadController {
    private final static String UTF_8 = "utf-8";
    private final static String DOWNLOAD_PATH = DownLoadController.class.getClassLoader().getResource("").getPath();

    @RequestMapping("/download")
    public void downLoadFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
        File file = new File(DOWNLOAD_PATH, "application.yml");
        response.setCharacterEncoding(UTF_8);
        InputStream is = null;
        OutputStream os = null;
        try {
            //分片下载   http  Range bytes=100-1000   bytes=100-
            long fSize = file.length();
            response.setContentType("application/x-download");
            String fileName = URLEncoder.encode(file.getName(), UTF_8);
            response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
            response.setHeader("Accept-Range", "bytes");

            response.setHeader("fSize", String.valueOf(fSize));
            response.setHeader("fName", fileName);

            long pos = 0, last = fSize - 1, sum = 0;
            if (null != request.getHeader("Range")) {
                response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);

                String numRange = request.getHeader("Range").replaceAll("bytes=", "");
                String[] strRange = numRange.split("-");
                if (strRange.length == 2) {
                    pos = Long.parseLong(strRange[0].trim());
                    last = Long.parseLong(strRange[1].trim());
                    if (last > fSize - 1) {
                        last = fSize - 1;
                    }
                } else {
                    pos = Long.parseLong(numRange.replaceAll("-", "").trim());
                }
            }
            long rangeLenght = last - pos + 1;
            String contentRange = new StringBuffer("bytes ").append(pos).append("-").append(last).append("/").append(fSize).toString();
            response.setHeader("Content-Range", contentRange);
            response.setHeader("Content-Lenght", String.valueOf(rangeLenght));

            os = new BufferedOutputStream(response.getOutputStream());
            is = new BufferedInputStream(new FileInputStream(file));
            is.skip(pos);
            byte[] buffer = new byte[1024];
            int lenght = 0;
            while (sum < rangeLenght) {
                lenght = is.read(buffer, 0, ((rangeLenght - sum) <= buffer.length ? ((int) (rangeLenght - sum)) : buffer.length));
                sum = sum + lenght;
                os.write(buffer, 0, lenght);
            }
            System.out.println("下载完成");
        } finally {
            if (is != null) {
                is.close();
            }
            if (os != null) {
                os.close();
            }
        }
    }
}
