package com.aliencat.javabase.api.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface MyAnnotation {
    public String key() default "default_key";
    public String value() default "default_value";
}

@MyAnnotation
interface inter{
    public void run();
}

@MyAnnotation
@interface MyAnnotationA{

}

//定义类上
@MyAnnotation
class Info implements inter{
    //定义在属性上
    @MyAnnotation
    public static String desc = "info";

    //定义在构造器上
    @MyAnnotation
    Info(){
        System.out.println("InheritableFather:"+Info.class.isAnnotationPresent(Inherited.class));
    }

    //定义在方法和参数上
    @MyAnnotation
    public static void main(@MyAnnotation String[] args) {
        System.out.println(desc);
    }


    @Deprecated
    @SuppressWarnings({"deprecated","unused"})
    @MyAnnotation
    public void run() {

    }


}