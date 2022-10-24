package com.aliencat.springboot.elasticsearch.utils;

import lombok.Cleanup;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {

    //逐行读取文件内容返回内容列表
    public static List<String> readLine(String path) {
        List<String> list = new ArrayList<>();
        try {
            //@Cleanup lombok用法 自动关闭IO
            @Cleanup BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
            String line = "";
            while ((line = reader.readLine()) != null) {
                if (line.trim().length() > 0) {
                    list.add(line);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }


    //读取文件内容并返回
    public static String readToString(String fileName) {
        String encoding = "UTF-8";
        //new File对象
        File file = new File(fileName);
        //获取文件长度
        Long filelength = file.length();
        //获取同长度的字节数组
        byte[] filecontent = new byte[filelength.intValue()];
        FileInputStream in = null;
        try {
            in = new FileInputStream(file);
            in.read(filecontent);
            return new String(filecontent, encoding);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    //向文件里写入内容
    public static void saveAsFileWriter(String path, String content) {
        saveAsFileWriter(path, content, false);
    }

    public static void saveAsFileWriter(String path, String content, boolean isAppend) {
        File file = new File(path);
        //如果目录不存在 创建目录
        try {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        FileWriter fileWriter = null;
        try {
            // true表示不覆盖原来的内容，而是加到文件的后面。若要覆盖原来的内容，直接省略这个参数就好
            fileWriter = new FileWriter(path, isAppend);
            fileWriter.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}