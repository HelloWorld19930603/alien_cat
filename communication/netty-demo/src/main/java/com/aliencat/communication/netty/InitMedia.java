package com.aliencat.communication.netty;

import com.aliencat.communication.netty.annotation.Action;
import com.aliencat.communication.netty.param.BeanMethod;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import java.lang.reflect.Method;
import java.util.Map;


@Component
public class InitMedia implements ApplicationListener<ContextRefreshedEvent>, Ordered {

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

        //查找所有bean当中，包含Controller主机的bean
        Map<String, Object> beans = event.getApplicationContext().getBeansWithAnnotation(Controller.class);

        for (String key : beans.keySet()) {
            Object bean = beans.get(key);
            Method[] methods = bean.getClass().getDeclaredMethods();
            for (Method m : methods) {
                //假如方法上面存在Action注解
                if (m.isAnnotationPresent(Action.class)) {
                    Action action = m.getAnnotation(Action.class);
                    String command = action.value();
                    BeanMethod beanMethod = new BeanMethod();
                    beanMethod.setBean(bean);
                    beanMethod.setMethod(m);
                    Media.beanMap.put(command, beanMethod);
                }
            }

        }


    }

}
