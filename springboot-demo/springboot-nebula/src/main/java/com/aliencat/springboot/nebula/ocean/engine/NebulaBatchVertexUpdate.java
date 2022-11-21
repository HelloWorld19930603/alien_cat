
package com.aliencat.springboot.nebula.ocean.engine;


import com.google.common.collect.Lists;
import com.aliencat.springboot.nebula.ocean.common.GraphHelper;
import com.aliencat.springboot.nebula.ocean.common.utils.StringUtil;
import com.aliencat.springboot.nebula.ocean.dao.VertexUpdateEngine;
import com.aliencat.springboot.nebula.ocean.domain.GraphLabel;
import com.aliencat.springboot.nebula.ocean.domain.impl.GraphVertexEntity;
import com.aliencat.springboot.nebula.ocean.domain.impl.GraphVertexType;
import com.aliencat.springboot.nebula.ocean.enums.ErrorEnum;
import com.aliencat.springboot.nebula.ocean.enums.GraphDataTypeEnum;
import com.aliencat.springboot.nebula.ocean.exception.CheckThrower;
import com.aliencat.springboot.nebula.ocean.exception.NebulaException;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 批量顶点更新引擎
 *
 * @author Anyzm
 * @date 2020/4/13
 */
public class NebulaBatchVertexUpdate<T> implements VertexUpdateEngine {

    private static final String VERTEX_UPSET_SQL = "UPSERT VERTEX %s SET %s";

    private static final String VERTEX_INSERT_SQL = "INSERT VERTEX  %s VALUES %s";

    private List<GraphVertexEntity<T>> graphVertexEntities;

    private GraphVertexType<T> graphVertexType;

    private int batchSize;


    /**
     * 构建顶点批量插入
     *
     * @param graphVertexEntities
     */
    public NebulaBatchVertexUpdate(List<GraphVertexEntity<T>> graphVertexEntities) throws NebulaException {
        CheckThrower.ifTrueThrow(CollectionUtils.isEmpty(graphVertexEntities), ErrorEnum.UPDATE_FIELD_DATA_NOT_EMPTY);
        this.graphVertexEntities = graphVertexEntities;
        this.graphVertexType = graphVertexEntities.get(0).getGraphVertexType();
        this.batchSize = graphVertexEntities.size();
    }

    private String getOneVertexSql() throws NebulaException {
        return generateInsertSql(graphVertexEntities);
    }

    private List<String> getMultiVertexSql() throws NebulaException {
        // nebula> UPSERT VERTEX 111 SET player.name = "Dwight Howard", player.age = $^.player.age + 11;
        List<String> sqlList = Lists.newArrayListWithExpectedSize(batchSize);
        for (GraphVertexEntity graphVertexEntity : this.graphVertexEntities) {
            String sql = generateUpsetSql(graphVertexEntity);
            sqlList.add(sql);
        }
        return StringUtil.aggregate(sqlList, batchSize, ";");
    }

    private String generateUpsetSql(GraphVertexEntity graphVertexEntity) throws NebulaException {
        Set<Map.Entry<String, Object>> entries = graphVertexEntity.getProps().entrySet();
        String queryId = GraphHelper.getQueryId(this.graphVertexType, graphVertexEntity.getId());
        StringBuilder builder = new StringBuilder();
        Map<String, GraphDataTypeEnum> dataTypeMap = graphVertexEntity.getGraphVertexType().getDataTypeMap();
        for (Map.Entry<String, Object> entry : entries) {
            GraphDataTypeEnum graphDataTypeEnum = dataTypeMap.get(entry.getKey());
            if (GraphDataTypeEnum.STRING.equals(graphDataTypeEnum)) {
                builder.append(',').append(this.graphVertexType.getVertexName()).append('.')
                        .append(entry.getKey()).append("=\"").append(entry.getValue()).append("\"");
            } else {
                builder.append(',').append(this.graphVertexType.getVertexName()).append('.')
                        .append(entry.getKey()).append("=").append(entry.getValue());
            }
        }
        String sqlSet = builder.delete(0, 1).toString();
        return String.format(VERTEX_INSERT_SQL, queryId, sqlSet);
    }

    private String generateInsertSql(List<GraphVertexEntity<T>> graphVertexEntities) throws NebulaException {
        // 表名称和字段
        StringBuilder tableAndFields = new StringBuilder(this.graphVertexType.getVertexName() + "  (");
        AtomicInteger tableNum = new AtomicInteger();
        StringBuilder values = new StringBuilder();
        graphVertexEntities.stream().forEach(graphVertexEntity -> {
            String id = GraphHelper.getQueryId(this.graphVertexType, graphVertexEntity.getId());
            if (Objects.nonNull(id)) {
                values.append(id + " : (");
                Map<String, Object> valueMap = graphVertexEntity.getProps();
                Map<String, GraphDataTypeEnum> dataTypeMap = graphVertexEntity.getGraphVertexType().getDataTypeMap();
                List<String> mustFields = graphVertexEntity.getGraphVertexType().getMustFields();
                mustFields.stream().forEach(field -> {
                    if (!field.equals("id")) {
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
            }
        });
//        tableAndFields.append(")");
        String tableAndFieldsStr = tableAndFields.toString();
        tableAndFieldsStr = tableAndFieldsStr.substring(0, tableAndFields.length() - 1);
        tableAndFieldsStr = tableAndFieldsStr + ")";

        String valuesStr = values.toString().replace(",)", ")");
        valuesStr = valuesStr.substring(0, valuesStr.length() - 1);
//        String sqlSet = values.delete(0, 1).toString();
        return String.format(VERTEX_INSERT_SQL, tableAndFieldsStr, valuesStr);
    }


    @Override
    public List<GraphVertexEntity<T>> getGraphVertexEntityList() {
        return this.graphVertexEntities;
    }

    @Override
    public GraphVertexType<T> getGraphVertexType() {
        return this.graphVertexType;
    }

    @Override
    public List<String> getSqlList() throws NebulaException {
//        if (this.batchSize == 1) {
        return Lists.newArrayList(getOneVertexSql());
//        }
//        return getMultiVertexSql();
    }

    @Override
    public List<GraphLabel> getLabels() {
        return Lists.newArrayList(this.getGraphVertexType());
    }
}
