package com.aliencat.nio.file;

import org.junit.Test;

import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;

/**
 * Created by 尼恩@ 疯创客圈
 */
public class FileNIOFastCopyDemo {

    public static void main(String[] args) {
        //演示复制资源文件
        fastCopyResouceFile();
    }

    /**
     * 复制两个资源目录下的文件
     */
    public static void fastCopyResouceFile() {
        String srcDecodePath = getSourceFile();

        String destDecodePath = getDestFile();

        fastCopyFile(srcDecodePath, destDecodePath);


    }

    private static String getDestFile() {
        String destDecodePath = null;
        return destDecodePath;
    }

    private static String getSourceFile() {

        return null;
    }

    /**
     * 复制文件
     *
     * @param srcPath
     * @param destPath
     */
    public static void fastCopyFile(String srcPath, String destPath) {

    }

    @Test
    public void transferFrom() throws Exception {
        try (FileChannel fromChannel = new RandomAccessFile(
                getSourceFile(), "rw").getChannel();
             FileChannel toChannel = new RandomAccessFile(
                     getDestFile(), "rw").getChannel()) {
            long position = 0L;
            long offset = fromChannel.size();
            toChannel.transferFrom(fromChannel, position, offset);
        }
    }

    @Test
    public void transferTo() throws Exception {
        try (FileChannel fromChannel = new RandomAccessFile(
                getSourceFile(), "rw").getChannel();
             FileChannel toChannel = new RandomAccessFile(
                     getDestFile(), "rw").getChannel()) {
            long position = 0L;
            long offset = fromChannel.size();
            fromChannel.transferTo(position, offset, toChannel);
        }
    }

}
