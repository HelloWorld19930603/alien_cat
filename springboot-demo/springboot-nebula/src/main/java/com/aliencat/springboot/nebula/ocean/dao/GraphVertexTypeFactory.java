package com.aliencat.springboot.nebula.ocean.dao;


import com.aliencat.springboot.nebula.ocean.domain.impl.GraphVertexType;
import com.aliencat.springboot.nebula.ocean.exception.NebulaException;

public interface GraphVertexTypeFactory {

    /**
     * 根据类创建顶点类型
     *
     * @param clazz
     * @param <T>
     * @return
     * @throws NebulaException
     */
    <T> GraphVertexType<T> buildGraphVertexType(Class<T> clazz) throws NebulaException;

}
