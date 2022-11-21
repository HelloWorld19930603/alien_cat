
package com.aliencat.springboot.nebula.ocean.session;

import com.vesoft.nebula.client.graph.data.ResultSet;
import com.aliencat.springboot.nebula.ocean.domain.Session;
import com.aliencat.springboot.nebula.ocean.exception.NebulaExecuteException;

/**
 * Description  NebulaSession is used for
 *
 * @author Anyzm
 * Date  2021/7/15 - 18:26
 * @version 1.0.0
 */
public interface NebulaSession extends Session {


    /**
     * 执行查询
     *
     * @param statement
     * @return
     * @throws NebulaExecuteException
     */
    public ResultSet executeQuery(String statement) throws NebulaExecuteException;


}
