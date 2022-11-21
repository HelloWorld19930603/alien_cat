package com.aliencat.springboot.nebula.pojo;

import lombok.Data;

/**
 * 邮箱的点表
 */
@Data
public class EmailTagDto {
    /**
     * 生成的uuid.
     */
    private String id;

    /**
     * 遍历
     */
    private String ergodic;

    /**
     * 来源
     */
    private String source;

    /**
     * 邮件地址
     */
    private String emailAddress;

    /**
     * 类型
     */
    private String type;

    /**
     * 类型中文翻译
     */
    private String typeCn;
}