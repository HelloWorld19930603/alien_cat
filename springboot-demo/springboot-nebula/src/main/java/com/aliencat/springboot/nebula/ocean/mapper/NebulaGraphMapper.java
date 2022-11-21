
package com.aliencat.springboot.nebula.ocean.mapper;



import com.aliencat.springboot.nebula.ocean.common.RtsThread;
import com.aliencat.springboot.nebula.ocean.common.utils.CollectionUtils;
import com.aliencat.springboot.nebula.ocean.dao.*;
import com.aliencat.springboot.nebula.ocean.dao.impl.DefaultGraphEdgeEntityFactory;
import com.aliencat.springboot.nebula.ocean.dao.impl.DefaultGraphTypeManager;
import com.aliencat.springboot.nebula.ocean.dao.impl.DefaultGraphVertexEntityFactory;
import com.aliencat.springboot.nebula.ocean.domain.EdgeQuery;
import com.aliencat.springboot.nebula.ocean.domain.GraphLabel;
import com.aliencat.springboot.nebula.ocean.domain.GraphQuery;
import com.aliencat.springboot.nebula.ocean.domain.VertexQuery;
import com.aliencat.springboot.nebula.ocean.domain.impl.*;
import com.aliencat.springboot.nebula.ocean.engine.*;
import com.aliencat.springboot.nebula.ocean.enums.EdgeDirectionEnum;
import com.aliencat.springboot.nebula.ocean.enums.ErrorEnum;
import com.aliencat.springboot.nebula.ocean.enums.GraphDataTypeEnum;
import com.aliencat.springboot.nebula.ocean.exception.CheckThrower;
import com.aliencat.springboot.nebula.ocean.exception.NebulaException;
import com.aliencat.springboot.nebula.ocean.session.NebulaPoolSessionManager;
import com.aliencat.springboot.nebula.ocean.session.NebulaSessionWrapper;
import com.google.common.collect.Lists;
import com.vesoft.nebula.client.graph.data.ResultSet;
import com.vesoft.nebula.client.graph.exception.NotValidConnectionException;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Description  NebulaGraphMapper is used for
 *
 * @author Anyzm
 * Date  2021/7/16 - 17:37
 * @version 1.0.0
 */
@Slf4j
public class NebulaGraphMapper implements GraphMapper {

    @Setter
    @Getter
    private NebulaPoolSessionManager nebulaPoolSessionManager;

    private GraphUpdateEdgeEngineFactory graphUpdateEdgeEngineFactory;

    private GraphUpdateVertexEngineFactory graphUpdateVertexEngineFactory;

    @Resource
    private RtsThread rtsThread;

    @Setter
    @Getter
    private String space;

    private GraphVertexEntityFactory graphVertexEntityFactory;

    private GraphEdgeEntityFactory graphEdgeEntityFactory;

    private GraphTypeManager graphTypeManager;

    private void init() {
        this.graphVertexEntityFactory = new DefaultGraphVertexEntityFactory(graphTypeManager);
        this.graphEdgeEntityFactory = new DefaultGraphEdgeEntityFactory();
        NebulaCondition.setGraphTypeManager(graphTypeManager);
        NebulaVertexQuery.setGraphTypeManager(graphTypeManager);
        NebulaEdgeQuery.setGraphTypeManager(graphTypeManager);
    }

    public NebulaGraphMapper(NebulaPoolSessionManager nebulaPoolSessionManager,
                             String space) {
        this.graphTypeManager = new DefaultGraphTypeManager();
        this.graphUpdateVertexEngineFactory = new NebulaUpdateVertexEngineFactory();
        this.graphUpdateEdgeEngineFactory = new NebulaUpdateEdgeEngineFactory();
        this.nebulaPoolSessionManager = nebulaPoolSessionManager;
        this.space = space;
        init();
    }

