
package com.aliencat.springboot.nebula.ocean.engine;


import com.aliencat.springboot.nebula.ocean.common.GraphHelper;
import com.aliencat.springboot.nebula.ocean.common.utils.CollectionUtils;
import com.aliencat.springboot.nebula.ocean.common.utils.StringUtil;
import com.aliencat.springboot.nebula.ocean.dao.GraphTypeManager;
import com.aliencat.springboot.nebula.ocean.domain.EdgeQuery;
import com.aliencat.springboot.nebula.ocean.domain.GraphCondition;
import com.aliencat.springboot.nebula.ocean.domain.GraphExpression;
import com.aliencat.springboot.nebula.ocean.domain.GraphQuery;
import com.aliencat.springboot.nebula.ocean.domain.impl.GraphEdgeType;
import com.aliencat.springboot.nebula.ocean.enums.EdgeDirectionEnum;
import com.aliencat.springboot.nebula.ocean.enums.GraphDataTypeEnum;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Description  NebulaEdgeQuery is used for
 *
 * @author Anyzm
 * Date  2021/8/10 - 17:46
 * @version 1.0.0
 */
public class NebulaEdgeQuery implements EdgeQuery {

    @Getter
    @Setter
    private static GraphTypeManager graphTypeManager;

    private StringBuilder sqlBuilder = new StringBuilder();

    private NebulaEdgeQuery() {
    }

    public static NebulaEdgeQuery build() {
        return new NebulaEdgeQuery();
    }

    private void goFromOnly() {
        sqlBuilder.append("go from ");
    }

    private void appendEdgeVertexId(Class clazz, boolean isReverse, String... vertexIds) {
        GraphEdgeType graphEdgeType = graphTypeManager.getGraphEdgeType(clazz);
        String edgeName = graphEdgeType.getEdgeName();
        if (isReverse) {
            NebulaQueryUtils.appendVertexDstId(graphEdgeType, sqlBuilder, vertexIds);
        } else {
            NebulaQueryUtils.appendVertexSrcId(graphEdgeType, sqlBuilder, vertexIds);
        }
        sqlBuilder.deleteCharAt(sqlBuilder.length() - 1);
        sqlBuilder.append(" over ").append(edgeName);
    }

    private void appendSrcVertexId(Class clazz, String... vertexIds) {
        appendEdgeVertexId(clazz, false, vertexIds);
    }

    private void appendDstVertexId(Class clazz, String... vertexIds) {
        appendEdgeVertexId(clazz, true, vertexIds);
    }

    private void goFromOnly(Integer fromSteps, Integer toSteps) {
        sqlBuilder.append("go ");
        if (fromSteps != null && fromSteps >= 0) {
            sqlBuilder.append(fromSteps);
        }
        if (toSteps != null && toSteps >= 0) {
            sqlBuilder.append(" to ").append(toSteps);
        }
        sqlBuilder.append(" STEPS from ");
    }

    @Override
    public EdgeQuery goFrom(Class clazz, String... vertexIds) {
        goFromOnly();
        appendSrcVertexId(clazz, vertexIds);
        return this;
    }

    @Override
    public EdgeQuery getEdgeForQuery(Class clazz, String fromVertexId, String toVertexId) {
        GraphEdgeType graphEdgeType = graphTypeManager.getGraphEdgeType(clazz);
        String edgeName = graphEdgeType.getEdgeName();
        sqlBuilder.append("fetch prop on ").append(edgeName);

        String src = GraphHelper.getQuerySrcId(graphEdgeType, fromVertexId);
        String end = GraphHelper.getQueryDstId(graphEdgeType, toVertexId);
//            String id = GraphHelper.getQueryId(this.graphVertexType, graphVertexEntity.getId());
        sqlBuilder.append(src + " -> " + end);
//        NebulaQueryUtils.appendVertexId(graphEdgeType, sqlBuilder, vertexIds);
        sqlBuilder.deleteCharAt(sqlBuilder.length() - 1);
        return this;
//        return null;
    }

    @Override
    public EdgeQuery getEdgeSetForQuery(Class clazz, List<String> fromVertexIdAndToVertexIdList) {
        GraphEdgeType graphEdgeType = graphTypeManager.getGraphEdgeType(clazz);
        String edgeName = graphEdgeType.getEdgeName();
        sqlBuilder.append("fetch prop on ").append(edgeName);
        fromVertexIdAndToVertexIdList.forEach(fromVertexIdAndToVertexId -> sqlBuilder.append(fromVertexIdAndToVertexId).append(","));
        sqlBuilder.deleteCharAt(sqlBuilder.length() - 1);
        return this;
    }

