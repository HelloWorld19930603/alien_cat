package com.aliencat.springboot.nebula.pojo;

import lombok.Data;

/**
 * 人员与邮箱之间的关系
 */
@Data
public class PersonToEmailEdgeDto {

    private String fromId;

    private String toId;

    /**
     * 遍历
     */
    private String ergodic;

    /**
     * 来源
     */
    private String source;

    /**
     * 开始时间
     */
    private String startDate;

    /**
     * 结束时间
     */
    private String endDate;

    /**
     * 邮箱的点信息
     */
    private EmailTagDto emailTagDto;
}