    public NebulaGraphMapper(NebulaPoolSessionManager nebulaPoolSessionManager,
                             String space,
                             GraphUpdateVertexEngineFactory graphUpdateVertexEngineFactory,
                             GraphUpdateEdgeEngineFactory graphUpdateEdgeEngineFactory) {
        this.graphTypeManager = new DefaultGraphTypeManager();
        this.graphUpdateVertexEngineFactory = graphUpdateVertexEngineFactory;
        this.graphUpdateEdgeEngineFactory = graphUpdateEdgeEngineFactory;
        this.nebulaPoolSessionManager = nebulaPoolSessionManager;
        this.space = space;
        init();
    }

    private <T> int batchUpdateVertex(List<GraphVertexEntity<T>> graphVertexEntityList) throws NebulaException {
        VertexUpdateEngine build = this.graphUpdateVertexEngineFactory.build(graphVertexEntityList);
        List<String> sqlList = build.getSqlList();
        return executeBatchUpdateSql(space, sqlList);
    }

    @Override
    public <T> int saveVertexEntities(List<T> entities, boolean retry) {
        if (CollectionUtils.isEmpty(entities)) {
            return 0;
        }
        try {
            // 对象依次取出属性，并去掉特殊字符
           // entities.parallelStream().forEach(ToolUtil::removeSpecialCharactersForObject);
            List<GraphVertexEntity<T>> vertexEntities = Lists.newArrayListWithExpectedSize(entities.size());
            for (T entity : entities) {
                GraphVertexEntity<T> graphVertexEntity = graphVertexEntityFactory.buildGraphVertexEntity(entity);
                vertexEntities.add(graphVertexEntity);
//            log.debug("构造对象entity={},graphVertexEntity={}", entity, graphVertexEntity);
            }
            batchUpdateVertex(vertexEntities);
//            log.info("保存顶点信息到nebula,size={}", vertexEntities.size());
        } catch (Exception e) {
            log.error("saveVertexEntities jiwei 异常报错" + e.getMessage());
            if (retry) {
                log.info("saveVertexEntities jiwei 异常报错 重新保存 start " + entities.size());
                if (entities.size() <= 200) {
                    //ToolUtil.sleepRandom(100, 200);
                    saveVertexEntities(entities, false);
                } else {
                    Stream.iterate(0, n -> n + 1).limit((entities.size() / 200) + 1).forEach(i -> {
                        List<T> subArticle = entities.stream().skip(i * 200).limit(200).collect(Collectors.toList());
                        if (!CollectionUtils.isEmpty(subArticle) && subArticle.size() > 0) {
                            //ToolUtil.sleepRandom(100, 200);
                            saveVertexEntities(subArticle, false);
                        }
                    });
                }
                log.info("saveVertexEntities jiwei 异常报错 重新保存 end " + entities.size());
            } else {
                log.error("saveVertexEntities jiwei one" + e.getMessage() + entities.toString());
            }
        }

//        log.error("saveAllVertexEntities jiwei 异常报错" + e.getMessage());
//        for (T t : entities) {
//            try {
//                saveVertexEntities(Collections.singletonList(t));
//            } catch (Exception ee) {
//                log.error("saveAllVertexEntities jiwei one" + ee.getMessage());
//            }
//        }
        return 0;
    }

    @Override
    public <T> int saveAllVertexEntities(List<T> entities, int num) {

        if (!CollectionUtils.isEmpty(entities)) {
            log.info("saveAll 点 size " + entities.size());
//            ToolUtil.sleepRandom(0, 500);
            // 新增数量过大  分批插入
            if (entities.size() <= num) {
                saveVertexEntities(entities, true);
            } else {
                Stream.iterate(0, n -> n + 1).limit((entities.size() / num) + 1).forEach(i -> {
                    List<T> subArticle = entities.stream().skip(i * num).limit(num).collect(Collectors.toList());
                    if (subArticle.size() > 0) {
//                        ToolUtil.sleepRandom(200, 500);
                        saveVertexEntities(subArticle, true);
                    }
                });
            }
        }

        return 0;
    }

