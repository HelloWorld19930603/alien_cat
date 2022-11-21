package com.aliencat.springboot.nebula.ocean.dao;


import com.aliencat.springboot.nebula.ocean.domain.GraphQuery;
import com.aliencat.springboot.nebula.ocean.domain.impl.QueryResult;
import com.aliencat.springboot.nebula.ocean.exception.NebulaException;
import com.vesoft.nebula.client.graph.data.ResultSet;
import com.vesoft.nebula.client.graph.exception.NotValidConnectionException;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

/**
 * Description  GraphMapper is used for
 *
 * @author Anyzm
 * Date  2021/7/19 - 14:16
 * @version 1.0.0
 */
public interface GraphMapper {

    /**
     * 批量保存顶点信息
     *
     * @param entities
     * @param <T>
     * @return
     * @throws NebulaException
     */
    <T> int saveVertexEntities(List<T> entities, boolean retry);

    <T> int saveAllVertexEntities(List<T> entities, int num);

    /**
     * 批量保存边信息和顶点信息
     *
     * @param entities
     * @param srcVertexEntityFunction
     * @param dstVertexEntityFunction
     * @param <S>
     * @param <T>
     * @param <E>
     * @return
     * @throws NebulaException
     */
    <S, T, E> int saveEdgeEntitiesWithVertex(List<E> entities, Function<String, S> srcVertexEntityFunction,
                                                    Function<String, T> dstVertexEntityFunction) throws NebulaException;


    /**
     * 批量保存边信息
     *
     * @param entities
     * @return
     * @throws NebulaException
     */
    <S, T, E> int saveEdgeEntities(List<E> entities, boolean retry);

    <S, T, E> int saveAllEdgeEntities(List<E> entities, int num);

    /**
     * 批量执行更新语句
     *
     * @param space
     * @param sqlList
     * @return
     * @throws NebulaException
     * @throws NotValidConnectionException
     */
    int executeBatchUpdateSql(String space, List<String> sqlList) throws NebulaException, NotValidConnectionException;


    /**
     * 执行更新sql
     *
     * @param space
     * @param sql
     * @return
     * @throws NebulaException
     * @throws NotValidConnectionException
     */
    int executeUpdateSql(String space, String sql) throws NebulaException, NotValidConnectionException;


    /**
     * 执行更新sql
     *
     * @param sql
     * @return
     * @throws NebulaException
     * @throws NotValidConnectionException
     */
    int executeUpdateSql(String sql) throws NebulaException, NotValidConnectionException;

    /**
     * 执行查询
     *
     * @param sql
     * @return
     * @throws NebulaException
     */
    QueryResult executeQuerySql(String sql) throws NebulaException;

    /**
     * 执行查询
     *
     * @param sql
     * @return
     * @throws NebulaException
     */
    ResultSet executeQuerySqlForResultSet(String sql) throws NebulaException;


    /**
     * 执行查询
     *
     * @param space
     * @param sql
     * @return
     * @throws NebulaException
     */
    QueryResult executeQuerySql(String space, String sql) throws NebulaException;


    /**
     * 执行查询
     *
     * @param sql
     * @param clazz
     * @param <T>
     * @return
     * @throws NebulaException
     */
    <T> Set<T> executeQuerySql(String sql, Class<T> clazz) throws NebulaException;


    /**
     * 执行查询
     *
     * @param query
     * @return
     * @throws NebulaException
     */
    QueryResult executeQuery(GraphQuery query) throws NebulaException;


    /**
     * 指定空间执行查询
     *
     * @param space
     * @param query
     * @return
     * @throws NebulaException
     */
    QueryResult executeQuery(String space, GraphQuery query) throws NebulaException;


    /**
     * 执行查询
     *
     * @param query
     * @param clazz
     * @param <T>
     * @return
     * @throws NebulaException
     */
    <T> Set<T> executeQuery(GraphQuery query, Class<T> clazz) throws NebulaException;


    /**
     * 查询边
     *
     * @param edgeClazz
     * @param vertexIds
     * @return
     */
    <T> Set<T> goOutEdge(Class<T> edgeClazz, String... vertexIds);

    /**
     * 查询某条边
     *
     * @param edgeClazz    边class
     * @param fromVertexId 边起点ID
     * @param toVertexId   边终点id
     * @return
     */
    <T> Set<T> getEdge(Class<T> edgeClazz, String fromVertexId, String toVertexId);

    /**
     * 查询多条边集合
     *
     * @param edgeClazz                     边class
     * @param fromVertexIdAndToVertexIdList 边起点ID到边终点id的集合list
     * @return
     */
    <T> Set<T> getEdgeSet(Class<T> edgeClazz, List<String> fromVertexIdAndToVertexIdList);

    /**
     * 根据属性查询某条边
     *
     * @param edgeClazz 边class
     * @param paramMap  属性参数
     * @return
     */
    <T> Set<T> getEdgeByParam(Class<T> edgeClazz, Map<String, String> paramMap);

    /**
     * 根据参数查询边
     *
     * @param edgeClazz 边class
     * @param str       属性参数
     * @return
     */
    <T> Set<T> getEdgeByStr(Class<T> edgeClazz, String str);

    /**
     * 查询反向边
     *
     * @param edgeClazz
     * @param vertexIds
     * @param <T>
     * @return
     */
    <T> Set<T> goReverseEdge(Class<T> edgeClazz, String... vertexIds);

    /**
     * 查询所有的出入边
     *
     * @param edgeClazz
     * @param vertexId
     * @return
     */
    <T> Set<T> goAllEdge(Class<T> edgeClazz, String param, String[] fieldsName, String... vertexId);

    /**
     * 查询tag
     *
     * @param vertexClazz
     * @param vertexIds
     * @return
     */
    <T> Set<T> fetchVertexTag(Class<T> vertexClazz, Set<String> vertexIds, String[] fieldsName);

    /**
     * 根据参数查询tag
     *
     * @param param      查询参数
     * @param fieldsName 字段名称
     * @return
     */
    <T> Set<T> getTagByParam(Class<T> vertexClazz, String param, String[] fieldsName);

    /**
     * 查询tag
     *
     * @param edgeClazz 边
     * @param paramMap  属性参数
     * @return
     */
    <T> Set<String> findShortestPath(Class<T> edgeClazz, String fromVertexId, String toVertexId, Map<String, String> paramMap);
}
