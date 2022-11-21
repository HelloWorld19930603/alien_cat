
package com.aliencat.springboot.nebula.ocean.annotation;


import com.aliencat.springboot.nebula.ocean.enums.GraphKeyPolicy;

import java.lang.annotation.*;

/**
 * 业务说明：标注顶点类型
 *
 * @author Anyzm
 * @date 2021/4/28
 **/
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface GraphVertex {

    /**
     * 顶点名称
     *
     * @return
     */
    String value();

    /**
     * 主键生成方法
     *
     * @return
     */
    GraphKeyPolicy keyPolicy();

    /**
     * 顶点id是否作为属性
     *
     * @return
     */
    boolean idAsField() default true;

}
