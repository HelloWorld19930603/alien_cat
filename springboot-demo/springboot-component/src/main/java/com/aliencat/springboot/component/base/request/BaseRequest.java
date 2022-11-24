package com.aliencat.springboot.component.base.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 公共请求
 *
 * @author cai
 * @version 1.0
 * @date 2022-09-30 12:00
 */
@Data
public class BaseRequest implements Serializable {
    /**
     * 每页显示数量
     */
    private Long pageSize;

    /**
     * 第几页
     */
    private Long pageNo;

    /**
     * 搜索内容
     */
    private String keyword;

    /**
     * 排序字段
     */
    private String orderBy;

    /**
     * 正序或者倒序排列（asc 或 desc）
     */
    private String sort;
}
