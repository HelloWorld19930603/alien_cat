package com.aliencat.springboot.aop.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@Builder
@ToString
public class MyError {

    private String message;
    private Integer code;
    private String origin;

}
