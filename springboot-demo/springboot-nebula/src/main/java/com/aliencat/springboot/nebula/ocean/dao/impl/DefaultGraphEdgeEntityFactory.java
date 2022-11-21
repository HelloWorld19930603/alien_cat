
package com.aliencat.springboot.nebula.ocean.dao.impl;

import com.google.common.collect.Maps;
import com.aliencat.springboot.nebula.ocean.annotation.GraphProperty;
import com.aliencat.springboot.nebula.ocean.common.GraphHelper;
import com.aliencat.springboot.nebula.ocean.dao.GraphEdgeEntityFactory;
import com.aliencat.springboot.nebula.ocean.dao.GraphTypeManager;
import com.aliencat.springboot.nebula.ocean.domain.impl.GraphEdgeEntity;
import com.aliencat.springboot.nebula.ocean.domain.impl.GraphEdgeType;
import com.aliencat.springboot.nebula.ocean.domain.impl.GraphVertexType;
import com.aliencat.springboot.nebula.ocean.enums.ErrorEnum;
import com.aliencat.springboot.nebula.ocean.enums.GraphPropertyTypeEnum;
import com.aliencat.springboot.nebula.ocean.exception.CheckThrower;
import com.aliencat.springboot.nebula.ocean.exception.NebulaException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * @author Anyzm
 * created by ZhaoLai Huang on 2021/7/18
 */
@Slf4j
public class DefaultGraphEdgeEntityFactory implements GraphEdgeEntityFactory {

    private GraphTypeManager graphTypeManager;

    public DefaultGraphEdgeEntityFactory() {
        this.graphTypeManager = new DefaultGraphTypeManager();
    }

    public DefaultGraphEdgeEntityFactory(Field[] declaredFields, GraphTypeManager graphTypeManager) {
        this.graphTypeManager = graphTypeManager;
    }

    private <S, T, E> Pair<String, String> collectEdgeEntityProperties(E input, Field[] declaredFields,
                                                                       GraphEdgeType<S, T, E> graphEdgeType,
                                                                       Map<String, Object> propertyMap) {
        String srcId = null;
        String dstId = null;
        for (Field declaredField : declaredFields) {
            declaredField.setAccessible(true);
            GraphProperty graphProperty = declaredField.getAnnotation(GraphProperty.class);
            if (graphProperty == null) {
                continue;
            }
            Object value = GraphHelper.formatFieldValue(declaredField, graphProperty, input, graphEdgeType);
            if (graphProperty.propertyTypeEnum().equals(GraphPropertyTypeEnum.GRAPH_EDGE_SRC_ID)) {
                srcId = (String) value;
                if (!graphEdgeType.isSrcIdAsField()) {
                    continue;
                }
            }
            if (graphProperty.propertyTypeEnum().equals(GraphPropertyTypeEnum.GRAPH_EDGE_DST_ID)) {
                dstId = (String) value;
                if (!graphEdgeType.isDstIdAsField()) {
                    continue;
                }
            }
            if (value != null) {
                propertyMap.put(graphProperty.value(), value);
            }
        }
        return new ImmutablePair<String, String>(srcId, dstId);
    }

    @Override
    public <S, T, E> GraphEdgeEntity<S, T, E> buildGraphEdgeEntity(E input) throws NebulaException {
        if (input == null) {
            return null;
        }
        Class<E> inputClass = (Class<E>) input.getClass();
        GraphEdgeType<S, T, E> graphEdgeType = graphTypeManager.getGraphEdgeType(inputClass);
        if (graphEdgeType == null) {
            return null;
        }
        //起点类型
        GraphVertexType<?> srcVertexType = graphEdgeType.getSrcVertexType();
        //终点类型
        GraphVertexType<?> dstVertexType = graphEdgeType.getDstVertexType();
        Field[] declaredFields = inputClass.getDeclaredFields();
        String srcId = null;
        String dstId = null;
        //所有属性与值
        Map<String, Object> propertyMap = Maps.newHashMapWithExpectedSize(declaredFields.length);
        Pair<String, String> idPair = collectEdgeEntityProperties(input, declaredFields, graphEdgeType, propertyMap);
        srcId = StringUtils.isNotBlank(idPair.getKey()) ? idPair.getKey() : srcId;
        dstId = StringUtils.isNotBlank(idPair.getValue()) ? idPair.getValue() : dstId;
        Class<? super E> superclass = inputClass.getSuperclass();
        while (superclass != Object.class) {
            declaredFields = superclass.getDeclaredFields();
            idPair = collectEdgeEntityProperties(input, declaredFields, graphEdgeType, propertyMap);
            srcId = StringUtils.isNotBlank(idPair.getKey()) ? idPair.getKey() : srcId;
            dstId = StringUtils.isNotBlank(idPair.getValue()) ? idPair.getValue() : dstId;
            superclass = superclass.getSuperclass();
        }
        CheckThrower.ifTrueThrow(StringUtils.isBlank(srcId) || StringUtils.isBlank(dstId),
                ErrorEnum.INVALID_ID);
        return new GraphEdgeEntity(graphEdgeType, srcId, dstId, srcVertexType, dstVertexType, propertyMap);
    }
}
