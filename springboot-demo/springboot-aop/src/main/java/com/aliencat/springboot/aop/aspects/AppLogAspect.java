package com.aliencat.springboot.aop.aspects;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Enumeration;

/**
 * Order 定义切面执行的优先级，数字越低，优先级越高
 * 在切入点之前执行：按order值有小到大的顺序执行
 * 在切入点之后执行：按order值由大到小的顺序执行
 * 其使用步骤如下：
 * 使用 @Aspect 声明切面类
 * 在切面类内添加一个切点 pointcut()，为了实现此切点灵活可装配的能力，这里使用 execution 全部拦截
 * 在切面类中使用 注解 @Before 声明一个通知方法 doBefore() 在 Controller 方法被执行之前执行；
 * 在切面类中使用 注解 @After 声明一个通知方法 doAfter() 在 Controller 方法被执行之后执行。
 */
@Component
@Aspect
@Order(-3)
@Slf4j
public class AppLogAspect {

    // 保证多线程下的数据隔离，这里主要是用来传递时间
    private ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    @Pointcut("execution(* com.aliencat.springboot.aop.controller.AopController.*(..))")
    public void pointcut() {
    }

    @Before("pointcut()")
    public void doBefore(JoinPoint joinPoint) {
        //记录请求执行前的系统时间
        threadLocal.set(System.currentTimeMillis());
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        // 记录请求的内容
        log.info("Request URL: {}", request.getRequestURL().toString());
        log.info("Request Method: {}", request.getMethod());
        log.info("IP: {}", request.getRemoteAddr());
        log.info("User-Agent:{}", request.getHeader("User-Agent"));
        log.info("Class Method:{}", joinPoint.getSignature().getDeclaringTypeName()
                + "." + joinPoint.getSignature().getName());
        log.info("Cookies:{}", request.getCookies().toString());
        log.info("Params:{}", Arrays.toString(joinPoint.getArgs()));
        Enumeration<String> enums = request.getParameterNames();
        while (enums.hasMoreElements()) {
            String paraName = enums.nextElement();
            log.info(paraName + ":" + request.getParameter(paraName));
        }
    }

    @After("pointcut()")
    public void doAfter(JoinPoint joinPoint) {
        log.info("doAfter():{}", joinPoint.toString());
    }

    @AfterReturning("pointcut()")
    public void doAfterReturning(JoinPoint joinPoint) {
        //统计一次请求的耗时
        log.info("耗时 :{}", ((System.currentTimeMillis() - threadLocal.get())) + "ms");
    }
}
