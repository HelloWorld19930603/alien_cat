
package com.aliencat.springboot.nebula.ocean.dao.impl;

import com.aliencat.springboot.nebula.ocean.annotation.GraphProperty;
import com.aliencat.springboot.nebula.ocean.common.GraphHelper;
import com.aliencat.springboot.nebula.ocean.dao.GraphTypeManager;
import com.aliencat.springboot.nebula.ocean.dao.GraphVertexEntityFactory;
import com.aliencat.springboot.nebula.ocean.domain.impl.GraphVertexEntity;
import com.aliencat.springboot.nebula.ocean.domain.impl.GraphVertexType;
import com.aliencat.springboot.nebula.ocean.enums.ErrorEnum;
import com.aliencat.springboot.nebula.ocean.enums.GraphPropertyTypeEnum;
import com.aliencat.springboot.nebula.ocean.exception.CheckThrower;
import com.aliencat.springboot.nebula.ocean.exception.NebulaException;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * @author Anyzm
 * created by ZhaoLai Huang on 2021/7/18
 */
@Slf4j
public class DefaultGraphVertexEntityFactory implements GraphVertexEntityFactory {

    private GraphTypeManager graphTypeManager;

    public DefaultGraphVertexEntityFactory(GraphTypeManager graphTypeManager) {
        this.graphTypeManager = graphTypeManager;
    }

    public DefaultGraphVertexEntityFactory() {
        this.graphTypeManager = new DefaultGraphTypeManager();
    }

    private <T> String collectVertexEntityProperties(T input, Field[] declaredFields, GraphVertexType<T> graphVertexType,
                                                     Map<String, Object> propertyMap) {
        String id = null;
        for (Field declaredField : declaredFields) {
            declaredField.setAccessible(true);
            GraphProperty graphProperty = declaredField.getAnnotation(GraphProperty.class);
            if (graphProperty == null) {
                continue;
            }
            Object value = GraphHelper.formatFieldValue(declaredField, graphProperty, input, graphVertexType);
            if (graphProperty.propertyTypeEnum().equals(GraphPropertyTypeEnum.GRAPH_VERTEX_ID)) {
                id = (String) value;
                if (!graphVertexType.isIdAsField()) {
                    continue;
                }
            }
            if (value != null) {
                propertyMap.put(graphProperty.value(), value);
            }
        }
        return id;
    }

    @Override
    public <T> GraphVertexEntity<T> buildGraphVertexEntity(T input) throws NebulaException {
        if (input == null) {
            return null;
        }
        Class<T> inputClass = (Class<T>) input.getClass();
        GraphVertexType<T> graphVertexType = graphTypeManager.getGraphVertexType(inputClass);
        if (graphVertexType == null) {
            return null;
        }
        Field[] declaredFields = inputClass.getDeclaredFields();
        String id = null;
        Map<String, Object> propertyMap = Maps.newHashMapWithExpectedSize(declaredFields.length);
        String tempId = collectVertexEntityProperties(input, declaredFields, graphVertexType, propertyMap);
        id = StringUtils.isNotBlank(tempId) ? tempId : id;
        Class<? super T> superclass = inputClass.getSuperclass();
        while (superclass != Object.class) {
            declaredFields = superclass.getDeclaredFields();
            tempId = collectVertexEntityProperties(input, declaredFields, graphVertexType, propertyMap);
            id = StringUtils.isNotBlank(tempId) ? tempId : id;
            superclass = superclass.getSuperclass();
        }
        CheckThrower.ifTrueThrow(StringUtils.isBlank(id), ErrorEnum.INVALID_ID);
        return new GraphVertexEntity<>(graphVertexType, id, propertyMap);
    }
}