    private <S, T, E> int batchUpdateEdge(List<GraphEdgeEntity<S, T, E>> graphEdgeEntities) {
        EdgeUpdateEngine<S, T, E> build = this.graphUpdateEdgeEngineFactory.build(graphEdgeEntities);
        List<String> sqlList = build.getSqlList();
        return executeBatchUpdateSql(space, sqlList);
    }

    @Override
    public <S, T, E> int saveEdgeEntitiesWithVertex(List<E> entities, Function<String, S> srcVertexEntityFunction,
                                                    Function<String, T> dstVertexEntityFunction) throws NebulaException {
        if (CollectionUtils.isEmpty(entities)) {
            return 0;
        }
        List<GraphEdgeEntity<S, T, E>> graphEdgeEntities = Lists.newArrayListWithExpectedSize(entities.size());
        List<GraphVertexEntity<S>> srcGraphVertexEntities = Lists.newArrayListWithExpectedSize(entities.size());
        List<GraphVertexEntity<T>> dstGraphVertexEntities = Lists.newArrayListWithExpectedSize(entities.size());
        for (E entity : entities) {
            GraphEdgeEntity<S, T, E> graphEdgeEntity = graphEdgeEntityFactory.buildGraphEdgeEntity(entity);
            log.debug("构造对象entity={},graphEdgeEntity={}", entity, graphEdgeEntity);
            S srcEntity = srcVertexEntityFunction.apply(graphEdgeEntity.getSrcId());
            T dstEntity = dstVertexEntityFunction.apply(graphEdgeEntity.getDstId());
            GraphVertexEntity<S> srcVertexEntity = (GraphVertexEntity<S>) graphVertexEntityFactory.buildGraphVertexEntity(srcEntity);
            GraphVertexEntity<T> dstVertexEntity = (GraphVertexEntity<T>) graphVertexEntityFactory.buildGraphVertexEntity(dstEntity);
            srcGraphVertexEntities.add(srcVertexEntity);
            dstGraphVertexEntities.add(dstVertexEntity);
            graphEdgeEntities.add(graphEdgeEntity);
        }
        return batchUpdateEdgeWithVertex(graphEdgeEntities, srcGraphVertexEntities, dstGraphVertexEntities);
    }

    @Override
    public <S, T, E> int saveEdgeEntities(List<E> entities, boolean retry) {
        if (CollectionUtils.isEmpty(entities)) {
            return 0;
        }
        // 对象依次取出属性，并去掉特殊字符
       // entities.parallelStream().forEach(ToolUtil::removeSpecialCharactersForObject);
        List<GraphEdgeEntity<S, T, E>> graphEdgeEntities = Lists.newArrayListWithExpectedSize(entities.size());
        for (E entity : entities) {
            GraphEdgeEntity<S, T, E> graphEdgeEntity = graphEdgeEntityFactory.buildGraphEdgeEntity(entity);
//            log.debug("构造对象entity={},graphEdgeEntity={}", entity, graphEdgeEntity);
            graphEdgeEntities.add(graphEdgeEntity);
        }
        try {
            batchUpdateEdge(graphEdgeEntities);
        } catch (Exception e) {
            log.error("saveEdgeEntities jiwei 异常报错 " + e.getMessage());
            if (retry) {
                log.info("saveEdgeEntities jiwei 异常报错 重新保存 start " + entities.size());
                if (entities.size() <= 200) {
//                    ToolUtil.sleepRandom(100, 200);
                    saveEdgeEntities(entities, false);
                } else {
                    Stream.iterate(0, n -> n + 1).limit((entities.size() / 200) + 1).forEach(i -> {
                        List<E> subArticle = entities.stream().skip(i * 200).limit(200).collect(Collectors.toList());
                        if (!CollectionUtils.isEmpty(subArticle) && subArticle.size() > 0) {
//                            ToolUtil.sleepRandom(100, 200);
                            saveEdgeEntities(subArticle, false);
                        }
                    });
                }
                log.info("saveEdgeEntities jiwei 异常报错 重新保存 end " + entities.size());
            } else {
                log.error("saveEdgeEntities jiwei one" + e.getMessage() + entities.toString());
            }
        }
        return 0;
    }

