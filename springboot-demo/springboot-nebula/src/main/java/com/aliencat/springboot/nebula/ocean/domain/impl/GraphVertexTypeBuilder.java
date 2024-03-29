package com.aliencat.springboot.nebula.ocean.domain.impl;


import com.aliencat.springboot.nebula.ocean.dao.GraphValueFormatter;
import com.aliencat.springboot.nebula.ocean.domain.GraphLabelBuilder;
import com.aliencat.springboot.nebula.ocean.enums.GraphDataTypeEnum;
import com.aliencat.springboot.nebula.ocean.enums.GraphKeyPolicy;

import java.util.List;
import java.util.Map;

/**
 * Description  GraphVertexTypeBuilder is used for
 *
 * @author Anyzm
 * Date  2021/9/13 - 16:15
 * @version 1.0.0
 */
public class GraphVertexTypeBuilder implements GraphLabelBuilder {

    private final GraphVertexType graphVertexType;

    private GraphVertexTypeBuilder() {
        this.graphVertexType = new GraphVertexType();
    }


    public static GraphVertexTypeBuilder builder() {
        return new GraphVertexTypeBuilder();
    }

    @Override
    public GraphVertexTypeBuilder graphLabelName(String graphLabelName) {
        this.graphVertexType.setVertexName(graphLabelName);
        return this;
    }

    @Override
    public GraphVertexTypeBuilder labelClass(Class labelClass) {
        this.graphVertexType.setTypeClass(labelClass);
        return this;
    }

    @Override
    public GraphVertexTypeBuilder propertyFormatMap(Map<String, GraphValueFormatter> propertyFormatMap) {
        this.graphVertexType.setPropertyFormatMap(propertyFormatMap);
        return this;
    }

    @Override
    public GraphVertexTypeBuilder dataTypeMap(Map<String, GraphDataTypeEnum> dataTypeMap) {
        this.graphVertexType.setDataTypeMap(dataTypeMap);
        return this;
    }

    @Override
    public GraphVertexTypeBuilder mustProps(List<String> mustProps) {
        this.graphVertexType.setMustFields(mustProps);
        return this;
    }

    @Override
    public GraphVertexTypeBuilder propertyFieldMap(Map<String, String> propertyFieldMap) {
        this.graphVertexType.setPropertyFieldMap(propertyFieldMap);
        return this;
    }

    @Override
    public GraphVertexTypeBuilder graphKeyPolicy(GraphKeyPolicy graphKeyPolicy) {
        this.graphVertexType.setGraphKeyPolicy(graphKeyPolicy);
        return this;
    }

    @Override
    public GraphVertexTypeBuilder idAsField(boolean idAsField) {
        this.graphVertexType.setIdAsField(idAsField);
        return this;
    }

    @Override
    public GraphVertexTypeBuilder idValueFormatter(GraphValueFormatter idValueFormatter) {
        this.graphVertexType.setIdValueFormatter(idValueFormatter);
        return this;
    }

    @Override
    public GraphVertexTypeBuilder srcIdAsField(boolean srcIdAsField) {
        return this;
    }

    @Override
    public GraphVertexTypeBuilder dstIdAsField(boolean dstIdAsField) {
        return this;
    }

    @Override
    public GraphVertexTypeBuilder srcIdValueFormatter(GraphValueFormatter srcIdValueFormatter) {
        return this;
    }

    @Override
    public GraphVertexTypeBuilder dstIdValueFormatter(GraphValueFormatter dstIdValueFormatter) {
        return this;
    }

    @Override
    public GraphVertexTypeBuilder srcGraphVertexType(GraphVertexType srcGraphVertexType) {
        return this;
    }

    @Override
    public GraphVertexTypeBuilder dstGraphVertexType(GraphVertexType dstGraphVertexType) {
        return this;
    }

    @Override
    public GraphVertexType build() {
        return this.graphVertexType;
    }
}
