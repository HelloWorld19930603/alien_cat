package com.aliencat.javabase.api.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/***
 *
 * 实体注解接口
 */
@Target(value = {ElementType.TYPE})
@Retention(value = RetentionPolicy.RUNTIME)
public @interface Entity {
    /***
     * 实体默认firstLevelCache属性为false
     */
    boolean firstLevelCache() default false;

    /***
     * 实体默认secondLevelCache属性为false
     */
    boolean secondLevelCache() default true;

    /***
     * 表名默认为空
     */
    String tableName() default "";

    /***
     * 默认以""分割注解
     */
    String split() default "";
}