    @Override
    public <S, T, E> int saveAllEdgeEntities(List<E> entities, int num) {
        try {
            if (!CollectionUtils.isEmpty(entities)) {
                log.info("saveAll 边 size " + entities.size());
//                ToolUtil.sleepRandom(0, 300);
                // 新增数量过大  分批插入
                if (entities.size() <= num) {
                    saveEdgeEntities(entities, true);
                } else {
                    Stream.iterate(0, n -> n + 1).limit((entities.size() / num) + 1).forEach(i -> {
                        List<E> subArticle = entities.stream().skip(i * num).limit(num).collect(Collectors.toList());
                        if (subArticle.size() > 0) {
//                            ToolUtil.sleepRandom(200, 500);
                            saveEdgeEntities(subArticle, true);
                        }
                    });
                }
            }
        } catch (Exception e) {
            log.error("saveAllEdgeEntities jiwei 异常报错 " + e.getMessage());
        }
        return 0;
    }

    private <S, T, E> int batchUpdateEdgeWithVertex(List<GraphEdgeEntity<S, T, E>> graphEdgeEntities,
                                                    List<GraphVertexEntity<S>> srcGraphVertexEntities,
                                                    List<GraphVertexEntity<T>> graphVertexEntities) throws NebulaException {
        EdgeUpdateEngine<S, T, E> build = this.graphUpdateEdgeEngineFactory.build(graphEdgeEntities,
                srcGraphVertexEntities, graphVertexEntities);
        List<String> sqlList = build.getSqlList();
        return executeBatchUpdateSql(space, sqlList);
    }

    @Override
    public int executeBatchUpdateSql(String space, List<String> sqlList) {
        NebulaSessionWrapper session = null;
        try {
            session = nebulaPoolSessionManager.getSession(space);
            for (String sql : sqlList) {
                long start = System.currentTimeMillis();
                sql.replaceAll("'", "");
//                log.info("executeBatchUpdateSql  sql: " + sql);
                int execute = session.execute(sql);
                long end = System.currentTimeMillis();
//                log.info("executeBatchUpdateSql  time: " + (end - start));
                CheckThrower.ifTrueThrow(execute != 0, ErrorEnum.UPDATE_NEBULA_EROR);
            }
        } catch (Exception e) {
            log.error("批量执行sql异常,space={},sqlList={},e={},Exception={}", space, sqlList, e.getMessage());
            throw new NebulaException(ErrorEnum.SYSTEM_ERROR);
        } finally {
            if (session != null) {
                try {
                    Thread.sleep(100);
                    session.release();
                } catch (Exception e) {
                    log.error(" session.release() " + e.getMessage());
                }
            }
        }
        return 0;
    }

    @Override
    public int executeUpdateSql(String space, String sql) throws NebulaException, NotValidConnectionException {
        NebulaSessionWrapper session = null;
        try {
            session = nebulaPoolSessionManager.getSession(space);
            return session.execute(sql);
        } catch (Exception e) {
            log.error("executeUpdateSql 执行sql异常,space={},sql={},Exception={}", space, sql, e.getMessage());
            throw new NebulaException(ErrorEnum.SYSTEM_ERROR);
        } finally {
            if (session != null) {
                session.release();
            }
        }
    }

    @Override
    public int executeUpdateSql(String sql) throws NebulaException, NotValidConnectionException {
        return executeUpdateSql(this.space, sql);
    }

    @Override
    public QueryResult executeQuerySql(String sql) throws NebulaException {
        return executeQuerySql(this.space, sql);
    }

