package com.aliencat.springboot.nebula.pojo;

import lombok.Data;

/**
 * 手机号码的点表
 */
@Data
public class PhoneNumberTagDto {
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
     * 手机号码
     */
    private String phoneNumber;
}