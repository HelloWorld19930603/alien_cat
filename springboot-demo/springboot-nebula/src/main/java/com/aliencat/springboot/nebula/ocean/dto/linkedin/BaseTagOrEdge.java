package com.aliencat.springboot.nebula.ocean.dto.linkedin;


import com.aliencat.springboot.nebula.ocean.annotation.GraphProperty;
import com.aliencat.springboot.nebula.ocean.enums.GraphDataTypeEnum;
import lombok.Data;

/**
 * 人员信息的点表
 */
@Data
public class BaseTagOrEdge {
    /**
     * 遍历
     */
    @GraphProperty(value = "ergodic", required = true, dataType = GraphDataTypeEnum.STRING)
    private String ergodic;

    /**
     * 来源
     */
    @GraphProperty(value = "source", required = true, dataType = GraphDataTypeEnum.STRING)
    private String source;
}