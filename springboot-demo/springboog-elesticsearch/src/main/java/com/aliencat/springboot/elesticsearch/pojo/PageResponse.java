package com.aliencat.springboot.elesticsearch.pojo;

import lombok.Data;

import java.util.List;

@Data
public class PageResponse<T> {

    private long total;

    private List<T> result;
}
