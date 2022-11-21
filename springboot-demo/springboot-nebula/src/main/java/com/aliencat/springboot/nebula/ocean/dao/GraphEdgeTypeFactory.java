package com.aliencat.springboot.nebula.ocean.dao;


import com.aliencat.springboot.nebula.ocean.domain.impl.GraphEdgeType;
import com.aliencat.springboot.nebula.ocean.domain.impl.GraphVertexType;
import com.aliencat.springboot.nebula.ocean.exception.NebulaException;

public interface GraphEdgeTypeFactory {

    /**
     * 根据类创建边类型
     *
     * @param clazz
     * @return
     * @throws NebulaException
     */
    <S, T, E> GraphEdgeType<S, T, E> buildGraphEdgeType(Class<E> clazz) throws NebulaException;


    /**
     * 根据类 和 顶点类型创建边类型
     *
     * @param clazz
     * @param srcGraphVertexType
     * @param dstGraphVertexType
     * @return
     * @throws NebulaException
     */
    <S, T, E> GraphEdgeType<S, T, E> buildGraphEdgeType(Class<E> clazz, GraphVertexType<S> srcGraphVertexType,
                                                               GraphVertexType<T> dstGraphVertexType) throws NebulaException;

}
