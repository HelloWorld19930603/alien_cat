package com.aliencat.springboot.elesticsearch.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

/**
 * @Author chengcheng
 * @Date 2022-06-27
 **/
@Data
@AllArgsConstructor
public class DemoDto {

    private long id;
    private String title;
    private String tag;
    private Date publishTime;

    public DemoDto(long id) {
        this.id = id;
    }
}