    @Override
    public EdgeQuery getEdgeByParam(Class clazz, Map<String, String> paramMap) {
        GraphEdgeType graphEdgeType = graphTypeManager.getGraphEdgeType(clazz);
        String edgeName = graphEdgeType.getEdgeName();
        sqlBuilder.append("LOOKUP ON ").append(edgeName).append(" WHERE ");
        Map<String, GraphDataTypeEnum> dataTypeMap = graphEdgeType.getDataTypeMap();
        paramMap.keySet().stream().forEach(field -> {
            GraphDataTypeEnum graphDataTypeEnum = dataTypeMap.get(field);
            if (GraphDataTypeEnum.STRING.equals(graphDataTypeEnum)) {
                sqlBuilder.append(edgeName + "." + field).append("== \"").append(paramMap.get(field)).append("\"  AND");
            } else {
                sqlBuilder.append(edgeName + "." + field).append("== ").append(paramMap.get(field)).append("  AND");
            }
        });

        sqlBuilder.delete(sqlBuilder.length() - 3, sqlBuilder.length());
//        sqlBuilder.deleteCharAt(sqlBuilder.length() - 9);
        return this;
    }

    @Override
    public EdgeQuery getEdgeByStr(Class clazz, String str) {
        GraphEdgeType graphEdgeType = graphTypeManager.getGraphEdgeType(clazz);
        String edgeName = graphEdgeType.getEdgeName();
        sqlBuilder.append("LOOKUP ON ").append(edgeName).append(" WHERE ");
        sqlBuilder.append(str);
        return this;
    }

    @Override
    public EdgeQuery findShortestPath(Class clazz, String fromVertexId, String toVertexId, Map<String, String> paramMap) {
        sqlBuilder.append("FIND SHORTEST PATH  FROM ");
        sqlBuilder.append("  \"" + fromVertexId + "\"  TO  \"" + toVertexId + "\" OVER ");
        if (Objects.nonNull(clazz)) {
            GraphEdgeType graphEdgeType = graphTypeManager.getGraphEdgeType(clazz);
            String edgeName = graphEdgeType.getEdgeName();
            Map<String, GraphDataTypeEnum> dataTypeMap = graphEdgeType.getDataTypeMap();
            sqlBuilder.append(edgeName).append(" BIDIRECT  ");
            if (!CollectionUtils.isEmpty(paramMap)) {
                sqlBuilder.append(" WHERE ");
                paramMap.keySet().stream().forEach(field -> {
                    GraphDataTypeEnum graphDataTypeEnum = dataTypeMap.get(field);
                    if (GraphDataTypeEnum.STRING.equals(graphDataTypeEnum)) {
                        sqlBuilder.append(edgeName + "." + field).append(">= \"").append(paramMap.get(field)).append("\"  AND");
                    } else if (GraphDataTypeEnum.BOOL.equals(graphDataTypeEnum)) {
                        sqlBuilder.append(edgeName + "." + field).append("== ").append(paramMap.get(field)).append(" AND");
                    } else {
                        sqlBuilder.append(edgeName + "." + field).append(">= ").append(paramMap.get(field)).append("  AND");
                    }
                });
                sqlBuilder.delete(sqlBuilder.length() - 3, sqlBuilder.length());
            }
        } else {
            sqlBuilder.append(" * BIDIRECT  ");
        }

        sqlBuilder.append("YIELD path AS p");
//        sqlBuilder.deleteCharAt(sqlBuilder.length() - 9);
        return this;
    }

    @Override
    public EdgeQuery goFrom(Class clazz, EdgeDirectionEnum directionEnum, String... vertexIds) {
        if (EdgeDirectionEnum.REVERSELY.equals(directionEnum)) {
            goFromOnly();
            appendDstVertexId(clazz, vertexIds);
        } else {
            goFrom(clazz, vertexIds);
        }
        sqlBuilder.append(" ").append(directionEnum.getWord());
        return this;
    }

    @Override
    public EdgeQuery addParam(String param) {
        if (!StringUtil.isEmpty(param)) {
            sqlBuilder.append(param);
        }
        return this;
    }

