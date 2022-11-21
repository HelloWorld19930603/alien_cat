package com.aliencat.springboot.nebula.ocean.dao;


import com.aliencat.springboot.nebula.ocean.domain.GraphLabel;
import com.aliencat.springboot.nebula.ocean.domain.impl.GraphEdgeType;
import com.aliencat.springboot.nebula.ocean.domain.impl.GraphVertexType;
import com.aliencat.springboot.nebula.ocean.exception.NebulaException;

/**
 * Description  GraphTypeManager is used for
 *
 * @author Anyzm
 * Date  2021/7/16 - 18:06
 * @version 1.0.0
 */
public interface GraphTypeManager {
    /**
     * 根据类类型获取顶点类型
     *
     * @param clazz
     * @param <T>
     * @return
     * @throws NebulaException
     */
    <T> GraphVertexType<T> getGraphVertexType(Class<T> clazz) throws NebulaException;


    /**
     * 根据类类型获取顶点类型
     *
     * @param clazz
     * @return
     * @throws NebulaException
     */
    <S, T, E> GraphEdgeType<S, T, E> getGraphEdgeType(Class<E> clazz) throws NebulaException;

    /**
     * 根据类对象获取图标签
     *
     * @param clazz
     * @return
     * @throws NebulaException
     */
    GraphLabel getGraphLabel(Class clazz) throws NebulaException;

}
