package com.aliencat.springboot.swagger2.pojo;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * 响应码
 **/
@Data
@Component
public class ResponseCode {

    public String successCode = "200";

    public String errorCode = "500";

    public String authErrorCode = "300";
}