    @Override
    public EdgeQuery goFromSteps(Class clazz, int steps, String... vertexIds) {
        goFromOnly(steps, null);
        appendSrcVertexId(clazz, vertexIds);
        return this;
    }

    @Override
    public EdgeQuery goFromSteps(Class clazz, int fromSteps, int toSteps, String... vertexIds) {
        goFromOnly(fromSteps, toSteps);
        appendSrcVertexId(clazz, vertexIds);
        return this;
    }

    @Override
    public EdgeQuery goFromSteps(Class clazz, EdgeDirectionEnum directionEnum, int steps, String... vertexIds) {
        if (EdgeDirectionEnum.REVERSELY.equals(directionEnum)) {
            goFromOnly(steps, null);
            appendDstVertexId(clazz, vertexIds);
        } else {
            goFromSteps(clazz, steps, vertexIds);
        }
        sqlBuilder.append(" ").append(directionEnum.getWord());
        return this;
    }

    @Override
    public EdgeQuery goFromSteps(Class clazz, EdgeDirectionEnum directionEnum, int fromSteps, int toSteps, String... vertexIds) {
        if (EdgeDirectionEnum.REVERSELY.equals(directionEnum)) {
            goFromOnly(fromSteps, toSteps);
            appendDstVertexId(clazz, vertexIds);
        } else {
            goFromSteps(clazz, fromSteps, toSteps, vertexIds);
        }
        sqlBuilder.append(" ").append(directionEnum.getWord());
        return this;
    }

    @Override
    public EdgeQuery connectAdd(GraphQuery graphQuery) {
        sqlBuilder.append(graphQuery.buildSql());
        return this;
    }

    @Override
    public String buildSql() {
        return sqlBuilder.toString();
    }

    @Override
    public EdgeQuery limit(int size) {
        NebulaQueryUtils.limit(sqlBuilder, size);
        return this;
    }

    @Override
    public EdgeQuery limit(int offset, int size) {
        NebulaQueryUtils.limit(sqlBuilder, offset, size);
        return this;
    }

    @Override
    public EdgeQuery distinct() {
        NebulaQueryUtils.distinct(sqlBuilder);
        return this;
    }

    @Override
    public EdgeQuery yield() {
        sqlBuilder.append(" yield ");
        return this;
    }

    @Override
    public EdgeQuery yield(Class clazz, String... fields) {
        NebulaQueryUtils.yield(graphTypeManager, sqlBuilder, clazz, fields);
        return this;
    }

    @Override
    public EdgeQuery yield(String symbol, Class clazz, String... fields) {
        NebulaQueryUtils.yield(graphTypeManager, sqlBuilder, symbol, clazz, fields);
        return this;
    }

    @Override
    public EdgeQuery yield(String... fields) {
        NebulaQueryUtils.yield(sqlBuilder, fields);
        return this;
    }

    @Override
    public EdgeQuery yield(Map<String, String> fieldAlias) {
        NebulaQueryUtils.yield(sqlBuilder, fieldAlias);
        return this;
    }

    @Override
    public EdgeQuery yieldDistinct(Class clazz, String... fields) {
        NebulaQueryUtils.yieldDistinct(graphTypeManager, sqlBuilder, clazz, fields);
        return this;
    }

    @Override
    public EdgeQuery yieldDistinct(String prefix, Class clazz, String... fields) {
        NebulaQueryUtils.yieldDistinct(graphTypeManager, sqlBuilder, prefix, clazz, fields);
        return this;
    }

    @Override
    public EdgeQuery yieldDistinct(String... fields) {
        NebulaQueryUtils.yieldDistinct(sqlBuilder, fields);
        return this;
    }

    @Override
    public EdgeQuery yieldDistinct(Map<String, String> fieldAlias) {
        NebulaQueryUtils.yieldDistinct(sqlBuilder, fieldAlias);
        return this;
    }

    @Override
    public EdgeQuery pipe() {
        NebulaQueryUtils.pipe(sqlBuilder);
        return this;
    }

    @Override
    public GraphQuery unionAll(GraphQuery graphQuery) {
        NebulaQueryUtils.unionAll(sqlBuilder, graphQuery);
        return this;
    }

    @Override
    public GraphQuery union(GraphQuery graphQuery) {
        NebulaQueryUtils.union(sqlBuilder, graphQuery);
        return this;
    }

    @Override
    public EdgeQuery groupBy(Class clazz, String... fields) {
        NebulaQueryUtils.groupBy(graphTypeManager, sqlBuilder, clazz, fields);
        return this;
    }

