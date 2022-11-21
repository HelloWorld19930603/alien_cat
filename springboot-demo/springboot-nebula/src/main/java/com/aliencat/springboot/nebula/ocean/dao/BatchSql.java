
package com.aliencat.springboot.nebula.ocean.dao;


import com.aliencat.springboot.nebula.ocean.exception.NebulaException;

import java.util.List;

public interface BatchSql {
    /**
     * 获取批量执行的 sql 列表
     *
     * @return
     * @throws NebulaException
     */
    public List<String> getSqlList() throws NebulaException;
}
