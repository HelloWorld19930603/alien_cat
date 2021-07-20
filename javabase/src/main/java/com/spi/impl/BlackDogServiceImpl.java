package com.spi.impl;


import com.spi.services.DogService;


public class BlackDogServiceImpl implements DogService {

    @Override
    public void sleep() {
        System.out.println("hello world");
    }

}
