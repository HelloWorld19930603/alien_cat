package com.aliencat.springboot.nebula.ocean.dao.impl;


import com.aliencat.springboot.nebula.ocean.annotation.GraphEdge;
import com.aliencat.springboot.nebula.ocean.cache.DefaultGraphTypeCache;
import com.aliencat.springboot.nebula.ocean.cache.GraphTypeCache;
import com.aliencat.springboot.nebula.ocean.dao.GraphEdgeTypeFactory;
import com.aliencat.springboot.nebula.ocean.dao.GraphTypeManager;
import com.aliencat.springboot.nebula.ocean.dao.GraphVertexTypeFactory;
import com.aliencat.springboot.nebula.ocean.domain.GraphLabel;
import com.aliencat.springboot.nebula.ocean.domain.impl.GraphEdgeType;
import com.aliencat.springboot.nebula.ocean.domain.impl.GraphVertexType;
import com.aliencat.springboot.nebula.ocean.exception.NebulaException;


public class DefaultGraphTypeManager implements GraphTypeManager {

    private final GraphTypeCache graphTypeCache;
    private final GraphVertexTypeFactory graphVertexTypeFactory;
    private final GraphEdgeTypeFactory graphEdgeTypeFactory;

    public DefaultGraphTypeManager() {
        this.graphTypeCache = new DefaultGraphTypeCache();
        this.graphVertexTypeFactory = new DefaultGraphVertexTypeFactory();
        this.graphEdgeTypeFactory = new DefaultGraphEdgeTypeFactory();
    }

    @Override
    public <T> GraphVertexType<T> getGraphVertexType(Class<T> clazz) throws NebulaException {
        if (clazz == null) {
            return null;
        }
        GraphVertexType<T> graphVertexType = graphTypeCache.getGraphVertexType(clazz);
        if (graphVertexType != null) {
            return graphVertexType;
        }
        graphVertexType = graphVertexTypeFactory.buildGraphVertexType(clazz);
        graphTypeCache.putGraphVertexType(clazz, graphVertexType);
        return graphVertexType;
    }

    @Override
    public <S, T, E> GraphEdgeType<S, T, E> getGraphEdgeType(Class<E> clazz) throws NebulaException {
        if (clazz == null) {
            return null;
        }
        GraphEdgeType graphEdgeType = graphTypeCache.getGraphEdgeType(clazz);
        if (graphEdgeType != null) {
            return graphEdgeType;
        }
        GraphEdge graphEdge = clazz.getAnnotation(GraphEdge.class);
        if (graphEdge == null) {
            return null;
        }
        Class srcVertexClass = graphEdge.srcVertex();
        Class dstVertexClass = graphEdge.dstVertex();
        GraphVertexType srcGraphVertexType = graphTypeCache.getGraphVertexType(srcVertexClass);
        GraphVertexType dstGraphVertexType = graphTypeCache.getGraphVertexType(dstVertexClass);
        graphEdgeType = graphEdgeTypeFactory.buildGraphEdgeType(clazz, srcGraphVertexType,
                dstGraphVertexType);
        graphTypeCache.putGraphEdgeType(clazz, graphEdgeType);
        if (srcGraphVertexType == null) {
            graphTypeCache.putGraphVertexType(graphEdgeType.getSrcVertexType().getTypeClass(), graphEdgeType.getSrcVertexType());
        }
        if (dstGraphVertexType == null) {
            graphTypeCache.putGraphVertexType(graphEdgeType.getDstVertexType().getTypeClass(), graphEdgeType.getDstVertexType());
        }
        return graphEdgeType;
    }

    @Override
    public GraphLabel getGraphLabel(Class clazz) throws NebulaException {
        try {
            GraphEdgeType graphEdgeType = this.getGraphEdgeType(clazz);
            if (graphEdgeType == null) {
                return this.getGraphVertexType(clazz);
            } else {
                return graphEdgeType;
            }
        } catch (NebulaException e) {
            return this.getGraphVertexType(clazz);
        }
    }
}
