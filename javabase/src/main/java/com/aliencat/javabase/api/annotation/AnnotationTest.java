package com.aliencat.javabase.api.annotation;

import org.junit.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;

public class AnnotationTest {

    @Test
    public void test1() throws NoSuchMethodException {
        Class cs = Info.class;
        //获取run方法对象
        Method method = cs.getMethod("run");
        //获取run方法上注解，注意只能得到@Retention值为RetentionPolicy.RUNTIME的注解
        Annotation[] annotations = method.getAnnotations();
        for (Annotation an : annotations) {
            System.out.println(an.toString());
            //判断是否是指定Annotation
        }
        System.out.println();
        annotations = cs.getAnnotations();
        Arrays.stream(annotations).forEach(annotation -> System.out.println(annotation.toString()));

        //判断是否包含指定Annotation
        if (method.isAnnotationPresent(MyAnnotation.class)) {
            //获取指定Annotation
            MyAnnotation myAnnotation = method.getAnnotation(MyAnnotation.class);
            //获取Annotation的属性
            String key = myAnnotation.key();
            String value = myAnnotation.value();
            System.out.println(key + " --> " + value);
        }
    }
}
