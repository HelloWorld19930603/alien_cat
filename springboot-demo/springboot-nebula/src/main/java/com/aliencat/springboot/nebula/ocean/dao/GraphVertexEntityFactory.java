package com.aliencat.springboot.nebula.ocean.dao;


import com.aliencat.springboot.nebula.ocean.domain.impl.GraphVertexEntity;
import com.aliencat.springboot.nebula.ocean.exception.NebulaException;

/**
 * @author: Anyzm
 * @Description:
 * @Date: Created in 19:59 2021/7/18
 */
public interface GraphVertexEntityFactory {

    /**
     * 构建GraphVertexEntity
     *
     * @param input
     * @return
     * @throws NebulaException
     */
    <T> GraphVertexEntity<T> buildGraphVertexEntity(T input) throws NebulaException;

}
