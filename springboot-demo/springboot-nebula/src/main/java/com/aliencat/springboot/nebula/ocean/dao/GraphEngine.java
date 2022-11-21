package com.aliencat.springboot.nebula.ocean.dao;


import com.aliencat.springboot.nebula.ocean.domain.GraphLabel;

import java.util.List;

/**
 * Description  GraphEngine is used for
 *
 * @author Anyzm
 * Date  2021/8/10 - 11:16
 * @version 1.0.0
 */
public interface GraphEngine extends BatchSql {

    /**
     * 获取操作标签(TAG || 关系(边))
     *
     * @return
     */
    List<GraphLabel> getLabels();

}
