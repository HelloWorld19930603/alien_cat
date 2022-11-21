
package com.aliencat.springboot.nebula.ocean.session;


import com.aliencat.springboot.nebula.ocean.common.utils.CollectionUtils;
import com.aliencat.springboot.nebula.ocean.domain.impl.QueryResult;
import com.aliencat.springboot.nebula.ocean.enums.ErrorEnum;
import com.aliencat.springboot.nebula.ocean.exception.CheckThrower;
import com.aliencat.springboot.nebula.ocean.exception.NebulaException;
import com.aliencat.springboot.nebula.ocean.exception.NebulaExecuteException;
import com.aliencat.springboot.nebula.ocean.exception.NebulaVersionConflictException;
import com.google.common.collect.Maps;
import com.vesoft.nebula.ErrorCode;
import com.vesoft.nebula.Row;
import com.vesoft.nebula.Value;
import com.vesoft.nebula.client.graph.data.ResultSet;
import com.vesoft.nebula.client.graph.net.Session;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class NebulaSessionWrapper implements NebulaSession {

    private Session session;

    private static final String E_DATA_CONFLICT_ERROR = "E_DATA_CONFLICT_ERROR";

    public NebulaSessionWrapper(Session session) throws NebulaExecuteException, NebulaException {
        CheckThrower.ifTrueThrow(session == null, ErrorEnum.SESSION_LACK);
        this.session = session;
    }

    @Override
    public int execute(String statement) throws NebulaExecuteException {
        ResultSet resultSet = null;
        try {
            log.debug("execute执行nebula,ngql={}", statement);
            resultSet = this.session.execute(statement);
        } catch (Exception e) {
            log.error("更新nebula异常 Thrift rpc call failed: {}", e.getMessage());
            throw new NebulaExecuteException(ErrorCode.E_RPC_FAILURE.getValue(), e.getMessage());
        }
        if (resultSet.getErrorCode() == ErrorCode.SUCCEEDED.getValue()) {
            return ErrorCode.SUCCEEDED.getValue();
        }
        if (resultSet.getErrorCode() == ErrorCode.E_EXECUTION_ERROR.getValue()
                && resultSet.getErrorMessage().contains(E_DATA_CONFLICT_ERROR)) {
            //版本冲突，session内部不再打印错误日志，直接抛出自定义的版本异常
            throw new NebulaVersionConflictException(resultSet.getErrorCode(), resultSet.getErrorMessage());
        }
        log.error("更新nebula异常 code:{}, msg:{}, nGql:{} ",
                resultSet.getErrorCode(), resultSet.getErrorMessage(), statement);
        throw new NebulaExecuteException(resultSet.getErrorCode(), resultSet.getErrorMessage());
    }

    @Override
    public ResultSet executeQuery(String statement) throws NebulaExecuteException {
        ResultSet resultSet = null;
        try {
//            log.info("executeQuery执行nebula,ngql={}", statement);
            long a = System.currentTimeMillis();
//            log.info("查看session对象"+ this.session);
            resultSet = this.session.execute(statement);
            long e = System.currentTimeMillis();
//            log.info(statement + " executeQuery执行nebula,time ={}", (e - a));
        } catch (Exception e) {
            log.error("查询nebula异常 code:{}, msg:{}, nGql:{} ", ErrorCode.E_RPC_FAILURE, e.getMessage(), statement);
            throw new NebulaExecuteException(ErrorEnum.QUERY_NEBULA_EROR, e);
        }
        if (resultSet != null && resultSet.getErrorCode() != ErrorCode.SUCCEEDED.getValue()) {
            log.error("查询nebula异常:{},{},nGql:{}", resultSet.getErrorCode(), resultSet.getErrorMessage(), statement);
            throw new NebulaExecuteException(ErrorEnum.QUERY_NEBULA_EROR);
        }
        return resultSet;
    }

    @Override
    public QueryResult executeQueryDefined(String statement) throws NebulaExecuteException {
        ResultSet resultSet = executeQuery(statement);
        QueryResult queryResult = new QueryResult();
        List<String> columns = resultSet.getColumnNames();
        if (CollectionUtils.isEmpty(columns)) {
            return queryResult;
        }
        List<QueryResult.Row> data = new ArrayList<>();
        List<Row> rows = resultSet.getRows();
        if (CollectionUtils.isEmpty(rows)) {
            return new QueryResult(data);
        }
        for (Row rowValue : rows) {
            // 设置为 columns.size() 优化内存使用
            Map<String, Object> rowMap = Maps.newHashMapWithExpectedSize(columns.size());
            List<Value> columnsValues = rowValue.getValues();
            if (!CollectionUtils.isEmpty(columnsValues)) {
                int size = columnsValues.size();
                for (int i = 0; i < size; i++) {
                    Value columnValue = columnsValues.get(i);
                    Object fieldValue = columnValue.getFieldValue();
                    int setField = columnValue.getSetField();
                    if (setField == Value.SVAL) {
                        fieldValue = fieldValue == null ? null : new String((byte[]) fieldValue);
                    }
                    // 利用 intern 将重复的列名作成享元模式
                    rowMap.put(columns.get(i).intern(), fieldValue);
                }
            }
            data.add(new QueryResult.Row(rowMap));
        }
        return new QueryResult(data);
    }

    @Override
    public void release() {
        this.session.release();
    }

    @Override
    public boolean ping() {
        return this.session.ping();
    }
}