    @Override
    public ResultSet executeQuerySqlForResultSet(String sql) throws NebulaException {
        NebulaSessionWrapper session = null;
        try {
            session = nebulaPoolSessionManager.getSession(space);
            assert session != null;
            return session.executeQuery(sql);
        } catch (Exception e) {
            log.error("executeQuerySqlForResultSet 执行sql异常,space={},sql={},Exception={}", space, sql, e.getMessage());
            throw new NebulaException(ErrorEnum.SYSTEM_ERROR);
        } finally {
            if (session != null) {
                session.release();
            }
        }
    }

    @Override
    public QueryResult executeQuerySql(String space, String sql) throws NebulaException {
        NebulaSessionWrapper session = null;
        try {
            session = nebulaPoolSessionManager.getSession(space);
            return session.executeQueryDefined(sql);
        } catch (Exception e) {
            log.error("执行sql异常,space={},sql={},Exception={}", space, sql, e.getMessage());
            throw new NebulaException(ErrorEnum.SYSTEM_ERROR);
        } finally {
            if (session != null) {
                session.release();
            }
        }
    }

    @Override
    public <T> Set<T> executeQuerySql(String sql, Class<T> clazz) throws NebulaException {
        long a = System.currentTimeMillis();
        QueryResult rows = executeQuerySql(sql);
        GraphLabel graphLabel = graphTypeManager.getGraphLabel(clazz);
        if (CollectionUtils.isEmpty(rows.getData())) {
            return Collections.emptySet();
        }

        rows.getData().parallelStream().forEach(row -> {
            for (Map.Entry<String, Object> entry : row) {
                String key = entry.getKey();
                String fieldName = graphLabel.getFieldName(key);

                Object o = graphLabel.reformatValue(fieldName, entry.getValue());
                if (o.toString().contains("__NULL__")) {
                    GraphDataTypeEnum fieldDataType = graphLabel.getFieldDataType(fieldName);
                    if (fieldDataType.name().equalsIgnoreCase("INT") || fieldDataType.name().equalsIgnoreCase("DOUBLE")) {
                        o = 0;
                    }
                    if (fieldDataType.name().equalsIgnoreCase("STRING")) {
                        o = null;
                    }
                    if (fieldDataType.name().equalsIgnoreCase("BOOL")) {
                        o = false;
                    }
                }
                row.setProp(key, o);
            }
        });

        long e = System.currentTimeMillis();
        log.info(" executeQuerySql tt=" + (e - a));
        return rows.getEntities(clazz);
    }

    @Override
    public QueryResult executeQuery(GraphQuery query) throws NebulaException {
        return executeQuerySql(query.buildSql());
    }

    @Override
    public QueryResult executeQuery(String space, GraphQuery query) throws NebulaException {
        return executeQuerySql(space, query.buildSql());
    }

    @Override
    public <T> Set<T> executeQuery(GraphQuery query, Class<T> clazz) throws NebulaException {
        return executeQuerySql(query.buildSql(), clazz);
    }

    @Override
    public <T> Set<T> goOutEdge(Class<T> edgeClazz, String... vertexIds) {
        GraphEdgeType<Object, Object, T> graphEdgeType = graphTypeManager.getGraphEdgeType(edgeClazz);
        String[] fieldsName = CollectionUtils.toStringArray(graphEdgeType.getAllFields());
        EdgeQuery query = NebulaEdgeQuery.build().goFrom(edgeClazz, vertexIds).yield(edgeClazz, fieldsName);
        return executeQuery(query, edgeClazz);
    }

    @Override
    public <T> Set<T> getEdge(Class<T> edgeClazz, String fromVertexId, String toVertexId) {
        GraphEdgeType<Object, Object, T> graphEdgeType = graphTypeManager.getGraphEdgeType(edgeClazz);
        String[] fieldsName = CollectionUtils.toStringArray(graphEdgeType.getAllFields());
        EdgeQuery query = NebulaEdgeQuery.build().getEdgeForQuery(edgeClazz, fromVertexId, toVertexId).yield(edgeClazz, fieldsName);
        return executeQuery(query, edgeClazz);
    }

