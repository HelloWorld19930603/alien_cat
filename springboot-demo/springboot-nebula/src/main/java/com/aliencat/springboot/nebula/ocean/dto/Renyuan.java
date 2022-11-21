package com.aliencat.springboot.nebula.ocean.dto;

import com.aliencat.springboot.nebula.ocean.annotation.GraphProperty;
import com.aliencat.springboot.nebula.ocean.annotation.GraphVertex;
import com.aliencat.springboot.nebula.ocean.enums.GraphKeyPolicy;
import lombok.Data;

@GraphVertex(value = "renyuan", keyPolicy = GraphKeyPolicy.string_key)
@Data
public class Renyuan {

    @GraphProperty(value = "name", required = true)
    private String name;

    public Renyuan() {
    }

    public Renyuan(String name) {

        this.name = name;
    }
}