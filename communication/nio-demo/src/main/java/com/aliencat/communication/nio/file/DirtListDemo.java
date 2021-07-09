package com.aliencat.communication.nio.file;

import java.io.File;
import java.io.IOException;

public class DirtListDemo {

    public static void listFile(String path) throws IOException {

        // 需要进行判空的校验
        // 如果path为空，后边的构造File对象会报异常
        if (path == null) {
            return;
        }

        File pFile = new File(path);


    }


}
