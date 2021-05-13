package com.aliencat.testing.pojo;

public class File {

    String fileName;

    public File(){

    }

    public File(String fileName){
        this.fileName = fileName;
    }

    public boolean isClosed() {

        return false;
    }

    public boolean isFile() {
        return false;
    }
}