    @Override
    public <T> Set<T> getEdgeSet(Class<T> edgeClazz, List<String> fromVertexIdAndToVertexIdList) {
        if (CollectionUtils.isEmpty(fromVertexIdAndToVertexIdList)) {
            return Collections.emptySet();
        }
        fromVertexIdAndToVertexIdList = fromVertexIdAndToVertexIdList.parallelStream().map(str -> {
            if (!str.contains("null")) {
                return str;
            }
            return null;
        }).filter(Objects::nonNull).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(fromVertexIdAndToVertexIdList)) {
            return Collections.emptySet();
        }
        GraphEdgeType<Object, Object, T> graphEdgeType = graphTypeManager.getGraphEdgeType(edgeClazz);
        String[] fieldsName = CollectionUtils.toStringArray(graphEdgeType.getAllFields());
        int size = 1000;
        List<List<String>> list = new ArrayList<>();
        if (fromVertexIdAndToVertexIdList.size() <= size) {
            EdgeQuery query = NebulaEdgeQuery.build().getEdgeSetForQuery(edgeClazz, fromVertexIdAndToVertexIdList).yield(edgeClazz, fieldsName);
            return executeQuery(query, edgeClazz);
        } else {
            List<String> finalFromVertexIdAndToVertexIdList = fromVertexIdAndToVertexIdList;
            Stream.iterate(0, n -> n + 1).limit((fromVertexIdAndToVertexIdList.size() / size) + 1).forEach(i -> {
                List<String> subArticle = finalFromVertexIdAndToVertexIdList.stream().skip(i * size).limit(size).collect(Collectors.toList());
                if (subArticle.size() > 0) {
                    list.add(subArticle);
                }
            });
        }
        Set<T> set = new HashSet<>();
        long start = System.currentTimeMillis();
        ThreadPoolExecutor threadPool = rtsThread.getThreadPoolExecutor();
        //定义多线程数量，一般就是任务数量
        final CountDownLatch latchQueryAccountTag = new CountDownLatch(list.size());
        for (List<String> key : list) {
            threadPool.execute(new QueryEdgeSet(latchQueryAccountTag, key, edgeClazz, fieldsName, set));
        }
        try {
            latchQueryAccountTag.await();//等待,不断检测数量是否为0，为零是执行后面的操作
        } catch (InterruptedException e) {
            log.error("QueryEdgeSet " + e.getMessage());
        }
        log.info("QueryEdgeSet 多线程 time =" + (System.currentTimeMillis() - start));
        return set;
//        EdgeQuery query = NebulaEdgeQuery.build().getEdgeSetForQuery(edgeClazz, fromVertexIdAndToVertexIdList).yield(edgeClazz, fieldsName);
//        return executeQuery(query, edgeClazz);
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    class QueryEdgeSet extends Thread {
        private CountDownLatch latch;
        private List<String> key;
        private Class edgeClazz;
        private String[] fieldsName;
        private Set set;


        public QueryEdgeSet(CountDownLatch latch, List<String> key, Class edgeClazz, String[] fieldsName, Set set) {
            this.latch = latch;
            this.key = key;
            this.edgeClazz = edgeClazz;
            this.fieldsName = fieldsName;
            this.set = set;
        }

        @Override
        public void run() {

            try {
                long start = System.currentTimeMillis();
                EdgeQuery query = NebulaEdgeQuery.build().getEdgeSetForQuery(edgeClazz, key).yield(edgeClazz, fieldsName);
                set.addAll(executeQuery(query, edgeClazz));
                log.info("QueryEdgeSet  time =" + (System.currentTimeMillis() - start));
            } catch (Exception e) {
                log.error("QueryEdgeSet " + e.getMessage());
            } finally {
                latch.countDown();
            }
        }
    }

