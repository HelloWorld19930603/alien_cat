package com.aliencat.javabase.jni;

public class HelloJni {

    //javah -classpath . -jni com.aliencat.javabase.jni.HelloJni
    public native void sayHello(String name);
}
