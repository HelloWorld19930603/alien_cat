package com.aliencat.javabase.api.reflect;

import com.aliencat.javabase.api.annotation.Entity;
import com.aliencat.javabase.api.annotation.MyAnnotation;
import com.aliencat.javabase.api.annotation.Service;
import org.junit.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class AnnotationReflectDemo {

    /**
     * 访问类的所有注解信息：
     */
    @Test
    public void test1() {

        Class aClass = MyObject.class;
        Annotation[] annotations = aClass.getAnnotations();

        for (Annotation annotation : annotations) {
            if (annotation instanceof MyAnnotation) {
                MyAnnotation myAnnotation = (MyAnnotation) annotation;
                System.out.println("value: " + myAnnotation.value());
            }
        }
    }

    /**
     * 访问属性注解
     */
    @Test
    public void test2() {
        Field[] fields = AnnotationObject.class.getDeclaredFields();
        for (Field field : fields) {
            Annotation[] annotations = field.getDeclaredAnnotations();
            for (Annotation annotation : annotations) {
                MyAnnotation myAnnotation = (MyAnnotation) annotation;
                System.out.println("value: " + myAnnotation.value());
            }
        }
    }

    /**
     * 访问方法注解信息
     */
    @Test
    public void test3() {
        Method[] methods = AnnotationObject.class.getDeclaredMethods();
        for (Method method : methods) {
            Annotation[] annotations = method.getDeclaredAnnotations();
            for (Annotation annotation : annotations) {
                if (annotation instanceof MyAnnotation) {
                    MyAnnotation myAnnotation = (MyAnnotation) annotation;
                    System.out.println("value: " + myAnnotation.value());
                }
            }
        }
    }

    /**
     * 访问方法参数注解信息
     */
    @Test
    public void test4() throws NoSuchMethodException {
        Method method = AnnotationObject.class
                .getDeclaredMethod("play", String.class);//私有方法需要使用getDeclaredMethod方法
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        Class[] parameterTypes = method.getParameterTypes();

        int i = 0;
        for (Annotation[] annotations : parameterAnnotations) {
            Class parameterType = parameterTypes[i++];
            for (Annotation annotation : annotations) {
                if (annotation instanceof MyAnnotation) {
                    MyAnnotation myAnnotation = (MyAnnotation) annotation;
                    System.out.println("param: " + parameterType.getName());
                    System.out.println("value: " + myAnnotation.value());
                }
            }
        }
    }
}

@Service(value = "service")
@MyAnnotation(value = "myAnnotation")
@Entity
class AnnotationObject {

    @MyAnnotation(value = "field1")
    public String name = "zhangsan";

    @MyAnnotation(value = "field2")
    private int age;

    public AnnotationObject() {
    }


    public AnnotationObject(int age) {
        this.age = age;
    }

    public AnnotationObject(int age, String name) {
        this.age = age;
        this.name = name;
        System.out.println("hello ");
    }

    @MyAnnotation(key = "method")
    private void play(@MyAnnotation("玩具") String toy) {
        System.out.println("--->play--->" + toy);
    }

    public void walk() {
        System.out.println("--->walk--->");
    }

    public void talk(int i) {
        System.out.println("talk---------->" + i);
    }
}