    @Override
    public <T> Set<T> getEdgeByParam(Class<T> edgeClazz, Map<String, String> paramMap) {
        if (paramMap.size() == 0) {
            return Collections.emptySet();
        }
        GraphEdgeType<Object, Object, T> graphEdgeType = graphTypeManager.getGraphEdgeType(edgeClazz);
        String[] fieldsName = CollectionUtils.toStringArray(graphEdgeType.getAllFields());
        EdgeQuery query = NebulaEdgeQuery.build().getEdgeByParam(edgeClazz, paramMap).yield(edgeClazz, fieldsName);
        return executeQuery(query, edgeClazz);
    }

    @Override
    public <T> Set<T> getEdgeByStr(Class<T> edgeClazz, String str) {
        if (str.length() == 0) {
            return Collections.emptySet();
        }
        GraphEdgeType<Object, Object, T> graphEdgeType = graphTypeManager.getGraphEdgeType(edgeClazz);
        String[] fieldsName = CollectionUtils.toStringArray(graphEdgeType.getAllFields());
        EdgeQuery query = NebulaEdgeQuery.build().getEdgeByStr(edgeClazz, str).yield(edgeClazz, fieldsName);
        return executeQuery(query, edgeClazz);
    }

    @Override
    public <T> Set<T> goReverseEdge(Class<T> edgeClazz, String... vertexIds) {
        GraphEdgeType<Object, Object, T> graphEdgeType = graphTypeManager.getGraphEdgeType(edgeClazz);
        String[] fieldsName = CollectionUtils.toStringArray(graphEdgeType.getAllFields());
        EdgeQuery query = NebulaEdgeQuery.build().goFrom(edgeClazz, EdgeDirectionEnum.REVERSELY, vertexIds).yield(edgeClazz, fieldsName);

        return executeQuery(query, edgeClazz);
    }

    @Override
    public <T> Set<T> goAllEdge(Class<T> edgeClazz, String param, String[] fieldsName, String... vertexIds) {
        GraphEdgeType<Object, Object, T> graphEdgeType = graphTypeManager.getGraphEdgeType(edgeClazz);
        if (fieldsName == null || fieldsName.length == 0) {
            fieldsName = CollectionUtils.toStringArray(graphEdgeType.getAllFields());
        }
        EdgeQuery query = NebulaEdgeQuery.build().goFrom(edgeClazz, EdgeDirectionEnum.BIDIRECT, vertexIds).addParam(param).yield(edgeClazz, fieldsName);
        return executeQuery(query, edgeClazz);
    }

