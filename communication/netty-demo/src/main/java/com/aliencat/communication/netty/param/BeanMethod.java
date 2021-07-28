package com.aliencat.communication.netty.param;

import lombok.Data;

import java.lang.reflect.Method;

@Data
public class BeanMethod {

    private Object bean;
    private Method method;

}