    @Override
    public EdgeQuery groupBy(String... fields) {
        NebulaQueryUtils.groupBy(sqlBuilder, fields);
        return this;
    }

    @Override
    public EdgeQuery countComma(String alias) {
        NebulaQueryUtils.countComma(sqlBuilder, alias);
        return this;
    }

    @Override
    public EdgeQuery countComma(String field, String alias) {
        NebulaQueryUtils.countComma(sqlBuilder, field, alias);
        return this;
    }

    @Override
    public EdgeQuery countComma(Class clazz, String field, String alias) {
        NebulaQueryUtils.countComma(graphTypeManager, sqlBuilder, clazz, field, alias);
        return this;
    }

    @Override
    public EdgeQuery countComma(GraphExpression graphExpression, String alias) {
        NebulaQueryUtils.countComma(sqlBuilder, graphExpression, alias);
        return this;
    }

    @Override
    public EdgeQuery count(String field, String alias) {
        NebulaQueryUtils.count(sqlBuilder, field, alias);
        return this;
    }

    @Override
    public EdgeQuery count(Map<String, String> fieldAlias) {
        NebulaQueryUtils.count(sqlBuilder, fieldAlias);
        return this;
    }

    @Override
    public EdgeQuery count(String alias) {
        NebulaQueryUtils.count(sqlBuilder, alias);
        return this;
    }

    @Override
    public EdgeQuery count(Class clazz, String field, String alias) {
        NebulaQueryUtils.count(graphTypeManager, sqlBuilder, clazz, field, alias);
        return this;
    }

    @Override
    public EdgeQuery count(GraphExpression graphExpression, String alias) {
        NebulaQueryUtils.count(sqlBuilder, graphExpression, alias);
        return this;
    }

    @Override
    public EdgeQuery avg(GraphExpression graphExpression, String alias) {
        NebulaQueryUtils.avg(sqlBuilder, graphExpression, alias);
        return this;
    }

    @Override
    public EdgeQuery avg(String field, String alias) {
        NebulaQueryUtils.avg(sqlBuilder, field, alias);
        return this;
    }

    @Override
    public EdgeQuery avg(Class clazz, String field, String alias) {
        NebulaQueryUtils.avg(graphTypeManager, sqlBuilder, clazz, field, alias);
        return this;
    }

    @Override
    public EdgeQuery avgComma(GraphExpression graphExpression, String alias) {
        NebulaQueryUtils.avgComma(sqlBuilder, graphExpression, alias);
        return this;
    }

    @Override
    public EdgeQuery avgComma(String field, String alias) {
        NebulaQueryUtils.avgComma(sqlBuilder, field, alias);
        return this;
    }

    @Override
    public EdgeQuery avgComma(Class clazz, String field, String alias) {
        NebulaQueryUtils.avgComma(graphTypeManager, sqlBuilder, clazz, field, alias);
        return this;
    }

    @Override
    public EdgeQuery sum(GraphExpression graphExpression, String alias) {
        NebulaQueryUtils.sum(sqlBuilder, graphExpression, alias);
        return this;
    }

    @Override
    public EdgeQuery sum(String field, String alias) {
        NebulaQueryUtils.sum(sqlBuilder, field, alias);
        return this;
    }

    @Override
    public EdgeQuery sum(Class clazz, String field, String alias) {
        NebulaQueryUtils.sum(graphTypeManager, sqlBuilder, clazz, field, alias);
        return this;
    }

    @Override
    public EdgeQuery sumComma(GraphExpression graphExpression, String alias) {
        NebulaQueryUtils.sumComma(sqlBuilder, graphExpression, alias);
        return this;
    }

    @Override
    public EdgeQuery sumComma(String field, String alias) {
        NebulaQueryUtils.sumComma(sqlBuilder, field, alias);
        return this;
    }

    @Override
    public EdgeQuery sumComma(Class clazz, String field, String alias) {
        NebulaQueryUtils.sumComma(graphTypeManager, sqlBuilder, clazz, field, alias);
        return this;
    }

    @Override
    public EdgeQuery comma() {
        NebulaQueryUtils.comma(sqlBuilder);
        return this;
    }

    @Override
    public EdgeQuery where(GraphCondition graphCondition) {
        NebulaQueryUtils.where(sqlBuilder, graphCondition);
        return this;
    }


}
