package com.aliencat.communication.example.exception;

public class ThrowException {

    public static void main(String[] args) throws Exception {
        System.out.println("start");
        test();
        System.out.println("end");
    }

    public static void test() throws Exception {

        System.out.println("test in");
        throw new Exception("test exception...");

    }
}
