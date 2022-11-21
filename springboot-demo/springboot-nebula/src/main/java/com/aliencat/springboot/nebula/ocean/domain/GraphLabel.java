package com.aliencat.springboot.nebula.ocean.domain;


import com.aliencat.springboot.nebula.ocean.enums.GraphDataTypeEnum;

import java.util.Collection;
import java.util.List;

/**
 * Description  GraphLabel is used for
 *
 * @author Anyzm
 * Date  2021/8/10 - 11:18
 * @version 1.0.0
 */
public interface GraphLabel {

    /**
     * 是否Tag
     *
     * @return
     */
    boolean isTag();

    /**
     * 是否关系(边)
     *
     * @return
     */
    boolean isEdge();

    /**
     * 获取标签名称
     *
     * @return
     */
    String getName();

    /**
     * 获取必要字段
     *
     * @return
     */
    List<String> getMustFields();

    /**
     * 获取所有字段
     *
     * @return
     */
    Collection<String> getAllFields();

    /**
     * 格式化属性值
     *
     * @param field
     * @param originalValue
     * @return
     */
    Object formatValue(String field, Object originalValue);

    /**
     * 反转格式化属性值
     *
     * @param field
     * @param databaseValue
     * @return
     */
    Object reformatValue(String field, Object databaseValue);

    /**
     * 获取字段名
     *
     * @param property
     * @return
     */
    String getFieldName(String property);

    /**
     * 获取属性名
     *
     * @param field
     * @return
     */
    String getPropertyName(String field);

    /**
     * 获取字段的数据类型
     *
     * @param field
     * @return
     */
    GraphDataTypeEnum getFieldDataType(String field);

}
