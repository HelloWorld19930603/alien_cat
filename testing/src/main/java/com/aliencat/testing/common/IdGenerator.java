package com.aliencat.testing.common;

import org.springframework.stereotype.Component;

@Component
public class IdGenerator {

    private static volatile int i = 1;

    public long next(){
        return i++;
    }

}
