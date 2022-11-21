package com.aliencat.springboot.nebula.ocean.domain;


import com.aliencat.springboot.nebula.ocean.domain.impl.QueryResult;
import com.aliencat.springboot.nebula.ocean.exception.NebulaExecuteException;

/**
 * Description  Session is used for
 *
 * @author Anyzm
 * Date  2021/7/16 - 17:39
 * @version 1.0.0
 */
public interface Session {

    /**
     * 执行更新操作
     *
     * @param statement
     * @return
     * @throws NebulaExecuteException
     */
    int execute(String statement) throws NebulaExecuteException;

    /**
     * 执行查询
     *
     * @param statement
     * @return
     * @throws NebulaExecuteException
     */
    QueryResult executeQueryDefined(String statement) throws NebulaExecuteException;

    /**
     * 释放session
     */
    void release();

    /**
     * Need server supported, v1.0 nebula-graph doesn't supported
     *
     * @return ping服务器
     */
    boolean ping();

}
