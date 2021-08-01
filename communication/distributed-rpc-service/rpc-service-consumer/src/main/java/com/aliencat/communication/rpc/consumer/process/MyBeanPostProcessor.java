package com.aliencat.communication.rpc.consumer.process;

import com.aliencat.communication.rpc.consumer.proxy.RpcClientProxy;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

@Component
public class MyBeanPostProcessor implements BeanPostProcessor {

    @Autowired
    private RpcClientProxy rpcClientProxy;

    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClazz = bean.getClass();
        for (Field field : beanClazz.getDeclaredFields()) {
            if (field.getAnnotation(RpcReference.class) == null) {
                continue;
            }
            Object proxy = rpcClientProxy.createProxy(field.getType());
            try {
                field.setAccessible(true);
                field.set(bean, proxy);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return bean;
    }

}
