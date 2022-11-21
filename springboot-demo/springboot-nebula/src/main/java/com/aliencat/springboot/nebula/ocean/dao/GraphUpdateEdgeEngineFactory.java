package com.aliencat.springboot.nebula.ocean.dao;


import com.aliencat.springboot.nebula.ocean.domain.impl.GraphEdgeEntity;
import com.aliencat.springboot.nebula.ocean.domain.impl.GraphVertexEntity;
import com.aliencat.springboot.nebula.ocean.exception.NebulaException;

import java.util.List;

/**
 * Description  GraphUpdateEdgeEngineFactory is used for
 *
 * @author Anyzm
 * Date  2021/7/19 - 10:27
 * @version 1.0.0
 */
public interface GraphUpdateEdgeEngineFactory {

    /**
     * 构造图边更新引擎
     *
     * @param graphEdgeEntities
     * @param <S>
     * @param <E>
     * @return
     * @throws NebulaException
     */
    <S, T, E> EdgeUpdateEngine<S, T, E> build(List<GraphEdgeEntity<S, T, E>> graphEdgeEntities) throws NebulaException;


    /**
     * 构造图边更新引擎
     *
     * @param graphEdgeEntities
     * @param srcGraphVertexEntities
     * @param dstGraphVertexEntities
     * @param <S>
     * @param <E>
     * @return
     * @throws NebulaException
     */
    <S, T, E> EdgeUpdateEngine<S, T, E> build(List<GraphEdgeEntity<S, T, E>> graphEdgeEntities,
                                                     List<GraphVertexEntity<S>> srcGraphVertexEntities,
                                                     List<GraphVertexEntity<T>> dstGraphVertexEntities) throws NebulaException;

}
