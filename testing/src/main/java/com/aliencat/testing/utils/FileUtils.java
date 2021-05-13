package com.aliencat.testing.utils;


import com.aliencat.testing.pojo.File;

public final class FileUtils {
    public static boolean isFile(String fileName) {
        return new File(fileName).isFile();
    }

    public static boolean isClosed() {
        return new File().isClosed();
    }
}
