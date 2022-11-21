
package com.aliencat.springboot.nebula.ocean.engine;

import com.google.common.collect.Lists;
import com.aliencat.springboot.nebula.ocean.common.GraphHelper;
import com.aliencat.springboot.nebula.ocean.common.utils.StringUtil;
import com.aliencat.springboot.nebula.ocean.dao.EdgeUpdateEngine;
import com.aliencat.springboot.nebula.ocean.domain.GraphLabel;
import com.aliencat.springboot.nebula.ocean.domain.impl.GraphEdgeEntity;
import com.aliencat.springboot.nebula.ocean.domain.impl.GraphEdgeType;
import com.aliencat.springboot.nebula.ocean.domain.impl.GraphVertexEntity;
import com.aliencat.springboot.nebula.ocean.domain.impl.GraphVertexType;
import com.aliencat.springboot.nebula.ocean.enums.ErrorEnum;
import com.aliencat.springboot.nebula.ocean.enums.GraphDataTypeEnum;
import com.aliencat.springboot.nebula.ocean.exception.CheckThrower;
import com.aliencat.springboot.nebula.ocean.exception.NebulaException;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 批量边更新引擎
 *
 * @author Anyzm
 * @date 2020/3/30
 */
public class NebulaBatchEdgesUpdate<S, T, E> implements EdgeUpdateEngine<S, T, E> {

    private static final String UPSET_SQL_FORMAT = "UPSERT EDGE %s->%s of %s SET %s";

    private static final String EDGE_INSERT_SQL = "INSERT EDGE  %s VALUES %s";

    /**
     * 仅生成边的更新sql
     */
    private boolean isOnlyGenerateEdgeSql = true;

    private List<GraphEdgeEntity<S, T, E>> graphEdgeEntities;

    private GraphVertexType<S> srcGraphVertexType;

    private GraphVertexType<T> dstGraphVertexType;

    private List<GraphVertexEntity<S>> srcGraphVertexEntities;

    private List<GraphVertexEntity<T>> dstGraphVertexEntities;

    public NebulaBatchEdgesUpdate(List<GraphEdgeEntity<S, T, E>> graphEdgeEntities) throws NebulaException {
        this.graphEdgeEntities = graphEdgeEntities;
        CheckThrower.ifTrueThrow(CollectionUtils.isEmpty(graphEdgeEntities), ErrorEnum.UPDATE_FIELD_DATA_NOT_EMPTY);
        this.srcGraphVertexType = graphEdgeEntities.get(0).getSrcVertexType();
        this.dstGraphVertexType = graphEdgeEntities.get(0).getDstVertexType();
        this.isOnlyGenerateEdgeSql = true;
    }

    public NebulaBatchEdgesUpdate(List<GraphEdgeEntity<S, T, E>> graphEdgeEntities, List<GraphVertexEntity<S>> srcGraphVertexEntities,
                                  List<GraphVertexEntity<T>> dstGraphVertexEntities) throws NebulaException {
        this.graphEdgeEntities = graphEdgeEntities;
        CheckThrower.ifTrueThrow(CollectionUtils.isEmpty(graphEdgeEntities), ErrorEnum.UPDATE_FIELD_DATA_NOT_EMPTY);
        this.srcGraphVertexEntities = srcGraphVertexEntities;
        this.dstGraphVertexEntities = dstGraphVertexEntities;
        this.srcGraphVertexType = graphEdgeEntities.get(0).getSrcVertexType();
        this.dstGraphVertexType = graphEdgeEntities.get(0).getDstVertexType();
        this.isOnlyGenerateEdgeSql = false;
    }


    private List<String> getDstVertexSql() throws NebulaException {
        if (!CollectionUtils.isEmpty(dstGraphVertexEntities)) {
            NebulaBatchVertexUpdate nebulaUpdateBatchVertex = new NebulaBatchVertexUpdate(dstGraphVertexEntities);
            return nebulaUpdateBatchVertex.getSqlList();
        }
        return Collections.emptyList();
    }

    private List<String> getSrcVertexSql() throws NebulaException {
        if (!CollectionUtils.isEmpty(this.srcGraphVertexEntities)) {
            NebulaBatchVertexUpdate nebulaUpdateBatchVertex = new NebulaBatchVertexUpdate(this.srcGraphVertexEntities);
            return nebulaUpdateBatchVertex.getSqlList();
        }
        return Collections.emptyList();
    }


    @Override
    public List<String> getSqlList() throws NebulaException {
        List<String> sqlList = getEdgeSql();
        if (isOnlyGenerateEdgeSql) {
            return sqlList;
        }
        sqlList.addAll(this.getSrcVertexSql());
        sqlList.addAll(this.getDstVertexSql());
        return sqlList;
    }

    private List<String> getEdgeSql() throws NebulaException {
//        if (this.graphEdgeEntities.size() == 1) {
        String sql = getOneSql();
        return Lists.newArrayList(sql);
//        }
//        return getMultiSql();
    }

    /**
     * 获取单边sql
     *
     * @return
     */
    private String getOneSql() throws NebulaException {
        return generateInsertSql(this.graphEdgeEntities);
    }

