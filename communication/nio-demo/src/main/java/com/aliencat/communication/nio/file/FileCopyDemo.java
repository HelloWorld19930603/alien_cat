package com.aliencat.communication.nio.file;

import java.io.InputStream;
import java.io.OutputStream;


public class FileCopyDemo {

    /**
     * 演示程序的入口函数
     *
     * @param args
     */
    public static void main(String[] args) {
        //演示复制资源文件
        copyResouceFile();

    }

    /**
     * 复制两个资源目录下的文件
     */
    public static void copyResouceFile() {

    }


    /**
     * 复制文件
     *
     * @param srcPath
     * @param destPath
     */
    public static void blockCopyFile(String srcPath, String destPath) {
        InputStream input = null;
        OutputStream output = null;

    }

}
