package com.aliencat.springboot.elasticsearch.pojo;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class User {
    private Integer id;
    private String username;
}

