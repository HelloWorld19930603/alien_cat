package com.aliencat.springboot.aop.aspects;

import com.aliencat.springboot.aop.annotation.BusinessLogAnnotation;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Component
@Aspect
@Order(-5)
@Slf4j
public class BusinessLogAspect {


    @Pointcut(value = "@annotation(com.aliencat.springboot.aop.annotation.BusinessLogAnnotation)")
    public void pointcut() {
    }

    @Before("pointcut()")
    public void doBefore(JoinPoint point) throws NoSuchMethodException {
        //获取拦截的方法名
        Signature sig = point.getSignature();
        MethodSignature msig = null;
        if (!(sig instanceof MethodSignature)) {
            log.error("该注解只能用于方法");
            throw new IllegalArgumentException("该注解只能用于方法");
        }
        msig = (MethodSignature) sig;
        Object target = point.getTarget();
        //获取拦截方法的参数
        Method currentMethod = target.getClass().getMethod(msig.getName(), msig.getParameterTypes());
        //获取操作业务的名称
        BusinessLogAnnotation annotation = currentMethod.getAnnotation(BusinessLogAnnotation.class);
        String bussinessName = annotation.value();
        log.info("进入{}方法...", bussinessName);

    }
}
