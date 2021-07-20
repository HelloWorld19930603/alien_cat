package com.spi.impl;


import com.spi.services.DogService;

public class WhilteDogServiceImpl implements DogService {

    @Override
    public void sleep() {
        System.out.println("呼呼大睡觉...");

    }

}