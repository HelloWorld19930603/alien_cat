package com.aliencat.communication.netty.param;

import lombok.Data;

@Data
public class Response {

    private long id;
    private Object content;
    private int status;//响应码，0表示成功，1表示失败
    private String msg;//响应信息

}
