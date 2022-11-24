package com.aliencat.springboot.component.util;

import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * Zip工具类
 *
 */
@Slf4j
public class ZipExpUtil {

    /**
     * 将文件压缩到Zip中去
     *
     * @param file      需要压缩的文件
     * @param zos       {@link ZipOutputStream}
     * @param entryName 节点名
     * @param filter    文件过滤条件
     * @throws IOException
     */
    public static void zip(File file, ZipOutputStream zos, String entryName, FileFilter filter)
            throws IOException {
        if (filter.accept(file) && file.isFile()) {
            try (
                    InputStream in = new FileInputStream(file);
                    BufferedInputStream bis = new BufferedInputStream(in)
            ) {
                zos.putNextEntry(new ZipEntry(FileNameUtil.cleanInvalid(entryName)));
                byte[] bytes = new byte[1024];
                int len;
                while ((len = bis.read(bytes)) != -1) {
                    zos.write(bytes, 0, len);
                }
                zos.closeEntry();
            }
        }
    }

    /**
     * 将文件压缩到Zip中去
     *
     * @param bytes     文件内容
     * @param zos       {@link ZipOutputStream}
     * @param entryName 节点名
     * @throws IOException
     */
    public static void zipByte(ZipOutputStream zos, byte[] bytes, String entryName)
            throws IOException {
        zos.putNextEntry(new ZipEntry(FileNameUtil.cleanInvalid(entryName)));
        zos.write(bytes);
        zos.closeEntry();
    }

    /**
     * 基于HuTool 将Excel数据压入Zip
     * @param excelData excel数据
     * @param zos       {@link ZipOutputStream}
     * @param entryName 节点名称
     * @throws IOException
     */
    public static void zipExcel(Iterable<?> excelData, ZipOutputStream zos, String entryName) throws IOException {
        zos.putNextEntry(new ZipEntry(FileNameUtil.cleanInvalid(entryName)));
        ExcelWriter writer = ExcelUtil.getWriter(true);
        writer.write(excelData, true);
        writer.flush(zos);
        writer.close();
        zos.closeEntry();
    }


    /**
     * zip文件解压缩
     *
     * @param inputFile 文件绝对路径
     * @param isDelete   解压后是否删除源文件
     */
    public static void zipUncompress(String inputFile,boolean isDelete) {
        File oriFile = new File(inputFile);
        // 判断源文件是否存在
        String destDirPath = inputFile.replace(".zip", "");
        FileOutputStream fos = null;
        InputStream is = null;
        ZipFile zipFile = null;
        try {
            //创建压缩文件对象
            zipFile = new ZipFile(oriFile);
            //开始解压
            Enumeration<?> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) entries.nextElement();
                // 如果是文件夹，就创建个文件夹
                if (entry.isDirectory()) {
                    oriFile.mkdirs();
                } else {
                    // 如果是文件，就先创建一个文件，然后用io流把内容copy过去
                    File targetFile = new File(destDirPath + "/" + entry.getName());
                    // 保证这个文件的父文件夹必须要存在
                    if (!targetFile.getParentFile().exists()) {
                        targetFile.getParentFile().mkdirs();
                    }
                    targetFile.createNewFile();
                    // 将压缩文件内容写入到这个文件中
                    is = zipFile.getInputStream(entry);
                    fos = new FileOutputStream(targetFile);
                    int len;
                    byte[] buf = new byte[1024];
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                    }
                }
            }
        } catch (Exception e) {
            log.error("文件解压过程中异常,{}", e);
        } finally {
            // 关流顺序，先打开的后关闭
            try {
                if (fos != null) {
                    fos.close();
                }
                if (is != null) {
                    is.close();
                }
                if (zipFile != null) {
                    zipFile.close();
                }
            } catch (IOException e) {
                log.error("文件流关闭异常,{}", e);
            }
        }
        //解压后删除文件
        if (isDelete && oriFile.exists()) {
            oriFile.delete();
            if (oriFile.exists()) {
                oriFile.delete();
                if (oriFile.exists()) {
                    log.error(oriFile.getName() + "文件未被删除");
                }
            }
        }
        log.info(oriFile.getName() + "文件解压完成");
    }

}