    @Override
    public <T> Set<T> fetchVertexTag(Class<T> vertexClazz, Set<String> vertexIds, String[] fieldsName) {
        if (CollectionUtils.isEmpty(vertexIds)) {
            return Collections.emptySet();
        }
        GraphVertexType<T> graphVertexType = graphTypeManager.getGraphVertexType(vertexClazz);
        if (fieldsName == null || fieldsName.length == 0) {
            fieldsName = CollectionUtils.toStringArray(graphVertexType.getAllFields());
        }
        int size = 1000;
        List<Set<String>> list = new ArrayList<>();
        if (vertexIds.size() <= size) {
            VertexQuery query = NebulaVertexQuery.build().fetchPropOn(vertexClazz, vertexIds).yield(vertexClazz, fieldsName);
            return executeQuery(query, vertexClazz);
        } else {
            Stream.iterate(0, n -> n + 1).limit((vertexIds.size() / size) + 1).forEach(i -> {
                Set<String> subArticle = vertexIds.stream().skip(i * size).limit(size).collect(Collectors.toSet());
                if (subArticle.size() > 0) {
                    list.add(subArticle);
                }
            });
        }
        Set<T> set = new HashSet<>();
        long start = System.currentTimeMillis();
        ThreadPoolExecutor threadPool = rtsThread.getThreadPoolExecutor();
        //定义多线程数量，一般就是任务数量
        final CountDownLatch latch = new CountDownLatch(list.size());
        for (Set<String> key : list) {
            threadPool.execute(new QueryTagSet(latch, key, vertexClazz, fieldsName, set));
        }
        try {
            latch.await();//等待,不断检测数量是否为0，为零是执行后面的操作
        } catch (InterruptedException e) {
            log.error("QueryTagSet " + e.getMessage());
        }
        log.info("QueryTagSet 多线程 time =" + (System.currentTimeMillis() - start));
        return set;
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    class QueryTagSet extends Thread {
        private CountDownLatch latch;
        private Set<String> key;
        private Class vertexClazz;
        private String[] fieldsName;
        private Set set;


        public QueryTagSet(CountDownLatch latch, Set<String> key, Class vertexClazz, String[] fieldsName, Set set) {
            this.latch = latch;
            this.key = key;
            this.vertexClazz = vertexClazz;
            this.fieldsName = fieldsName;
            this.set = set;
        }

        @Override
        public void run() {

            try {
                long start = System.currentTimeMillis();
                VertexQuery query = NebulaVertexQuery.build().fetchPropOn(vertexClazz, key).yield(vertexClazz, fieldsName);
                set.addAll(executeQuery(query, vertexClazz));
                log.info("QueryTagSet  time =" + (System.currentTimeMillis() - start));
            } catch (Exception e) {
                log.error("QueryTagSet " + e.getMessage());
            } finally {
                latch.countDown();
            }
        }
    }

    @Override
    public <T> Set<T> getTagByParam(Class<T> vertexClazz, String param, String[] fieldsName) {
        GraphVertexType<T> graphVertexType = graphTypeManager.getGraphVertexType(vertexClazz);
        if (fieldsName == null || fieldsName.length == 0) {
            fieldsName = CollectionUtils.toStringArray(graphVertexType.getAllFields());
        }
        VertexQuery query = NebulaVertexQuery.build().getTagByParam(vertexClazz, param).yield(vertexClazz, fieldsName);
        return executeQuery(query, vertexClazz);
    }

    @Override
    public <T> Set<String> findShortestPath(Class<T> edgeClazz, String fromVertexId, String toVertexId, Map<String, String> paramMap) {
        EdgeQuery query = NebulaEdgeQuery.build().findShortestPath(edgeClazz, fromVertexId, toVertexId, paramMap);
        ResultSet resultSet = executeQuerySqlForResultSet(query.buildSql());
        System.out.println(resultSet.toString());
        String res = resultSet.toString();
        res = res.substring(24, res.length() - 1);
        if (!res.contains("-")) {
            return Collections.emptySet();
        }
        res = res.replaceAll("\\{}", "").replaceAll("-\\[:SocialAccountRelationInfo@0\\]-", "")
                .replaceAll("-\\[:SocialAccountPostEdge@0\\]-", "").replaceAll("\"", "").replaceAll(" ", "");
        res = res.replace(Matcher.quoteReplacement("("), "").replace(Matcher.quoteReplacement(")"), "");
        String[] split = res.split(",");
//        res = regex(res);
        Set<String> fromIdAndToIdset = new HashSet<>();
        for (int i = 0; i < split.length; i++) {
            String str = split[i];
            for (int j = 0; j < str.length() - 33; j = j + 33) {
                fromIdAndToIdset.add(str.substring(j, j + 65));
            }
        }
        return fromIdAndToIdset;
    }

    public static String regex(String str) {
        String replaceAll = null;
        String ps = "'([\\da-z]{32})";
        Pattern p = Pattern.compile(ps);
        Matcher m = p.matcher(str);
        if (m.find()) {
            replaceAll = m.group(1);
        }
        return replaceAll;
    }
}