    /**
     * 获取多边sql
     *
     * @return
     */
    private List<String> getMultiSql() throws NebulaException {
        List<String> sqlList = Lists.newArrayListWithExpectedSize(this.graphEdgeEntities.size());
        for (GraphEdgeEntity<S, T, E> graphEdgeEntity : this.graphEdgeEntities) {
            String sql = generateSql(graphEdgeEntity);
            sqlList.add(sql);
        }
        return StringUtil.aggregate(sqlList, sqlList.size(), ";");
    }

    private String generateSql(GraphEdgeEntity<S, T, E> graphEdgeEntity) throws NebulaException {
        String src = GraphHelper.getQuerySrcId(graphEdgeEntity.getGraphEdgeType(), graphEdgeEntity.getSrcId());
        String end = GraphHelper.getQueryDstId(graphEdgeEntity.getGraphEdgeType(), graphEdgeEntity.getDstId());
        Set<Map.Entry<String, Object>> entries = graphEdgeEntity.getProps().entrySet();
        StringBuilder sqlBuilder = new StringBuilder();
        Map<String, GraphDataTypeEnum> dataTypeMap = graphEdgeEntity.getGraphEdgeType().getDataTypeMap();
        for (Map.Entry<String, Object> entry : entries) {
            GraphDataTypeEnum graphDataTypeEnum = dataTypeMap.get(entry.getKey());
            if (GraphDataTypeEnum.STRING.equals(graphDataTypeEnum)) {
                sqlBuilder.append(",").append(entry.getKey()).append("=\"").append(entry.getValue()).append("\"");
            } else {
                sqlBuilder.append(",").append(entry.getKey()).append("=").append(entry.getValue());
            }
        }
        String sqlFieldSet = sqlBuilder.delete(0, 1).toString();
        return String.format(UPSET_SQL_FORMAT, src, end, graphEdgeEntity.getGraphEdgeType().getEdgeName(), sqlFieldSet);
    }

    private String generateInsertSql(List<GraphEdgeEntity<S, T, E>> graphEdgeEntities) throws NebulaException {
        // 边名称和字段
//        this.graphVertexType.getVertexName() + "  ("
        StringBuilder tableAndFields = new StringBuilder(this.getGraphEdgeType().getEdgeName() + "  (");
        AtomicInteger tableNum = new AtomicInteger();
        StringBuilder values = new StringBuilder();
        graphEdgeEntities.stream().forEach(graphEdgeEntity -> {
            String src = GraphHelper.getQuerySrcId(graphEdgeEntity.getGraphEdgeType(), graphEdgeEntity.getSrcId());
            String end = GraphHelper.getQueryDstId(graphEdgeEntity.getGraphEdgeType(), graphEdgeEntity.getDstId());
//            String id = GraphHelper.getQueryId(this.graphVertexType, graphVertexEntity.getId());
            values.append(src + " -> " + end + " : (");
            Map<String, Object> valueMap = graphEdgeEntity.getProps();
            Map<String, GraphDataTypeEnum> dataTypeMap = graphEdgeEntity.getGraphEdgeType().getDataTypeMap();
            List<String> mustFields = graphEdgeEntity.getGraphEdgeType().getMustFields();
            mustFields.stream().forEach(field -> {
                if (!(field.equals("fromId") || field.equals("toId"))) {
                    if (tableNum.get() == 0) {
                        tableAndFields.append(field + ",");
                    }
                    GraphDataTypeEnum graphDataTypeEnum = dataTypeMap.get(field);
                    if (GraphDataTypeEnum.STRING.equals(graphDataTypeEnum)) {
                        values.append("\"").append(valueMap.get(field)).append("\" ,");
                    } else {
                        values.append(valueMap.get(field)).append(",");
                    }
                }
            });
            values.append(") ,");
            tableNum.getAndIncrement();
        });
//        tableAndFields.append(")");
        String tableAndFieldsStr = tableAndFields.toString();
        tableAndFieldsStr = tableAndFieldsStr.substring(0, tableAndFields.length() - 1);
        tableAndFieldsStr = tableAndFieldsStr + ")";

        String valuesStr = values.toString().replace(",)", ")");
        valuesStr = valuesStr.substring(0, valuesStr.length() - 1);
        return String.format(EDGE_INSERT_SQL, tableAndFieldsStr, valuesStr);
    }

    @Override
    public List<GraphEdgeEntity<S, T, E>> getGraphEdgeEntityList() {
        return this.graphEdgeEntities;
    }

    @Override
    public GraphEdgeType<S, T, E> getGraphEdgeType() {
        return this.graphEdgeEntities.get(0).getGraphEdgeType();
    }

    @Override
    public List<GraphLabel> getLabels() {
        List<GraphLabel> list = Lists.newArrayList();
        GraphEdgeType<S, T, E> graphEdgeType = this.getGraphEdgeType();
        list.add(graphEdgeType);
        list.add(graphEdgeType.getSrcVertexType());
        list.add(graphEdgeType.getDstVertexType());
        return list;
    }

}
