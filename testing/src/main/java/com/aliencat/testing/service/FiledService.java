package com.aliencat.testing.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class FiledService {

    @Value("${system.limit}")
    private Long limit;

    public Long getLimit() {
        return limit;
    }
}
