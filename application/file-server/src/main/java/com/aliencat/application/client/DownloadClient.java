package com.aliencat.application.client;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
public class DownloadClient {

    private final static long PER_PAGE = 1024l * 1024l * 50l;
    private final static String DOWNPATH = "F:\\fileItem";
    ExecutorService pool = Executors.newFixedThreadPool(10);

    @RequestMapping("/downloadFile")
    public String downloadFile() throws Exception {
        FileInfo fileInfo = download(0, 10, -1, null);
        //总分片数量
        long pages = fileInfo.fSize / PER_PAGE;
        for (long i = 0; i <= pages; i++) {
            pool.submit(new Download(i * PER_PAGE, (i + 1) * PER_PAGE - 1, i, fileInfo.fName));
        }


        return "success";
    }

    private FileInfo download(long start, long end, long page, String fName) throws Exception {
        File file = new File(DOWNPATH, page + "-" + fName);
        if (file.exists()) {
            return null;
        }
        HttpClient client = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet("http://127.0.0.1:8080/download");
        httpGet.setHeader("Range", "bytes=" + start + "-" + end);

        HttpResponse response = client.execute(httpGet);
        HttpEntity entity = response.getEntity();
        InputStream is = entity.getContent();

        String fSize = response.getFirstHeader("fSize").getValue();
        fName = URLDecoder.decode(response.getFirstHeader("fName").getValue(), "utf-8");

        FileOutputStream fis = new FileOutputStream(file);
        byte[] buffer = new byte[1024];
        int ch = 0;
        while ((ch = is.read(buffer)) != -1) {
            fis.write(buffer, 0, ch);
        }
        is.close();
        fis.flush();
        fis.close();

        if (end - Long.valueOf(fSize) >= 0) {//最后一个分片
            mergeFile(fName, page);
        }
        return new FileInfo(Long.valueOf(fSize), fName);
    }

    private void mergeFile(String fName, long page) throws Exception {
        File tempFile = new File(DOWNPATH, fName);
        BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(tempFile));

        for (int i = 0; i <= page; i++) {
            File file = new File(DOWNPATH, i + "-" + fName);
            while (!file.exists() || (i != page && file.length() < PER_PAGE)) {
                Thread.sleep(100);
            }
            byte[] bytes = FileUtils.readFileToByteArray(file);
            os.write(bytes);
            os.flush();
            file.delete();
        }
        File file = new File(DOWNPATH, -1 + "-null");
        file.delete();
        os.flush();
        os.close();
        //文件子节计算导致文件不完整
        //流未关闭
    }

    class FileInfo {
        long fSize;
        String fName;

        public FileInfo(long fSize, String fName) {
            this.fSize = fSize;
            this.fName = fName;
        }
    }

    class Download implements Runnable {
        long start;
        long end;
        long page;
        String fName;

        public Download(long start, long end, long page, String fName) {
            this.start = start;
            this.end = end;
            this.page = page;
            this.fName = fName;
        }

        public void run() {
            try {
                FileInfo info = download(start, end, page, fName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
