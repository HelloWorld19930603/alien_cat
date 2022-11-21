package com.aliencat.springboot.nebula.ocean.domain;


import com.aliencat.springboot.nebula.ocean.dao.GraphValueFormatter;
import com.aliencat.springboot.nebula.ocean.domain.impl.GraphVertexType;
import com.aliencat.springboot.nebula.ocean.enums.GraphDataTypeEnum;
import com.aliencat.springboot.nebula.ocean.enums.GraphKeyPolicy;

import java.util.List;
import java.util.Map;

/**
 * Description  GraphLabelBuilder is used for
 *
 * @author Anyzm
 * Date  2021/9/13 - 16:08
 * @version 1.0.0
 */
public interface GraphLabelBuilder {

    /**
     * 构造元素名
     *
     * @param graphLabelName
     * @return
     */
    GraphLabelBuilder graphLabelName(String graphLabelName);

    /**
     * 构造元素对应的类
     *
     * @param labelClass
     * @return
     */
    GraphLabelBuilder labelClass(Class labelClass);

    /**
     * 构造属性格式化map
     *
     * @param propertyFormatMap
     * @return
     */
    GraphLabelBuilder propertyFormatMap(Map<String, GraphValueFormatter> propertyFormatMap);

    /**
     * 构造数据类型map
     *
     * @param dataTypeMap
     * @return
     */
    GraphLabelBuilder dataTypeMap(Map<String, GraphDataTypeEnum> dataTypeMap);

    /**
     * 构造必要属性
     *
     * @param mustProps
     * @return
     */
    GraphLabelBuilder mustProps(List<String> mustProps);

    /**
     * 构造属性字段map
     *
     * @param propertyFieldMap
     * @return
     */
    GraphLabelBuilder propertyFieldMap(Map<String, String> propertyFieldMap);

    /**
     * 构造主键策略
     *
     * @param graphKeyPolicy
     * @return
     */
    GraphLabelBuilder graphKeyPolicy(GraphKeyPolicy graphKeyPolicy);

    /**
     * 构造id是否作为字段
     *
     * @param idAsField
     * @return
     */
    GraphLabelBuilder idAsField(boolean idAsField);

    /**
     * 构造id值格式转化器
     *
     * @param idValueFormatter
     * @return
     */
    GraphLabelBuilder idValueFormatter(GraphValueFormatter idValueFormatter);


    /**
     * 构造起点id是否作为字段
     *
     * @param srcIdAsField
     * @return
     */
    GraphLabelBuilder srcIdAsField(boolean srcIdAsField);

    /**
     * 构造目标id是否作为字段
     *
     * @param dstIdAsField
     * @return
     */
    GraphLabelBuilder dstIdAsField(boolean dstIdAsField);

    /**
     * 构造起点id值格式转化器
     *
     * @param srcIdValueFormatter
     * @return
     */
    GraphLabelBuilder srcIdValueFormatter(GraphValueFormatter srcIdValueFormatter);

    /**
     * 构造目标id值格式转化器
     *
     * @param dstIdValueFormatter
     * @return
     */
    GraphLabelBuilder dstIdValueFormatter(GraphValueFormatter dstIdValueFormatter);

    /**
     * 构造起点顶点类型
     *
     * @param srcGraphVertexType
     * @return
     */
    GraphLabelBuilder srcGraphVertexType(GraphVertexType srcGraphVertexType);

    /**
     * 构造目标顶点类型
     *
     * @param dstGraphVertexType
     * @return
     */
    GraphLabelBuilder dstGraphVertexType(GraphVertexType dstGraphVertexType);

    /**
     * 构造出真实的元素
     *
     * @return
     */
    GraphLabel build();

}
